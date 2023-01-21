import { Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appToggle]'
})
export class ToggleDirective {

  @Input()
  condition?: boolean|(() => boolean);

  constructor(public host: ElementRef) { }

}
