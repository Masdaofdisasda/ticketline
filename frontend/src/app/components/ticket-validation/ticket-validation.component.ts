import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TicketService} from '../../services/ticket.service';
import {Observable} from 'rxjs';
import {TicketValidationDto} from '../../dto/ticketValidation.dto';

@Component({
  selector: 'app-ticket-validation',
  templateUrl: './ticket-validation.component.html',
  styleUrls: ['./ticket-validation.component.scss'],
})
export class TicketValidationComponent implements OnInit {
  validationTicket$: Observable<TicketValidationDto>;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.validationTicket$ = this.ticketService.checkTicketValidation(this.route.snapshot.queryParamMap.get('hash'));
    });
  }
}
