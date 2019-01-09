import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: Http) { }

  getStatistics() {
    return this.http.get(`http://localhost:8084/api/v1/statistics`)
      .pipe(map(response => response.json()));
  }
}
