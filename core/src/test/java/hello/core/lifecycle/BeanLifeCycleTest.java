package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    // 컨테이너 생성 -> 스프링 빈 생성(생성자의 경우 의존 주입) -> 의존성 주입 ->
    // 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> 스프링 종료
    // 컨테이너 생성 -> 설정 클래스(LifeCycleConfig)를 읽고 빈을 만들 준비를함
    // networkClient 빈 인스턴스화?? -> @Bean networkClient() 매서드 호출 -> new NetworkClient() 실행
    // url null 값으로 출력
    // 의존성 주입 -> networkClient.setUrl("http://hello-spring.dev");
    // getBean(NetworkClient.class) 인스턴스반환
    // 컨테이너 종료  ac.close
    // 객체 생성 -> 의존관계 주입 이 순서로 진행됨 but 생성자의 경우 동시에 의존성 주입됨
    // 그러면 의존관계까지 완료가 되면 이때부터 외부와의 작업을 해야되는데 이걸 어떻게 아냐?
    // 주입이 끝나며 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양하 기능을 제공함
    // 스프링이 스프링 컨테이너가 소멸되기 직전에 한번 더 소멸 콜백을 줌
    // 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원함
    // 1. 인터페이스(InitiazlizationBean, DisposableBean -> 인터페이스에 의존적임
    // 2. 설정 정보에 초기화 메서드, 종료 메서드 지정 -> @Bean에다가 직접 주입(init, destory)
    // 3. @PostConstruct, @PreDestory 에노테이션 지원
    @Test
    public void lifeCycleTest(){ //빈 생성 -> 객체 생성 -> 생성자 호출 -> url null인 상태로 출력
     ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class); // 컨테이너 등록까지 완료
     NetworkClient client = ac.getBean(NetworkClient.class); // 사용, 네트워크클라이언트 타입의 빈 검색
     ac.close(); // 소멸

    }

    @Configuration //설정정보 -> 빈 생성
    static class LifeCycleConfig{
        // 이 경우 개발자가 직접 빈을 등록한거임 -> 스프링을 통해 Component+Autowired는 다름
        // destoryMethod = (inferred) 가 디폴트값임 -> 추론기능이 있어서 'close', shutdown'이라는 메서드
        // 자동으로 호출해줌
//        @Bean(initMethod = "init", destroyMethod = "close") // 여기서 개발자가 직접 enw, setter 호출함
        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient(); // 생성자 호출
            networkClient.setUrl("http://hello-spring.dev"); // 주입
            return networkClient; // 컨테이너에 등록
        }
    }
}
