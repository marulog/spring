package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor // final이 붙은 생성자를 만들어줌 this.discountPolicy = discountPolicy;
public class OrderServiceImpl implements OrderService{

    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy(); OCP 위반
    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;

    // 4. 일반 메소드에 의존성 주입 애도 잘 안씀 ㅇㅇ
//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    // 3. 필드에 의존성 주입 > 개쓰레기같은 거임 쓰지말자 ㅇㅇ
//    @Autowired private DiscountPolicy discountPolicy;
//    @Autowired private MemberRepository memberRepository;

//    @Autowired 2. 세터를 통해 의존성 주입, 생성자만들고(빈 생성 후) 다음 의존성 주입됨, 선택적으로 주입
//    public void setDiscountPolicy(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 1. 생성자를 통해 구현체 주입, 근데 생성자가 딱 1개일때는 Autowired 없어도됨, 필수적으로 등록됨
    @Autowired // 해당 에노테이션을 통해서 생성자에 의존된 다른 객체 의존성 주입
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) { //어노테이션 직접 설정
        //  public OrderServiceImpl(DiscountPolicy discountPolicy, @MainDiscountPolicy MemberRepository memberRepository) {
        // 여기에 discountPolicy가 아니라 특정 구현체인 ratediscountPolicy 이렇게 ㅇㅇ
        System.out.println("memberRepository = " + memberRepository);
        System.out.println("discountPolicy = " + discountPolicy);
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository; // -> lombok으로 대체
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트용
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
