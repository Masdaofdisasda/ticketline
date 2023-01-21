import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ColorPickerDirective} from 'ngx-color-picker';

@Component({
  selector: 'app-color-input-field',
  templateUrl: './color-input-field.component.html',
  styleUrls: ['./color-input-field.component.scss']
})
export class ColorInputFieldComponent implements OnInit {

  @Input()
  _color: string;

  @Output()
  colorChange = new EventEmitter<string>();

  @Output()
  contentChange = new EventEmitter<void>();

  @ViewChild('colorPickerInput')
  input: ElementRef;

  @ViewChild('colorIndicator')
  colorIndicator: ElementRef;

  @ViewChild(ColorPickerDirective)
  pickerDirective: ColorPickerDirective;

  valid: boolean;
  touched: boolean;

  constructor() { }
  get color() {
    return this._color;
  }

  @Input()
  set color(color: string) {
    this._color = color?.replace('#', '');
    this.colorChange.emit(this._color);
  }

  ngOnInit(): void {
  }

  openPicker(): void {
    this.pickerDirective.openDialog();
  }

  validate(): boolean {
    return this.color.match(/^([0-9a-fA-F]{3}){1,2}\b/gm) !== null;
  }

  update(color?: string): void {
    this.color = color || this.color;
    this.touched = true;
    this.valid = this.validate();
    this.contentChange.emit();
  }

}
