import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NewsService} from '../../services/news.service';
import {NewsDto} from '../../dto/newsDto';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {UntypedFormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {PageResponseDto} from '../../dto/page-response.dto';
import {PageDto} from '../../dto/page.dto';
import {NewsOverviewDto} from '../../dto/newsOverviewDto';

@Component({
  selector: 'app-message',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;

  pagedProperties = PageResponseDto.getPageResponseDto();

  private news: NewsOverviewDto[];

  constructor(private messageService: NewsService,
              private ngbPaginationConfig: NgbPaginationConfig,
              private formBuilder: UntypedFormBuilder,
              private cd: ChangeDetectorRef,
              private router: Router,
              public authService: AuthService) {
  }

  ngOnInit() {
    this.refreshNews();
  }

  navigateCreatePage() {
    this.router.navigateByUrl('/news/create');
  }

  nextPage(){
    if (this.pagedProperties.pageIndex < this.pagedProperties.pagesTotal-1){
      this.pagedProperties = {pageIndex: this.pagedProperties.pageIndex++, ...this.pagedProperties};
      this.refreshNews();
    }
  }

  previousPage(){
    if (this.pagedProperties.pageIndex > 0){
      this.pagedProperties = {pageIndex: this.pagedProperties.pageIndex--, ...this.pagedProperties};
      this.refreshNews();
    }
  }

  loadNews(): NewsOverviewDto[] {
    return this.news;
  }

  refreshNews() {
    this.loadOldNews(this.pagedProperties);
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  /**
   * Loads the specified page of messages from the backend
   */
  private loadOldNews(pageDto: PageDto) {
    this.messageService.getPaginatedMessage(pageDto).subscribe({
      next: (news: PageResponseDto<NewsOverviewDto>) => {
        this.pagedProperties = news;
        this.news = news.data;
        if (this.pagedProperties.pagesTotal < this.pagedProperties.pageIndex){
          this.pagedProperties.pageIndex = this.pagedProperties.pagesTotal - 1;
          this.refreshNews();
        }
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

}
