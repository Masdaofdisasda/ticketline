package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {

  NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

  News newsDtoToNews(NewsDto newsDto);

  NewsDto newsToNewsDto(News news);
}
