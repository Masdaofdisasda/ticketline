import { ArtistDto } from './artist.dto';

export class PerformanceDto {
  id: number;
  startDate: Date;
  endDate: Date;
  artist: ArtistDto;
}
