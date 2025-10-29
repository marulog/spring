package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test // 정상 처리 -> commit됨!
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test // 런타임 예외 -> 롤백처리함!
    void runtimeException(){
        //given
        Order order = new Order();
        order.setUsername("예외");

        //when 예외가 터지면 런타임 예외는 롤백해버림
        Assertions.assertThatThrownBy(()-> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        //then -> 롤백하기 때문에 해당 인스턴스는 비어 있어야됨
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        Assertions.assertThat(orderOptional.isEmpty()).isTrue();
    }

    @Test // 비즈니스 로직 -> commit됨!, 근데 비지니스 상황에서 롤백하고 싶다면
    // 트랜잭션 옵션에 rollbackFor를 사용하면 됨 ㅇㅋㅇㅋ
    void bizException(){
        //given
        Order order = new Order();
        order.setUsername("잔고부족"); // 바로 업데이트 되지 않음!

        //when 비지니스 예외 -> 상태 변경 후 예외, commit 됨
        try {
            orderService.order(order); // 예외가 발생하지만 commit하며 해당 주문건 db에 업데이트
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");

        }
//        Assertions.assertThatThrownBy(()-> orderService.order(order))
//                .isInstanceOf(NotEnoughMoneyException.class);

        //then -> 롤백하기 때문에 해당 인스턴스는 비어 있어야됨
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }
}