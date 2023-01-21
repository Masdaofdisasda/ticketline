import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TicketItemComponent} from './ticket-item.component';
import {Ticket} from '../../../dto/ticket';

describe('TicketItemComponent', () => {
  let component: TicketItemComponent;
  let fixture: ComponentFixture<TicketItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketItemComponent);
    component = fixture.componentInstance;
    component.ticket = {
      performanceId: null,
      performance: 'performance',
      sector: 'hall A',
      row: 1,
      column: 1,
      seatNumber: 'A1',
      price: 10.01
    } as Ticket;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display price', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('div').textContent).toContain('10.01 EUR');
  });

  it('should display ticket', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('div').textContent).toContain('Sector: hall A, Seat: A1');
  });

});
