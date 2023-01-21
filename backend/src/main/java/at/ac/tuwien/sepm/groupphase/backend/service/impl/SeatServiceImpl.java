package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SeatServiceImpl implements SeatService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final SeatRepository repository;

  public SeatServiceImpl(SeatRepository repository) {
    this.repository = repository;
  }

  @Override
  public Seat createSeat(Seat toAdd) {
    LOGGER.trace("addSeat({})", toAdd);
    return this.repository.save(toAdd);
  }

  @Override
  public Seat getById(long id) throws NotFoundException {
    return repository.getReferenceById(id);
  }

  @Override
  public Seat editSeat(long id, SeatEditDto edit) {
    Seat seat = repository.getReferenceById(id);
    if (edit.getRowNumber() != null) {
      seat.setRowNumber(edit.getRowNumber());
    }
    if (edit.getColNumber() != null) {
      seat.setColNumber(edit.getColNumber());
    }
    repository.save(seat);
    return seat;
  }
}
