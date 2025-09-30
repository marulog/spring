package hello.login;

import hello.login.domain.item.Item;
import hello.login.domain.item.ItemRepository;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor // ItemRespository 의존성으로 생성자 주입
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRespository memberRespository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct // 빈 생성, 의존성 주입 이후 init() 자동 생성 -> 빈 초기화 콜백
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test");
        member.setName("tester");
        memberRespository.save(member);
    }

}