package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class NewsRepositoryTest implements TestData {

  @Autowired
  private NewsRepository newsRepository;

  @Test
  public void givenNothing_whenSaveMessage_thenFindListWithOneElementAndFindMessageById() {
    News news = News.builder()
      .title(TEST_NEWS_TITLE)
      .summary(TEST_NEWS_SUMMARY)
      .text(TEST_NEWS_TEXT)
      .publishedAt(TEST_NEWS_PUBLISHED_AT)
      .build();

    newsRepository.save(news);

    assertAll(
      () -> assertEquals(1, newsRepository.findAll().size()),
      () -> assertNotNull(newsRepository.findById(news.getId()))
    );
  }

}
