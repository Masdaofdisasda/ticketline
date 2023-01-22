import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {NewsDto} from '../dto/newsDto';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {NewsCreateDto} from '../dto/newsCreateDto';
import {UploadResponseDto} from '../dto/uploadResponseDto';
import {PageResponseDto} from '../dto/page-response.dto';
import {PageDto} from '../dto/page.dto';
import {NewsOverviewDto} from '../dto/newsOverviewDto';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  private newsBaseUri: string = this.globals.backendUri + '/news';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  /**
   * Load all messages from the backend
   *
   * @param pageDto information about currently loaded Page
   */
  getPaginatedNews(pageDto: PageDto): Observable<PageResponseDto<NewsOverviewDto>> {
    const params = new HttpParams()
      .set('pageIndex', pageDto.pageIndex)
      .set('pageSize', pageDto.pageSize);

    return this.httpClient.get<PageResponseDto<NewsOverviewDto>>(this.newsBaseUri+'/unread', {params});
  }


  /**
   * Load all messages from the backend
   *
   * @param pageDto information about currently loaded Page
   */
  getPaginatedNewsArchive(pageDto: PageDto): Observable<PageResponseDto<NewsOverviewDto>> {
    const params = new HttpParams()
      .set('pageIndex', pageDto.pageIndex)
      .set('pageSize', pageDto.pageSize);

    return this.httpClient.get<PageResponseDto<NewsOverviewDto>>(this.newsBaseUri, {params});
  }

  /**
   * Loads specific message from the backend
   *
   * @param id of message to load
   */
  getNewsById(id: number): Observable<NewsDto> {
    console.log('Load message details for ' + id);
    return this.httpClient.get<NewsDto>(this.newsBaseUri + '/' + id);
  }

  /**
   * Persists message to the backend
   *
   * @param message to persist
   */
  createNewsEntry(message: NewsCreateDto): Observable<NewsDto> {
    console.log('Create message with title ' + message.title);
    return this.httpClient.post<NewsDto>(this.newsBaseUri, message);
  }

  /**
   * Send a picture to save
   *
   * @param picture to upload
   */
  uploadPicture(picture: FormData): Observable<UploadResponseDto> {
    console.log('upload picture');
    return this.httpClient.post<UploadResponseDto>(this.newsBaseUri+'/picture', picture);
  }
}
