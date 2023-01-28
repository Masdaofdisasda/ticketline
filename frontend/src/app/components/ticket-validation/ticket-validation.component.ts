import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TicketService} from '../../services/ticket.service';
import {Observable} from 'rxjs';
import {TicketValidationDto} from '../../dto/ticketValidation.dto';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-ticket-validation',
  templateUrl: './ticket-validation.component.html',
  styleUrls: ['./ticket-validation.component.scss'],
})
export class TicketValidationComponent implements OnInit {
  validationTicket$: Observable<TicketValidationDto>;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService,
    private spinnerService: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.spinnerService.show().then(() => this.route.params.subscribe(() => {
      this.validationTicket$ = this.ticketService.checkTicketValidation(this.route.snapshot.queryParamMap.get('hash'));
    }));
  }
}
