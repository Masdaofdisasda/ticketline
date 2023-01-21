import {
  AfterViewInit,
  Component,
  EventEmitter,
  forwardRef,
  Injector,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  ControlValueAccessor,
  NG_VALUE_ACCESSOR,
  NgControl,
} from '@angular/forms';
import { DatePipe } from '@angular/common';
import {
  NgbDatepicker,
  NgbDateStruct,
  NgbPopover,
  NgbPopoverConfig,
  NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { noop } from 'rxjs';
import * as moment from 'moment/moment';
import { isNaN } from 'lodash';
import { ToastrService } from 'ngx-toastr';

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
      multi: true,
    },
  ],
})
export class DateTimePickerComponent
  implements ControlValueAccessor, OnInit, OnChanges, AfterViewInit {
  @Input() placeholder = '';
  @Input() hourStep = 1;
  @Input() minuteStep = 15;
  @Input() secondStep = 30;
  @Input() seconds = true;

  @Input() disabled = false;

  @Input() clear = new EventEmitter<boolean>();

  public ngControl: NgControl;

  inputDatetimeFormat = 'dd/MM/yyyy hh:mm a';

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

  constructor(
    private config: NgbPopoverConfig,
    private inj: Injector,
    private toastr: ToastrService
  ) {
    config.autoClose = 'outside';
    config.placement = 'auto';
  }

  ngOnInit(): void {
    // tslint:disable-next-line: deprecation
    this.ngControl = this.inj.get(NgControl);
  }

  ngOnChanges(changes: SimpleChanges) {
    this.clear.subscribe((bool) => {
      if (bool) {
        this.date = null;
      }
    });
  }

  ngAfterViewInit(): void {}

  writeValue(newModel: string) {
    console.log(newModel);
    if (newModel) {
      const myDate = moment(newModel).toDate();

      this.dateStruct = {
        year: myDate.getFullYear(),
        month: myDate.getMonth() + 1,
        day: myDate.getDate(),
      };

      this.timeStruct = {
        hour: myDate.getHours(),
        minute: myDate.getMinutes(),
        second: myDate.getSeconds(),
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

  parseDate(input: string) {
    if (input.length !== this.inputDatetimeFormat.length + 1) {
      return null;
    }
    const day = parseInt(input.substring(0, 2), 10);
    if (isNaN(day)) {
      console.error('day is not a number');
      return null;
    }
    const month = parseInt(input.substring(3, 5), 10);
    if (isNaN(month)) {
      console.error('month is not a number');
      return null;
    }
    const year = parseInt(input.substring(6, 10), 10);
    if (isNaN(year)) {
      console.error('month is not a number');
      return null;
    }
    let hours = parseInt(input.substring(11, 13), 10);
    if (isNaN(hours)) {
      console.error('hours is not a number');
      return null;
    }
    const minutes = parseInt(input.substring(14, 16), 10);
    if (isNaN(minutes)) {
      console.error('hours is not a number');
      return null;
    }
    const a = input.substring(17, 19);
    console.log(a);
    if (a !== 'AM' && a !== 'PM') {
      console.error('AM or PM could not be parsed');
    }
    if (a === 'PM') {
      hours += 12;
    }
    let date: Date;
    try {
      date = new Date(year, month - 1, day, hours, minutes);
    } catch (error) {
      date = null;
    }
    console.log(year, month, day, hours, minutes);
    return date;
  }

  onInputChange($event: any) {
    if ($event.target.value.length === 0) {
      this.date = null;
      this.onChange(this.date);
    } else {
      const newDate = this.parseDate($event.target.value);
      if (newDate) {
        this.date = newDate;
        this.dateStruct = {
          year: newDate.getFullYear(),
          month: newDate.getMonth() + 1,
          day: newDate.getDate(),
        };

        this.timeStruct = {
          hour: newDate.getHours(),
          minute: newDate.getMinutes(),
          second: newDate.getSeconds(),
        };
        this.setDateStringModel();
      } else {
        this.toastr.error(
          'Could not understand given date: ' +
            $event.target.value +
            ' Please use the following date format: ' +
            this.inputDatetimeFormat +
            ' E.g. 01/01/2022 02:00 PM'
        );
      }
    }
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
        second: dateA.getSeconds(),
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
