package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SimpleMessageService implements MessageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  public SimpleMessageService(MessageRepository messageRepository, UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Page<Message> findAllPaginated(PageDto pageDto) {
    LOGGER.debug("Find all messages paginated");
    return messageRepository.findAllPagable(PageRequest.of(pageDto.pageIndex(), pageDto.pageSize()));
  }

  @Override
  public Message findOne(Long id) {
    LOGGER.debug("Find message with id {}", id);
    Optional<Message> message = messageRepository.findById(id);
    if (message.isPresent()) {
      return message.get();
    } else {
      throw new NotFoundException(String.format("Could not find message with id %s", id));
    }
  }

  @Override
  public Message hasSeen(Long userId, Long messageId) {
    Optional<Message> message = messageRepository.findById(messageId);
    if (message.isPresent()) {
      message.get().getSeenBy().add(userRepository.findUserById(userId));
      return messageRepository.save(message.get());
    } else {
      throw new NotFoundException(String.format("Could not find message with id %s", messageId));
    }
  }

  @Override
  public Message publishMessage(Message message) {
    LOGGER.debug("Publish new message {}", message);
    message.setPublishedAt(LocalDateTime.now());
    return messageRepository.save(message);
  }

}
