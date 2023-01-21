export class BookingItem {
  id: number;
  bookedOn: Date;
  orderTotal: number;
  type: 'RESERVATION' | 'PURCHASE';
}
