import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormGroup, UntypedFormBuilder} from '@angular/forms';
import {NewsService} from 'src/app/services/news.service';
import {AuthService} from '../../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {NewsDto} from '../../../dto/newsDto';
import {Globals} from '../../../global/globals';

@Component({
  selector: 'app-message',
  templateUrl: './news-details.component.html',
  styleUrls: ['./news-details.component.scss']
})
export class NewsDetailsComponent implements OnInit {
  createFormGroup: FormGroup;

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;
  news: NewsDto;
  imageUrl: string;

  constructor(
    private newsService: NewsService,
    private ngbPaginationConfig: NgbPaginationConfig,
    private notification: ToastrService,
    private formBuilder: UntypedFormBuilder,
    private router: Router,
    private cd: ChangeDetectorRef,
    public authService: AuthService,
    private route: ActivatedRoute,
    private globals: Globals,
    private modalService: NgbModal) {
  }

  ngOnInit() {
    this.route.params.subscribe((data) => {

      const newsId = this.route.snapshot.paramMap.get('newsId');
      if (null == newsId) {
        return;
      }

      this.newsService.getNewsById(Number(newsId)).subscribe({
        next: response => {
          this.news = response;
          if (this.news.fileName){
            this.imageUrl = this.globals.backendUri + '/news/picture?path='+response.fileName;
          } else {
            this.imageUrl = 'assets/altpicture.png';
          }
        }
      });
    });
  }



  navigateLatestPage() {
    this.router.navigateByUrl('/news');
  }

}
