package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@MainDiscountPolicy // 직접만든 어노테이션
@Component
@Primary //Autowired은 타입으로 찾는데 DiscountPolicy는 2개의 구현체가  있어서
        // 무엇을 선택할건지 지정해야됨
        // @Quilifier, @Primary, @Autowired 필드 명 매칭
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
