package hello.springtx.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="orders") // 해당 Order 객체는 DB의 orders 테이블과 매핑하여 사용
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String username; // 정상, 예외, 잔고부족
    private String payStatus; // 대기, 완료

}
