export class Venue {
  id?: number;
  name: string;
  street: string;
  houseNumber: string;
  city: string;
  zipCode: string;
  country: string;

  rooms?: Array<Room>;
}

export class Room {
  id?: number;
  name: string;
  rowSize: number;
  columnSize: number;

  sectors: Array<Sector>;
}

export class Sector {
  id?: number;
  name: string;

  priceCategory: PriceCategory;

  seats?: Array<Seat>;
}

export class Seat {
  id?: number;
  rowNumber: number;
  colNumber: number;
  rowName?: string;
  colName?: string;
  state: SeatState;
}

export const enum SeatState {
  free = 0,
  reserved = 1,
  taken = 2
}

export class PriceCategory {
  id?: number;
  name: string;
  color: string;
  pricing: number;
}
