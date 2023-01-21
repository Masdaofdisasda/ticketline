import { Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appAttributes]'
})
export class AttributesDirective {
  @Input()
  appAttributes: object;

  constructor(private host: ElementRef) {
    for (const key in this.appAttributes) {
      if (Object.prototype.hasOwnProperty.call(this.appAttributes, key)) {
        const element = this.appAttributes[key];
        host.nativeElement.setAttribute(key, element);
      }
    }
  }

}
