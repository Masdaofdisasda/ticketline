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

  color?: string;
  pricing?: number;
}

export const enum SeatState {
  unset = 'UNSET',
  free = 'FREE',
  reserved = 'RESERVED',
  taken = 'TAKEN',
  blocked = 'BLOCKED'
}

export class PriceCategory {
  id?: number;
  name: string;
  color: string;

  pricing: number;
}

export class Pricing {
  id?: number;
  performanceId: number;
  priceCategoryId: number;
  pricing: number;
}
