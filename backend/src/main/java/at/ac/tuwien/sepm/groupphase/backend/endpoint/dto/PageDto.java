package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "pageBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {

  private int pageIndex;

  private int pageSize;

  private long hits;

  private int pagesTotal;
}
