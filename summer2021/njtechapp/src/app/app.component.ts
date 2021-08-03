import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, startWith, catchError } from 'rxjs/operators';
import { DataState } from './enum/datastate.enum';
import { AppState } from './interface/appstate';
import { CustomHttpResponse } from './interface/custom-http-response';
import { NotificationService } from './service/notification.service';
import { StudentService } from './service/student.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  appState$: Observable<AppState<CustomHttpResponse>> | undefined;
  readonly DataState = DataState;
  private dataSubject = new BehaviorSubject<CustomHttpResponse>(null);
  private isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoading.asObservable();

  constructor(private studentService: StudentService, private notifier: NotificationService) { }


  ngOnInit(): void {
    this.getStudents();
  }

  getStudents(): void {
    this.appState$ = this.studentService.students$
      .pipe(
        map(response => {
          this.notifier.onSuccess(response.message);
          this.dataSubject.next(response);
          return { dataState: DataState.LOADED, data: response }
        }),
        startWith({ dataState: DataState.LOADING }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.ERROR, error })
        })
      );
  }

  getClassInfo(): void {
    this.appState$ = this.studentService.classinfo$
      .pipe(
        map(response => {
          this.notifier.onSuccess(response.message);
          this.dataSubject.next(response);
          const element = document.getElementById("classinfo");
          element.classList.add("active");
          return { dataState: DataState.LOADED, data: response }
        }),
        startWith({ dataState: DataState.LOADED }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.ERROR, error })
        })
      );
  }

  registerStudent(studentForm: NgForm) {
    console.log(studentForm.value);
    this.isLoading.next(true);
    this.appState$ = this.studentService.register$(studentForm.value)
      .pipe(
        map(response => {
          this.notifier.onSuccess("Student with id " + studentForm.value.studentId + " registered successfully");
          //studentForm.resetForm({});
          return { dataState: DataState.LOADED, data: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED, data: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.LOADED, data: this.dataSubject.value })
        })
      );
  }

  getRegistrations() {
    //   var anchor = document.createElement("a");
    //   anchor.setAttribute("href", "#billing");
    //   anchor.setAttribute("data-toggle", "tab");
    //   document.body.appendChild(anchor);
    //   anchor.click();
    this.isLoading.next(true);
    this.appState$ = this.studentService.registrations$
      .pipe(
        map(response => {
          this.notifier.onSuccess(response.message);
          const element = document.getElementById("billing");
          element.classList.add("active");
          return { dataState: DataState.LOADED, data: response }
        }),
        startWith({ dataState: DataState.LOADED }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.LOADED, data: this.dataSubject.value })
        })
      );
  }

}
