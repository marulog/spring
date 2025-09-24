package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {
        // AppConig에 있는 빈들을 직접 등록시킴
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
//        AppConfig appconfig = new AppConfig();
//        MemberService memberService = appconfig.memberService();
        // 멤버서비스구현체가  주입된 멤버서비스 인스턴스 생성
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

//        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember =memberService.findMember(1L);
        System.out.println("new Member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());

    }
}
