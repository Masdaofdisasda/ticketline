import { Host, Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDeleteDialogComponent } from '../components/confirm-delete-dialog/confirm-delete-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDeleteService {

  constructor(private modalService: NgbModal) { }

  public open(type: string, name: string): Promise<boolean>{
    return new Promise<boolean>((resolve, _) => {
      const ref = this.modalService.open(ConfirmDeleteDialogComponent);

      const component = ref.componentInstance as ConfirmDeleteDialogComponent;
      component.type = type;
      component.name = name;

      component.close = (result) => {
        ref.close();
        resolve(result);
      };
    });
  }
}
