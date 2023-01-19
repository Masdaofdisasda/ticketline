import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {MessageDto} from '../dto/messageDto';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {MessageCreateDto} from '../dto/messageCreateDto';
import {UploadResponseDto} from '../dto/uploadResponseDto';
import {PageResponseDto} from "../dto/page-response.dto";
import {ExtendedEventDto} from "../dto/extended-event.dto";
import {PageDto} from "../dto/page.dto";
import {NewsOverviewDto} from "../dto/newsOverviewDto";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messageBaseUri: string = this.globals.backendUri + '/messages';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  /*
   * Load all messages from the backend
   */
  getPaginatedMessage(pageDto: PageDto): Observable<PageResponseDto<NewsOverviewDto>> {
    const params = new HttpParams()
      .set('pageIndex', pageDto.pageIndex)
      .set('pageSize', pageDto.pageSize);

    return this.httpClient.get<PageResponseDto<NewsOverviewDto>>(this.messageBaseUri, {params});
  }

  /**
   * Loads specific message from the backend
   *
   * @param id of message to load
   */
  getMessageById(id: number): Observable<MessageDto> {
    console.log('Load message details for ' + id);
    return this.httpClient.get<MessageDto>(this.messageBaseUri + '/' + id);
  }

  /**
   * Persists message to the backend
   *
   * @param message to persist
   */
  createMessage(message: MessageCreateDto): Observable<MessageDto> {
    console.log('Create message with title ' + message.title);
    return this.httpClient.post<MessageDto>(this.messageBaseUri, message);
  }

  /**
   * Send a picture to save
   *
   * @param picture to upload
   */
  uploadPicture(picture: FormData): Observable<UploadResponseDto> {
    console.log('upload picture');
    return this.httpClient.post<UploadResponseDto>(this.messageBaseUri+'/picture', picture);
  }
}
