import {PageDto} from './page.dto';

export class PageResponseDto<T> extends PageDto {
  data: T[];
  constructor(pageIndex: number, pageSize: number, hits: number, pagesTotal: number, data: T[]) {
    super(pageIndex, pageSize, hits, pagesTotal);
    this.data = data;
  }
  static getPageResponseDto(): any {
    return {
      ...this.initialPage(),
      data: []
    };
  }
}
