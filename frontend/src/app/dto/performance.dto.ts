import {ArtistDto} from './artist.dto';
import {Room} from './venue';

export class PerformanceDto {
  id: number;
  startDate: Date;
  endDate: Date;
  artist: ArtistDto;
  room: Room;
}
