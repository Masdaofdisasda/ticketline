import {EventDto} from './event.dto';

export class ExtendedEventDto extends EventDto {
  artistName: string;
  venueName: string;
  eventHallName: string;
}
