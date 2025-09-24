package hello.core.beanfind;

import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        // 빈 출력 -> 빈 이름, 반환할 타입 명시
        MemberService memberService = ac.getBean("memberService", MemberService.class); // 인터페이스로 조회
        System.out.println("memberService = " + memberService); // 객체이름
        System.out.println("memberService.getClass() = " + memberService.getClass());// 구현체 타입

        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        // 빈 출력 -> 빈 이름, 반환할 타입 명시
        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        // 빈 출력 -> 빈 이름, 반환할 타입 명시
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

//    @Test
//    @DisplayName("빈 이름으로 조회 x")
//    void findBeanByNameX() {
//        // 예외 발생시 이렇게 처리하면됨
//        MemberService memberService = ac.getBean("xxxxxx", MemberService.class);
//        assertThrows(NoSuchBeanDefinitionException.class,
//                () -> ac.getBean("xxxx", MemberService.class));
//    }
}
