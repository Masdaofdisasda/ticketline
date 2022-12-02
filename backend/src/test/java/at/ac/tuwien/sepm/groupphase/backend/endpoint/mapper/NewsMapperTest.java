package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NewsMapperTest {

  NewsMapper mapper = NewsMapper.INSTANCE;

  News news = News.builder()
    .id(0L)
    .publishedAt(LocalDate.now())
    .pictureUrl("URLFOrPicture")
    .summary("SummaryOfNews")
    .text("TextForNEws")
    .title("TitleOfNews")
    .build();

  NewsDto newsDto = NewsDto.builder()
    .id(1L)
    .publishedAt(LocalDate.MIN)
    .pictureUrl("anotherURL")
    .summary("anotherSummary")
    .text("anotherText")
    .title("anotherTitle")
    .build();

  @Test
  void newsToNewsDto() {
    NewsDto currentDto = mapper.newsToNewsDto(news);
    assertAll(()->{
      assertThat(currentDto.getId()).isEqualTo(news.getId());
      assertThat(currentDto.getPublishedAt()).isEqualTo(news.getPublishedAt());
      assertThat(currentDto.getPictureUrl()).isEqualTo(news.getPictureUrl());
      assertThat(currentDto.getSummary()).isEqualTo(news.getSummary());
      assertThat(currentDto.getText()).isEqualTo(news.getText());
      assertThat(currentDto.getTitle()).isEqualTo(news.getTitle());
    });
  }

  @Test
  void newsDtoToNews() {
    News currentNews = mapper.newsDtoToNews(newsDto);
    assertAll(()->{
      assertThat(currentNews.getId()).isEqualTo(newsDto.getId());
      assertThat(currentNews.getPublishedAt()).isEqualTo(newsDto.getPublishedAt());
      assertThat(currentNews.getPictureUrl()).isEqualTo(newsDto.getPictureUrl());
      assertThat(currentNews.getSummary()).isEqualTo(newsDto.getSummary());
      assertThat(currentNews.getText()).isEqualTo(newsDto.getText());
      assertThat(currentNews.getTitle()).isEqualTo(newsDto.getTitle());
    });
  }
}