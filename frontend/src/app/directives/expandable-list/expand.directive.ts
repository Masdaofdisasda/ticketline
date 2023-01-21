import { Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appExpand]'
})
export class ExpandDirective {

  @Input()
  condition?: boolean|(() => boolean);

  constructor(public host: ElementRef) { }

}
