import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ColorInputFieldComponent} from 'src/app/components/color-input-field/color-input-field.component';
import {PriceCategory} from 'src/app/dto/venue';
import {PriceCategoryService} from 'src/app/services/price-category.service';

@Component({
  selector: 'app-price-category-dropdown',
  templateUrl: './price-category-dropdown.component.html',
  styleUrls: ['./price-category-dropdown.component.scss']
})
export class PriceCategoryDropdownComponent implements OnInit {

  @ViewChild('dropdownButton')
  dropdownButton: ElementRef;

  @Input()
  selected: PriceCategory;

  @Input()
  priceCategories: Array<PriceCategory>;

  @Input()
  disabled: boolean;

  @Output()
  selectedChange = new EventEmitter<PriceCategory>();

  currentMode = 'default';
  priceCategoryAdd = new PriceCategory();
  colorSimilarTo = null;
  expanded3Digit = false;

  readonly minColorDistance = 40;

  constructor(private priceCategoryService: PriceCategoryService) { }

  ngOnInit(): void {
    this.priceCategoryService.getAllPriceCategories().subscribe({next: (data) => this.priceCategories = data || []});
  }

  addPriceCategory(event: Event, form: NgForm, color: ColorInputFieldComponent): void {
    // prevent the dropdown from closing
    event.stopPropagation();
    if (form.valid && color.valid) {
      this.priceCategoryService.addPriceCategory(this.priceCategoryAdd).subscribe({
        next: (added) => this.priceCategories.push(added),
        error: (err) => console.error(err) // TODO
      });
      this.currentMode = 'default';
      this.priceCategoryAdd = new PriceCategory();
    }
  }

  selectOption(event: any, category: PriceCategory) {
    this.selected = category;
    this.selectedChange.emit(category);
  }

  openCreateView(event: Event) {
    // prevent the dropdown from closing
    event.stopPropagation();
    this.currentMode = 'createNew';
  }

  contentUpdated() {
    this.colorSimilarTo = null;
    this.priceCategories.forEach(category => {
      if (this.colorDistance(category.color, this.priceCategoryAdd.color) < this.minColorDistance) {
        this.colorSimilarTo = category.name;
      }
    });

    if (this.expanded3Digit) {
      const charArray = [...this.priceCategoryAdd.color];
      const last = charArray.splice(-1);
      setTimeout(() => this.priceCategoryAdd.color = charArray.filter((value, index) => index % 2 === 0).join('') + last, 10);
      this.expanded3Digit = false;
    } else if (this.priceCategoryAdd.color.length === 3) {
      setTimeout(() => {
        this.priceCategoryAdd.color = this.convertTo6Digit(this.priceCategoryAdd.color);
        this.expanded3Digit = true;
      }, 50);
    } else {
      this.expanded3Digit = false;
    }
  }

  colorDistance(colorA: string, colorB: string): number {
    colorA = this.convertTo6Digit(colorA);
    colorB = this.convertTo6Digit(colorB);
    let distance = 0;

    for (let i = 0; i < 6; i += 2) {
      // convert every two hex letters to decimal to compute the distance
      const colorAPart = parseInt('0x' + colorA.slice(i, i + 2), 16).valueOf();
      const colorBPart = parseInt('0x' + colorB.slice(i, i + 2), 16).valueOf();

      distance += Math.pow(colorAPart - colorBPart, 2);
    }

    return Math.sqrt(distance);
  }

  convertTo6Digit(color: string): string {
    // convert a three digit hex color to 6 digits
    if (color.length === 3) {
      for (let i = 0; i < 6; i += 2) {
        const char = color.charAt(i);
        color = color.slice(0, i + 1) + char + color.slice(i + 1);
      }
    }
    return color;
  }

  updateColor() {
    this.priceCategoryAdd.color = this.convertTo6Digit(this.priceCategoryAdd.color);
  }
}
