import {ArtistDto} from './artist.dto';
import {Room} from './venue';

export class PerformanceDto {
  id: number;
  startDate: Date;
  endDate: Date;
  room: Room;
  artists: ArtistDto[];

  blockedSeats: number[];
}
