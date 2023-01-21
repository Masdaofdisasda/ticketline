import { PerformanceDto } from './performance.dto';

export class EventDto {
  id: number;
  name: string;
  category: string;
  startDate: Date;
  endDate: Date;
  performances: PerformanceDto[];
}
