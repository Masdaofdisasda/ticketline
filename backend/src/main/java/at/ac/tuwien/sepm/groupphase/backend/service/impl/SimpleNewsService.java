package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleNewsService implements NewsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final NewsRepository newsRepository;
  private final UserRepository userRepository;

  public SimpleNewsService(NewsRepository newsRepository, UserRepository userRepository) {
    this.newsRepository = newsRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Page<News> findAllPaginated(PageDto pageDto) {
    LOGGER.debug("Find all news paginated");
    return newsRepository.findAllPagable(PageRequest.of(pageDto.pageIndex(), pageDto.pageSize()));
  }

  @Override
  public Page<News> findAllUnreadPaginated(Long userId, PageDto pageDto) {
    LOGGER.debug("Find all unread news paginated");
    return newsRepository.findAllUnreadPagable(userId, PageRequest.of(pageDto.pageIndex(), pageDto.pageSize()));
  }

  @Override
  public News findOne(Long id) {
    LOGGER.debug("Find news entry with id {}", id);
    return newsRepository.findNewsById(id).orElseThrow(() ->
      new NotFoundException(String.format("Could not find news entry with id %s", id)));

  }

  @Override
  public void markAsSeen(Long userId, News news) {
    ApplicationUser user = userRepository.findUserById(userId);
    if (!news.getHasSeen().contains(user)) {
      user.addHasSeen(news);
      news.add(user);
      newsRepository.save(news);
    }
  }

  @Override
  public News publishMessage(News news) {
    LOGGER.debug("Publish new news entry {}", news);
    return newsRepository.save(news);
  }

}
