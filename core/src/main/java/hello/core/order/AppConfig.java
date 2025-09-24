package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정정보
public class AppConfig { // 구현체를 지정해줌 -> 구성

    //@Bean memberService -> new MemoryMemmerRepository()
    //@Bean orderService -> .. 객체가 2개씩 생성됨 이러면 싱글톤 안되는거 아님?
    @Bean // 적으면 스프링 컨테이너에 등록이 됨
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        // 생성자를 통해서 어떤 구현체를 선택할건지 지정해줌
        // new DBMemberReposritoy로 변경 가능 ㅇㅇ
        return new MemberServiceImpl(memberRepository()); // 리턴값으로 어떤 구현체를 보내줄지 지정해줌
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        // 생성자를 통해 2개의 구연체 주입
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
