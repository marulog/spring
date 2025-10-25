package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// <a, b> a: 사용할 객체, b: 해당 객체의 PK 타입
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    //쿼리 메소드 -> 존나 길어염 ㅠㅠ (아래 메소드와 같은 기능 수행)
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //쿼리 직접 실행
    // spql -> Item : 앤티티 클래스 이름 그 뒤에 i  엔티이의 별칭임 이제부터 Item 엔티티는 i로 사용함
    // select i -> 모든 item의 객체를 조회할거임
    // i.item lik : itemName -> 의 itemName의 필드 값이 파라미터로 넘어온 itemname와 일치하냐?
    // :itemName 값에 파라미터로 넘어온 매핑됨 "itemName"으로 넘겨온 String itemName이 매핑됨
    // And i.price <= :price : 파라미터로 넘어온 price 가격보다 해당 필드 값이 낮냐?
    // spql 결과 다수의 레코드가 나올 수 있기 때문에 List로 받음
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
