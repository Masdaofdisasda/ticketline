import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appExpanded]'
})
export class ExpandedDirective {

  constructor(public host: ElementRef) { }

}
