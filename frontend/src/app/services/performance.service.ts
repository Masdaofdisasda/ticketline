import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {PerformanceDto} from '../dto/performance.dto';

@Injectable({
  providedIn: 'root'
})
export class PerformanceService {
  private performanceBaseUri: string = this.globals.backendUri + '/performance';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  public getById(id: number): Observable<PerformanceDto> {
    return this.httpClient.get<PerformanceDto>(`${this.performanceBaseUri}/${id}`);
  }

  public getRoomPlanPerformance(id: number): Observable<PerformanceDto> {
    return this.httpClient.get<PerformanceDto>(`${this.performanceBaseUri}/room/${id}`);
  }
}
