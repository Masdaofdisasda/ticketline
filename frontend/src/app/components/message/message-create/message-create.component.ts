import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormControl, FormGroup, UntypedFormBuilder, Validators} from '@angular/forms';
import {MessageService} from 'src/app/services/message.service';
import {AuthService} from '../../../services/auth.service';
import {MessageDto} from '../../../dto/messageDto';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {MessageCreateDto} from '../../../dto/messageCreateDto';

@Component({
  selector: 'app-message',
  templateUrl: './message-create.component.html',
  styleUrls: ['./message-create.component.scss']
})
export class MessageCreateComponent implements OnInit {
  createFormGroup: FormGroup;
  selectedFile: File;

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;

  currentMessage: MessageDto;

  private message: MessageDto[];

  constructor(
    private messageService: MessageService,
    private ngbPaginationConfig: NgbPaginationConfig,
    private notification: ToastrService,
    private formBuilder: UntypedFormBuilder,
    private router: Router,
    private cd: ChangeDetectorRef,
    public authService: AuthService,
    private modalService: NgbModal) {
  }

  ngOnInit() {
    this.loadMessage();
    this.createFormGroup = this.formBuilder.group({
      messageTitle: new FormControl('', Validators.required),
      messagePublishedAt: new FormControl('', Validators.required),
      messageSummary: new FormControl('', Validators.required),
      messageText: new FormControl('', Validators.required)
    });
  }

  getMessage(): MessageDto[] {
    return this.message;
  }

  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  onSubmit(): void {
    const pictureUpload = new FormData();
    pictureUpload.append('imageFile', this.selectedFile, this.selectedFile.name);
    const observableFileUpload = this.messageService.uploadPicture(pictureUpload);

    observableFileUpload.subscribe({
      next: (data) => {
        this.createMessage(data.generatedFilename);
        this.notification.success(
          `Picture: \'${data.originalFilename}\' successfully uploaded.`
        );
      },
      error: (err) => {
        console.log('Error uploading picture', err);
        this.notification.error('Error uploading picture');
      }
    });
  }


  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
  private createMessage(generatedFileName: string) {
    const observable = this.messageService.createMessage({
      title: this.createFormGroup.get('messageTitle').value,
      publishedAt: this.createFormGroup.get('messagePublishedAt').value.toISOString(),
      summary: this.createFormGroup.get('messageSummary').value,
      text: this.createFormGroup.get('messageText').value,
      fileName: generatedFileName
    } as MessageCreateDto);

    observable.subscribe({
      next: (data) => {
        this.notification.success(
          `News: \'${data.title}\' successfully created.`
        );
        this.router.navigate(['/message']);
      },
      error: (err) => {
        console.log('Error creating Event', err);
        console.log(err);
        this.notification.error('Error creating event: \n' + err.error.errors);
      },
    });
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
