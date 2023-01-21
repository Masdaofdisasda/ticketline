import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PriceCategory} from '../dto/venue';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class PriceCategoryService {
  private priceCategoryBaseUri: string = this.globals.backendUri + '/price-category';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getAllPriceCategories(): Observable<Array<PriceCategory>> {
    return this.httpClient.get<Array<PriceCategory>>(this.priceCategoryBaseUri);
  }

  addPriceCategory(categoryToAdd: PriceCategory): Observable<PriceCategory> {
    return this.httpClient.post<PriceCategory>(this.priceCategoryBaseUri, categoryToAdd);
  }

  getByRoomId(id: number): Observable<Array<PriceCategory>> {
    return this.httpClient.get<Array<PriceCategory>>(`${this.priceCategoryBaseUri}/room/${id}`);
  }
}
