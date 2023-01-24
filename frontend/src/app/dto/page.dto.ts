export class PageDto {
  pageIndex = 0;

  pagesTotal = 1;

  pageSize = 10;
  hits = 0;


  constructor(pageIndex: number, pageSize: number, hits: number, pagesTotal: number) {
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.hits = hits;
    this.pagesTotal = pagesTotal;
  }

  static initialPage(): PageDto {
    return new PageDto(0, 10, 0, 0);
  }
}
