import { AfterContentInit, Component, ContentChildren, ElementRef, EventEmitter, Input, OnInit, Output, QueryList } from '@angular/core';
import { CollapsedDirective } from 'src/app/directives/expandable-list/collapsed.directive';
import { ToggleDirective } from 'src/app/directives/expandable-list/toggle.directive';
import { ExpandDirective } from 'src/app/directives/expandable-list/expand.directive';
import { CollapseDirective } from 'src/app/directives/expandable-list/collapse.directive';
import { ExpandedDirective } from 'src/app/directives/expandable-list/expanded.directive';

@Component({
  selector: 'app-expandable-list-item',
  templateUrl: './expandable-list-item.component.html',
  styleUrls: ['./expandable-list-item.component.scss']
})
export class ExpandableListItemComponent implements OnInit, AfterContentInit {

  @Output()
  expanding = new EventEmitter<(boolean?: boolean) => boolean>();

  @Output()
  collapsing = new EventEmitter<(boolean?: boolean) => boolean>();

  @Output()
  changing = new EventEmitter<(boolean?: boolean) => boolean>();

  @ContentChildren(CollapsedDirective, {descendants: true})
  collapsedContent?: QueryList<CollapsedDirective>;

  @ContentChildren(ExpandedDirective, {descendants: true})
  expandedContent?: QueryList<ExpandedDirective>;

  @ContentChildren(ToggleDirective, {descendants: true})
  toggleElements?: QueryList<ToggleDirective>;
  @ContentChildren(ExpandDirective, {descendants: true})
  expandElements?: QueryList<ExpandDirective>;
  @ContentChildren(CollapseDirective, {descendants: true})
  collapseElements?: QueryList<CollapseDirective>;

  @Input()
  isExpanded = false;

  constructor() {
  }


  ngOnInit(): void {
  }

  ngAfterContentInit(): void {
    if (this.toggleElements) {
      for (const toggleElement of this.toggleElements) {
        toggleElement.host.nativeElement.addEventListener('click', () => {
          if ((typeof toggleElement.condition === 'boolean' && toggleElement.condition)
            || (typeof toggleElement.condition === 'function' && toggleElement.condition())
            || typeof toggleElement.condition === 'undefined') {
            this.toggle.bind(this)();
          }
        });
      }
    }

    if (this.expandElements) {
      for (const expandElement of this.expandElements) {
        expandElement.host.nativeElement.addEventListener('click', () => {
          if ((typeof expandElement.condition === 'boolean' && expandElement.condition)
            || (typeof expandElement.condition === 'function' && expandElement.condition())
            || typeof expandElement.condition === 'undefined') {
            this.expand.bind(this)();
          }
        });
      }
    }
    if (this.collapseElements) {
      for (const collapseElement of this.collapseElements) {
        collapseElement.host.nativeElement.addEventListener('click', () => {
          if ((typeof collapseElement.condition === 'boolean' && collapseElement.condition)
            || (typeof collapseElement.condition === 'function' && collapseElement.condition())
            || typeof collapseElement.condition === 'undefined') {
            this.collapse.bind(this)();
          }
        });
      }
    }
    this.changing.subscribe((expanded) => {
      // hide and show respective elements
      (expanded() ? this.collapsedContent : this.expandedContent)?.forEach((el) => {
        el.host.nativeElement.setAttribute('style', 'display: none;');
      });
      (!expanded() ? this.collapsedContent : this.expandedContent)?.forEach((el) => {
        el.host.nativeElement.setAttribute('style', 'display: initial;');
      });
    });

    this.changing.emit(this.emitFunc(() => {}));
  }

  collapse(): void {
    this.changing.emit(this.emitFunc(() => this.isExpanded = false));
    this.collapsing.emit(this.emitFunc(() => this.isExpanded = false));
  }

  expand(): void {
    this.changing.emit(this.emitFunc(() => this.isExpanded = true));
    this.expanding.emit(this.emitFunc(() => this.isExpanded = true));
  }

  toggle() {
    (this.isExpanded ? this.collapse : this.expand).bind(this)();
  }

  private emitFunc = function(f: () => void) {
    return (abort?: boolean) => {
      if (!abort) {
        f();
      }
      return this.isExpanded;
    };
  };

  private updateCollapsedAndExpandedElements() {

  }
}
