export class EventSearchRequest {
  artistName: string;
  country: string;
  city: string;
  street: string;
  zipCode: string;
  venueName: string;
  eventHall: string;
  startTime: string;
  endTime: string;
  category: string;
  nameOfEvent: string;
  pageIndex: number;
  pageSize: number;

  static getEmptyRequest(): EventSearchRequest {
    return {
      artistName: '',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      startTime: '',
      endTime: '',
      category: '',
      nameOfEvent: '',
      pageIndex: 0,
      pageSize: 10
    } as EventSearchRequest;
  }
}
