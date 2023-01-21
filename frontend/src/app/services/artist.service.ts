import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Globals } from '../global/globals';
import { ArtistDto } from '../dto/artist.dto';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ArtistService {
  private artistBasUri = this.globals.backendUri + '/artists';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  create(artist: ArtistDto): Observable<ArtistDto> {
    console.log('Sending to : ' + this.artistBasUri + '/create');
    console.log(artist);
    return this.httpClient.post<ArtistDto>(
      this.artistBasUri + '/create',
      artist
    );
  }

  findAll(): Observable<ArtistDto[]> {
    return this.httpClient.get<ArtistDto[]>(this.artistBasUri);
  }

  filterByName(name: string): Observable<ArtistDto[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('name', name);
    return this.httpClient.get<ArtistDto[]>(this.artistBasUri + '/filter', {
      params: queryParams,
    });
  }
}
