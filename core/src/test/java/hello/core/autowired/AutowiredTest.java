package hello.core.autowired;

import hello.core.member.Member;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.bean.override.convention.TestBean;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption(){
      ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean{
        // 디폴트는 true, false->에러는 안나게 할건데 호출은 안됨 ㅅㄱ
        @Autowired(required = false) // 등록이 안된 빈이라면 -> 의존관계설정할 떼 호출 안됨
        public void setNoBean1(Member noBean1){ // 현재 스프링 컨테이너에 등록이 안된 빈
            System.out.println("noBean1 =  "+ noBean1);
        }

        @Autowired // 호출은 시켜줄게 근데 null임
        public void setNoBean2(@Nullable Member noBean2){
            System.out.println("noBean2 =  "+ noBean2);
        }

        @Autowired // 값이 없으면 옵서녈로 박싱할거야~
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 =  "+ noBean3);
        }

    }
}
