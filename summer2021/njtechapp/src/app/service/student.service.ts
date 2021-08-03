import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { CustomHttpResponse } from '../interface/custom-http-response';
import { Student } from '../interface/student';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private readonly server = environment.apiBaseUrl;
  readonly avatarUrl = 'https://robohash.org';

  constructor(private http: HttpClient) { }

  students$ = <Observable<CustomHttpResponse>>
    this.http.get<CustomHttpResponse>(`${this.server}/student/list`)
      .pipe(
        map(response => {
          return {
            ...response, students: response.students.map(student => ({
              ...student,
              avatarUrl: `${this.avatarUrl}/${student.firstName.toLowerCase()}`
            }))
          }
        }),
        tap(console.log),
        catchError(this.handleError)
      );

  registrations$ = <Observable<CustomHttpResponse>>
    this.http.get<CustomHttpResponse>(`${this.server}/student/registrations`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  classinfo$ = <Observable<CustomHttpResponse>>
    this.http.get<CustomHttpResponse>(`${this.server}/student/classinfo`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  register$ = (payload: { studentId: number, courseId: number, sectionId: number, time: string }) => <Observable<CustomHttpResponse>>
    this.http.post<CustomHttpResponse>(`${this.server}/student/register`, payload)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  save$ = (student: Student) => <Observable<CustomHttpResponse>>
    this.http.post<CustomHttpResponse>(`${this.server}/student/save`, student)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  update$ = (student: Student) => <Observable<CustomHttpResponse>>
    this.http.put<CustomHttpResponse>(`${this.server}/student/update`, student)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  delete$ = (noteId: number) => <Observable<CustomHttpResponse>>
    this.http.delete<CustomHttpResponse>(`${this.server}/note/delete/${noteId}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  //   filterStudents$ = (level: Level, data: CustomHttpResponse) => <Observable<CustomHttpResponse>>new Observable<CustomHttpResponse>(subscriber => {
  //     subscriber.next(level === Level.ALL ? data :
  //       <CustomHttpResponse>{
  //         ...data,
  //         message: data.notes.filter(note => note.level === level).length > 0 ? `Notes filtered by ${level.toLowerCase()} priority` : `No notes of ${level.toLowerCase()} priority found`,
  //         notes: data.notes.filter(note => note.level === level)
  //       });
  //     subscriber.complete();
  //   }).pipe(
  //     tap(console.log),
  //     catchError(this.handleError)
  //   );

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage = `An error occurred - ${error.error.developerMessage}`;
    return throwError(errorMessage);
  }

}