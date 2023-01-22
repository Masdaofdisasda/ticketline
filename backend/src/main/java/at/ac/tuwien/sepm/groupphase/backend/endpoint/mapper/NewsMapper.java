package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsOverviewDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface NewsMapper {

  @Named("simpleMessage")
  NewsDto newsToNewsDto(News news);

  /**
   * This is necessary since the SimpleMessageDto misses the text property and the collection mapper can't handle
   * missing fields.
   **/
  @IterableMapping(qualifiedByName = "simpleMessage")
  List<NewsDto> newsToNewsDto(List<News> news);

  DetailedNewsDto newsToDetailedNewsDto(News news);

  News detailedNewsDtoToNews(DetailedNewsDto detailedNewsDto);

  News newsCreationDtoToNews(NewsCreationDto newsCreationDto);

  NewsCreationDto newsToNewsCreationDto(News news);

  List<NewsOverviewDto> newsToNewsOverviewDto(List<News> news);

  NewsOverviewDto newsToNewsOverviewDto(News news);
}

