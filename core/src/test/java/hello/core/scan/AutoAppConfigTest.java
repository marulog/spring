package hello.core.scan;

import hello.core.member.MemberService;
import hello.core.order.AutoAppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutoAppConfigTest { //사용자 정의 어노테이션

    @Test
    void basicScan(){
       AnnotationConfigApplicationContext ac =
               new AnnotationConfigApplicationContext(AutoAppConfig.class);

       MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    void whoRegistersMemberRepositories() {
        var ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        var bf = (org.springframework.beans.factory.support.DefaultListableBeanFactory)
                ac.getAutowireCapableBeanFactory();
        ac.getBeansOfType(hello.core.member.MemberRepository.class)
                .forEach((name, bean) -> {
                    var def = bf.getBeanDefinition(name);
                    System.out.println(name + " -> " + def.getResourceDescription());
                });
    }


}
