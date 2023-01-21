import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventSearchResultComponent} from './event-search-result.component';

describe('EventSearchResultComponent', () => {
  let component: EventSearchResultComponent;
  let fixture: ComponentFixture<EventSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventSearchResultComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EventSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit pageChangedEvent', () => {
    spyOn(component.pageChangedEvent, 'emit');
    component.refreshEvents();
    expect(component.pageChangedEvent.emit).toHaveBeenCalled();
  });
});
