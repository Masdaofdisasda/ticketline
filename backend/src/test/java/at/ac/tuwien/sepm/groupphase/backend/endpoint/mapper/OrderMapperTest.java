package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class OrderMapperTest {

  OrderMapper mapper = OrderMapper.INSTANCE;

  Booking order = Booking.builder()
    .id(0L)
    .build();

  OrderDto orderDto = OrderDto.builder()
    .id(1L)
    .build();

  @Test
  void orderDtoToOrder() {
    Booking currentOrder = mapper.orderDtoToOrder(orderDto);
    assertAll(()->{
      assertThat(currentOrder.getId()).isEqualTo(orderDto.getId());
    });
  }

  @Test
  void orderToOrderDto() {
    OrderDto currentDto = mapper.orderToOrderDto(order);
    assertAll(()->{
      assertThat(currentDto.getId()).isEqualTo(order.getId());
    });
  }
}