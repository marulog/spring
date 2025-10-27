package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }


    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        // 따로 save안해도 자동으로 sql 날려줌 -> 실제로 commit되는 시점
        // JPA가 만들어서 실행한 SQL
        //  update item set item_name=?, price=?, quantity=? where id=?
        // JPA는 트랜잭션이 커밋되는 시점에, 변경된 엔티티 객체가 있는지 확인
        // 특정 엔티티 객체가 변경된 겅우 update sql을 실행함

    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);

        /* JPA가 만들어서 실행한 SQL
        // JPA가 만들거라 조금 복잡함
         select
  item0_.id as id1_0_0_,
  item0_.item_name as item_nam2_0_0_,
  item0_.price as price3_0_0_,
  item0_.quantity as quantity4_0_0_
 from item item0_
 where item0_.id=?
         */
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
         String jpql = "select i from Item i";
         // 동적쿼리를 생성하기 위해서는 JPQL를 사용해야됨
        // 객체지향 쿼리 언어 커리문 -> JPQL -> SQL

        /*
        JPQL
        select i from Item i
        where i.itemName like concat('%',:itemName,'%')
        and i.price <= :maxPrice

        JPQL -> SQL
        select
        item0_.id as id1_0_,
        item0_.item_name as item_nam2_0_,
        item0_.price as price3_0_,
        item0_.quantity as quantity4_0_
        from item item0_
        where (item0_.item_name like ('%'||?||'%'))
        and item0_.price<=?
         */

        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }
        log.info("jpql={}", jpql);

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList();
    }
}
