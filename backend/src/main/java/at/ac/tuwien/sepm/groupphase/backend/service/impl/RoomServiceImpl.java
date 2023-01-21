package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
  private final RoomRepository repository;

  public RoomServiceImpl(RoomRepository repository) {
    this.repository = repository;
  }

  @Override
  public Room deleteRoom(long id) {
    final Room room = repository.getReferenceById(id);
    repository.deleteById(id);
    return room;
  }

  @Override
  public Room editRoom(long id, Room toEdit) throws NotFoundException {
    toEdit.setId(id);
    return this.repository.save(toEdit);
  }
}
