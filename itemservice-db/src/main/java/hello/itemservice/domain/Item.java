package hello.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // jpa에서 관리하는 테이블임
public class Item {

    @Id
    // JPA가 PK를 생성하는 전략을 지정하는 애노테이션
    //Auto -> JPA 구현체가 DB벤더에 맞게 자동으로 전략 생성
    //Auto MySQL -> IDENTITY || 오라클, H2 -> SEQUENCE, H2
    //IDENTITY -> 데이터베이스의 AUTO_INCREMENT 기능 그대로 사용
    //SEQUENCE -> DB의 시퀀스 객체를 사용함 -> insert전에 시퀀스에서 nextval로 값 가져옴
    //table -> 위 방법을 지원하지 않는 DB를 위한 대체 전략
    //UUID -> JPA 표준에서는 정식이 아니지만 실무에서 자주 쓰임
    //UUID -> 구현체는 UUID 타입 필드를 인식해서 내부적으로 UUID.randomUUID() 사용
    // -> 자바 코드 수준에서 고유키를 만듬
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Long id;

    @Column(name ="item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() { // JPA가 관리하는 클래스에서 public or protected의 경우 기본 생성자는 필수임
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
