export class BookingDetail {
  bookingId: number;
  orderTotal: number;
  boughtByEmail: string;
  type: 'RESERVATION' | 'PURCHASE';
}
