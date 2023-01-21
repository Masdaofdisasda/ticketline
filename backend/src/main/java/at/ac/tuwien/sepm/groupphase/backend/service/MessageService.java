package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.springframework.data.domain.Page;

public interface MessageService {

  Page<Message> findAllPaginated(PageDto pageDto);

  /**
   * Find a single message entry by id.
   *
   * @param id the id of the message entry
   * @return the message entry
   */
  Message findOne(Long id);

  /**
   * Mark a message as seen by a userID.
   *
   * @param userId    the userId of the messageRead entry
   * @param messageId the id of the message
   * @return the message entry
   */
  Message hasSeen(Long userId, Long messageId);

  /**
   * Publish a single message entry.
   *
   * @param message to publish
   * @return published message entry
   */
  Message publishMessage(Message message);

}
