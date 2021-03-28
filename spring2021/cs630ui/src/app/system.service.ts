import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError, Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class SystemService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getOsInfo(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/osinfo`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );
  }

  getServices(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/services`)
      .pipe(
        tap(data => console.log(data)),
        catchError(this.handleError)
      );
  }

  getProcesses(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/processes`)
      .pipe(
        tap(data => console.log(data)),
        catchError(this.handleError)
      );
  }

  getMemory(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/memory`)
      .pipe(
        tap(data => console.log(data)),
        catchError(this.handleError)
      );
  }

  getProcessor(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/processor`)
      .pipe(
        tap(data => console.log(data)),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
      if (error.error.message) {
        errorMessage = `${error.error.message} - Error code ${error.status}`;
      } else {
        errorMessage = `An error occurred - Error code ${error.status}`;
      }
    }
    return throwError(errorMessage);
  }
}
