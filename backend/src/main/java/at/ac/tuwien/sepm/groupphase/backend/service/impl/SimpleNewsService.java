package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
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
import java.util.Optional;

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
  public News findOne(Long id) {
    LOGGER.debug("Find news entry with id {}", id);
    Optional<News> message = newsRepository.findById(id);
    if (message.isPresent()) {
      return message.get();
    } else {
      throw new NotFoundException(String.format("Could not find news entry with id %s", id));
    }
  }

  @Override
  public News hasSeen(Long userId, Long newsId) {
    Optional<News> message = newsRepository.findById(newsId);
    if (message.isPresent()) {
      message.get().getSeenBy().add(userRepository.findUserById(userId));
      return newsRepository.save(message.get());
    } else {
      throw new NotFoundException(String.format("Could not find news entry with id %s", newsId));
    }
  }

  @Override
  public News publishMessage(News news) {
    LOGGER.debug("Publish new news entry {}", news);
    return newsRepository.save(news);
  }

}
