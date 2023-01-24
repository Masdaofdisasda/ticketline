package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class PageDtoResponse<T> extends PageDto {

  private List<T> data;

  public PageDtoResponse(int pageIndex, int pageSize, long hits, int pagesTotal, List<T> data) {
    super(pageIndex, pageSize, hits, pagesTotal);
    this.data = Collections.unmodifiableList(data);
  }
}
