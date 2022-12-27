import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appCollapsed]'
})
export class CollapsedDirective {

  constructor(public host: ElementRef) { }

}
