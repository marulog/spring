package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Provider;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
      AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeTest.class);
      PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
      prototypeBean1.addCount();
      Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientussePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeTest.class);
       ClientBean clientBean1 =  ac.getBean(ClientBean.class);
       int count1 = clientBean1.logic();
       Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 =  ac.getBean(ClientBean.class);
        int count2 = clientBean1.logic();
        Assertions.assertThat(count2).isEqualTo(2);
    }

    @Scope("singleton")
    static class ClientBean { // Client빈은 싱글톤 타입이고 생성자를 통해 의존성을 주입받음
//        private final PrototypeBean prototypeBean;

        @Autowired
//        private ObjectProvider<PrototypeBean> prototypeBeanProvider;
        private Provider<PrototypeBean> prototypeBeanProvider; // 라이브러리 사용 get()
        // 프로토타입만 생명주기를 다르게 하고 싶을 때 -> ObjectProvider

//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean){
//            this.prototypeBean = prototypeBean;
//        }

        public int logic() {// 즉 프로토타입과 클라이언트 빈은 생명주기를 공유함
//            prototypeBean.addCount();
//            int count = prototypeBean.getCount();
//            return count;
//            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            // applicationContect를 생성해서 반환해주는게 아닌 프로토타입만 찾아서 반환
            // 즉 스프링컨테이너를 통해서 직접 조회하는게 하신 간단하게 조회하는 방법임
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
         }

         @PostConstruct
        public void init() {
             System.out.println("PrototypeBean.init " + this);
         }

         @PreDestroy
        public void destory() {
             System.out.println("PrototypeBean.destory " + this);
         }
    }
}
