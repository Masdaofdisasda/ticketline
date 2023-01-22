import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TicketService } from '../../services/ticket.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-ticket-validation',
  templateUrl: './ticket-validation.component.html',
  styleUrls: ['./ticket-validation.component.scss'],
})
export class TicketValidationComponent implements OnInit {
  hash: string;

  status: string;

  constructor(
    private route: ActivatedRoute,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((data) => {
      this.hash = this.route.snapshot.queryParamMap.get('hash');
    });
    this.updateStatus();
  }

  updateStatus(): void {
    this.ticketService.checkTicketValidation(this.hash).subscribe((value) => {
      this.status = value.status;
    });
    console.log(this.status);
  }
}
