package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NewsMapperTest implements TestData {

  private final News news = News.builder()
    .id(ID)
    .title(TEST_NEWS_TITLE)
    .summary(TEST_NEWS_SUMMARY)
    .text(TEST_NEWS_TEXT)
    .publishedAt(TEST_NEWS_PUBLISHED_AT)
    .build();
  @Autowired
  private NewsMapper newsMapper;

  @Test
  public void givenNothing_whenMapDetailedMessageDtoToEntity_thenEntityHasAllProperties() {
    DetailedNewsDto detailedNewsDto = newsMapper.newsToDetailedNewsDto(news);
    assertAll(
      () -> assertEquals(ID, detailedNewsDto.getId()),
      () -> assertEquals(TEST_NEWS_TITLE, detailedNewsDto.getTitle()),
      () -> assertEquals(TEST_NEWS_SUMMARY, detailedNewsDto.getSummary()),
      () -> assertEquals(TEST_NEWS_TEXT, detailedNewsDto.getText()),
      () -> assertEquals(TEST_NEWS_PUBLISHED_AT, detailedNewsDto.getPublishedAt())
    );
  }

  @Test
  public void givenNothing_whenMapListWithTwoMessageEntitiesToSimpleDto_thenGetListWithSizeTwoAndAllProperties() {
    List<News> news = new ArrayList<>();
    news.add(this.news);
    news.add(this.news);

    List<NewsDto> newsDtos = newsMapper.newsToNewsDto(news);
    assertEquals(2, newsDtos.size());
    NewsDto newsDto = newsDtos.get(0);
    assertAll(
      () -> assertEquals(ID, newsDto.getId()),
      () -> assertEquals(TEST_NEWS_TITLE, newsDto.getTitle()),
      () -> assertEquals(TEST_NEWS_SUMMARY, newsDto.getSummary()),
      () -> assertEquals(TEST_NEWS_PUBLISHED_AT, newsDto.getPublishedAt())
    );
  }


}
