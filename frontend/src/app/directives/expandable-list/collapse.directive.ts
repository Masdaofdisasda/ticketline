import { Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appCollapse]'
})
export class CollapseDirective {

  @Input()
  condition?: boolean|(() => boolean);

  constructor(public host: ElementRef) { }

}
