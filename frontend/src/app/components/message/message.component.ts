import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {MessageDto} from '../../dto/messageDto';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {UntypedFormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;

  currentMessage: MessageDto;

  private message: MessageDto[];

  constructor(private messageService: MessageService,
              private ngbPaginationConfig: NgbPaginationConfig,
              private formBuilder: UntypedFormBuilder,
              private cd: ChangeDetectorRef,
              private router: Router,
              public authService: AuthService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.loadMessage();
  }

  navigateCreatePage() {
    this.router.navigateByUrl('/message/create');
  }

  getMessage(): MessageDto[] {
    return this.message;
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  /**
   * Sends message creation request
   *
   * @param message the message which should be created
   */
  private createMessage(message: MessageDto) {
    this.messageService.createMessage(message).subscribe({
        next: () => {
          this.loadMessage();
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
        }
      }
    );
  }

  /**
   * Loads the specified page of message from the backend
   */
  private loadMessage() {
    this.messageService.getMessage().subscribe({
      next: (message: MessageDto[]) => {
        this.message = message;
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }


  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

  private clearForm() {
    this.currentMessage = new MessageDto();
    this.submitted = false;
  }

}
