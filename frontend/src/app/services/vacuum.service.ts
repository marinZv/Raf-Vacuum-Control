import {EventEmitter, Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {VacuumStatus} from "../model/vacuum-status";
import {BehaviorSubject, catchError, Observable, tap, throwError} from "rxjs";
import {VacuumResponse} from "../model/responses/vacuum-response";
import {VacuumAction} from "../model/vacuum-action";
import {VacuumErrorResponse} from "../model/responses/vacuum-error-response";
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class VacuumService {

  private vacuumUrl = environment.vacuumsUrl
  private headers = new HttpHeaders({
    'Authorization':'Bearer ' + localStorage.getItem("jwt")
  });


  constructor(private httpClient: HttpClient) { }


  getVacuums(name: string, statusList: VacuumStatus[], dateFrom: number, dateTo: number):Observable<VacuumResponse[]>{
    let params = new HttpParams();

    if(name != ""){
      params = params.append("name", name);
    }

    if(statusList.length > 0){
      params = params.append("statusList", statusList.toString());
    }

    if(dateFrom > -1 && dateTo > -1){
      params = params.append("dateFrom", dateFrom);
      params = params.append("dateTo", dateTo);
    }

    return this.httpClient.get<VacuumResponse[]>(this.vacuumUrl, {
      headers: this.headers,
      params: params
    }).pipe(
      catchError(err => {
        return throwError(()=> new Error(err.error.message));
      })
    )
  }

  addVacuum(name: string): Observable<VacuumResponse>{
    let url = this.vacuumUrl + "/add/" + name;

    return this.httpClient.post<VacuumResponse>(url, {}, {
      headers: this.headers,
    }).pipe(
      catchError(err => {
        return throwError(()=> new Error(err.error.message));
      })
    );
  }


  removeVacuum(id: number):Observable<any>{
    let url = this.vacuumUrl + "/remove/" + id;

    return this.httpClient.delete(url, {
      headers: this.headers
    }).pipe(
      catchError(err => {
        return throwError(()=> new Error(err.error.message));
      })
    );
  }

  vacuumActions(id: number, action: VacuumAction):Observable<any>{
    let url = this.vacuumUrl + "/" + action.toString().toLowerCase() + "/" + id;

    return this.httpClient.post(url, {}, {
      headers: this.headers
    }).pipe(
      catchError(err => {
        return throwError(err.error.message)
      })
    );
  }

  // vacuumActions(id: number, action: VacuumAction): Observable<any> {
  //   let url = `${this.vacuumUrl}/${action.toString().toLowerCase()}/${id}`;
  //
  //   return this.httpClient.post(url, {}, { headers: this.headers }).pipe(
  //     catchError(error => {
  //
  //       console.log("U error sam: " + error.error.message);
  //
  //       const errorMessage = error.error?.message || 'Unknown error occurred';
  //       return throwError(errorMessage);
  //     })
  //   );
  // }


  // vacuumActions(id: number, action: VacuumAction): Observable<any>{
  //   let url = this.vacuumUrl + "/" + action.toString().toLowerCase() + "/" + id;
  //
  //   return this.httpClient.post(url, {}, {
  //     headers: this.headers
  //   }).pipe(
  //     catchError((error: HttpErrorResponse) => {
  //       if (error.error instanceof ErrorEvent) {
  //         // Ako je greška klijentska (npr. mrežni problem), ovde možete obraditi tu situaciju
  //         console.error('An error occurred:', error.error.message);
  //       } else {
  //         // Ako je greška na serveru, ovde pristupite poruci greške koja je vraćena iz servera
  //         console.error(
  //           `Backend returned code ${error.status}, ` +
  //           `body was: ${error.error}`);
  //       }
  //       // Ovde možete proslediti poruku greške dalje
  //       return throwError('Something bad happened; please try again later.');
  //     })
  //   );
  // }

  getVacuumErrors(): Observable<VacuumErrorResponse[]>{

    return this.httpClient.get<VacuumErrorResponse[]>(this.vacuumUrl + "/errors", {
      headers: this.headers
    }).pipe(
      catchError(err => {
        return throwError(err.error.message);
      })
    );
  }

  scheduleTask(id: number, action: VacuumAction, date: number):Observable<any>{
    return this.httpClient.post(this.vacuumUrl + "/schedule", {
      id: id,
      scheduleDate: date,
      action: action
    }, {
      headers: this.headers
    }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }


}
