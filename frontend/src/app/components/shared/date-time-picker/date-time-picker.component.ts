import {
  AfterViewInit,
  Component,
  EventEmitter,
  forwardRef,
  Injector,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR, NgControl} from '@angular/forms';
import {DatePipe} from '@angular/common';
import {NgbDatepicker, NgbDateStruct, NgbPopover, NgbPopoverConfig, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {noop} from 'rxjs';
import * as moment from 'moment/moment';

/*
 * is an extern module from npm, couldn't load over npm i because this library was
 * to old so angular dependencies where outdated
 */
@Component({
  selector: 'app-date-time-picker',
  templateUrl: './date-time-picker.component.html',
  providers: [
    DatePipe,
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DateTimePickerComponent),
      multi: true
    }
  ]
})
export class DateTimePickerComponent implements ControlValueAccessor, OnInit, OnChanges, AfterViewInit {

  @Input() inputDatetimeFormat = 'dd/MM/yyyy H:mm:ss';
  @Input() placeholder = '';
  @Input() hourStep = 1;
  @Input() minuteStep = 15;
  @Input() secondStep = 30;
  @Input() seconds = true;

  @Input() disabled = false;

  @Input() clear = new EventEmitter<boolean>();

  public ngControl: NgControl;

  dateStruct: NgbDateStruct;
  timeStruct: NgbTimeStruct;
  date: Date;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  @ViewChild(NgbDatepicker)
  private dp: NgbDatepicker;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  @ViewChild(NgbPopover)
  private popover: NgbPopover;

  private onTouched: () => void = noop;
  private onChange: (_: any) => void = noop;

  constructor(private config: NgbPopoverConfig, private inj: Injector) {
    config.autoClose = 'outside';
    config.placement = 'auto';
  }

  ngOnInit(): void {
    // tslint:disable-next-line: deprecation
    this.ngControl = this.inj.get(NgControl);
  }

  ngOnChanges(changes: SimpleChanges) {
    this.clear.subscribe(bool => {
      if (bool) {
        this.date = null;
      }
    });
  }

  ngAfterViewInit(): void {
  }

  writeValue(newModel: string) {
    if (newModel) {
      const myDate = moment(newModel).toDate();

      this.dateStruct = {
        year: myDate.getFullYear(),
        month: myDate.getMonth() + 1,
        day: myDate.getDate()
      };

      this.timeStruct = {
        hour: myDate.getHours(),
        minute: myDate.getMinutes(),
        second: myDate.getSeconds()
      };

      this.setDateStringModel();
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  onInputChange($event: any) {
  }

  onDateChange(event: NgbDateStruct) {
    this.setDateStringModel();
  }

  onTimeChange(event: NgbTimeStruct) {
    this.setDateStringModel();
  }

  setDateStringModel() {
    if (!this.timeStruct) {
      const dateA = new Date();
      this.timeStruct = {
        hour: dateA.getHours(),
        minute: dateA.getMinutes(),
        second: dateA.getSeconds()
      };
    }

    if (this.dateStruct) {
      this.date = new Date(
        this.dateStruct.year,
        this.dateStruct.month - 1,
        this.dateStruct.day,
        this.timeStruct.hour,
        this.timeStruct.minute,
        this.timeStruct.second
      );

      this.onChange(this.date);
    }
  }

  inputBlur($event) {
    this.onTouched();
  }

}
