package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// <a, b> a: 사용할 객체, b: 해당 객체의 PK 타입
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
//extends 받는거라서 기본 기능들은 알아서 제공
    // JPARepository 인터페이르르 상속받으면 스프링 데이터 JPA가 프록시 기술
    //을 사용하여 구현 클래스를 만들어주고, 구현 클래스의 인스턴스를 만들어서
    // 스프링 빈으로 등록함

    // jpa 자체는 자바 진영의 대표적인 ORM으로 구현체로 hiberante를 사용함
    // 이를 사용하면 객체에 대한 신경을 많이 써야됨
    // @Entity -> JPA가 사용하는 객체 -> JPA가 해당 클래스를 객체라고인식
    //@Colum -> 보통 객체는 카멜, DB는 언더스코어를 씀 -> 알아서 변환해주는거
    // JPA의 모든 동작은 엔티니 매니저를 통해 이루어지고, 엔티티 매너저의
    // 내부에는 데이터 소스를 가지고 있어서 해당 소스로 데이터베이스에 접근

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
//
