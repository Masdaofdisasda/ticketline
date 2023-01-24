import { AfterViewInit, Component, ContentChildren, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ExpandableListItemComponent } from '../expandable-list-item/expandable-list-item.component';

@Component({
  selector: 'app-expandable-list',
  templateUrl: './expandable-list.component.html',
  styleUrls: ['./expandable-list.component.scss']
})
export class ExpandableListComponent implements OnInit, AfterViewInit {

  @ContentChildren(ExpandableListItemComponent)
  private listItemChildren: QueryList<ExpandableListItemComponent>;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    for (const listItem of this.listItemChildren) {
      listItem.expanding.subscribe(() => {
        for (const item of this.listItemChildren) {
          if (item !== listItem) {
            item.collapse();
          }
        }
      });
    }
  }

}
