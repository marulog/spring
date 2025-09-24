package hello.core.discount;

import hello.core.member.Member;
import org.springframework.stereotype.Component;


public interface DiscountPolicy {

    /**
     * *
     * @param member
     * @param price
     * @return 할인 대상 금액
     */
    int discount(Member member, int price);
}
