import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormControl, FormGroup, UntypedFormBuilder, Validators} from '@angular/forms';
import {NewsService} from 'src/app/services/news.service';
import {AuthService} from '../../../services/auth.service';
import {NewsDto} from '../../../dto/newsDto';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {NewsCreateDto} from '../../../dto/newsCreateDto';

@Component({
  selector: 'app-message',
  templateUrl: './news-create.component.html',
  styleUrls: ['./news-create.component.scss']
})
export class NewsCreateComponent implements OnInit {
  createFormGroup: FormGroup;

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;

  constructor(
    private newsService: NewsService,
    private ngbPaginationConfig: NgbPaginationConfig,
    private notification: ToastrService,
    private formBuilder: UntypedFormBuilder,
    private router: Router,
    private cd: ChangeDetectorRef,
    public authService: AuthService,
    private modalService: NgbModal) {
  }

  ngOnInit() {
    this.createFormGroup = this.formBuilder.group({
      messageTitle: new FormControl('', Validators.required),
      messagePublishedAt: new FormControl('', Validators.required),
      messageSummary: new FormControl('', Validators.required),
      messageText: new FormControl('', Validators.required),
      selectedFile: new FormControl(null, Validators.required),
    });
  }

  public onFileChanged(event) {
    this.createFormGroup.get('selectedFile').setValue(event.target.files[0]);
  }

  onSubmit(): void {
    const selectedFile: File = this.createFormGroup.get('selectedFile').value;
    const pictureUpload = new FormData();
    pictureUpload.append('imageFile', selectedFile, selectedFile.name);
    this.newsService.uploadPicture(pictureUpload).subscribe(data => this.createMessage(data.generatedFilename));
  }

  private createMessage(generatedFileName: string) {
    this.newsService.createNewsEntry({
      title: this.createFormGroup.get('messageTitle').value,
      publishedAt: this.createFormGroup.get('messagePublishedAt').value.toISOString(),
      summary: this.createFormGroup.get('messageSummary').value,
      text: this.createFormGroup.get('messageText').value,
      fileName: generatedFileName
    } as NewsCreateDto).subscribe(() => {
      this.notification.success('news entry was created');
      this.router.navigate(['/news']);
    });
  }
}
