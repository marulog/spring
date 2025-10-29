package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

    @Test
    void internalCall(){ // 바로 트랜잭션 적용
        callService.internal();
    }

    @Test
    void externalCall(){
        callService.external();
        // 프록시 객체가 트랜잭션 적용을 안함
        // 트랜잭션을 적용하지 않은 채로 프록시 객체가 callService의 external호출
        // external 내부의 internal 호출-> 프록시가 호출하는게 아닌
        // 실제 객체의 인스턴스가 호출함
        // 이걸막기 위해서 internal 메소드를 따로 분리해야됨

    }

    @TestConfiguration
    static class InternalCallV1TestConfig{
        @Bean
        CallService callService(){
            return new CallService();
        }
    }


    // 안의 메소드 중 하나라도 @Transactional이 붙은 메소드가 있으면
    // 해당 클래스 전체를 트랜잭션 AOP 대상으로 등록하여 프록시 생성함
    static class CallService{

        // 외부에서 호출을 하고 트랜잭션 메소드 호출!
        // 클라이언트측에서 직접 호출한게 아님
        public void external(){
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional // readOnly 디폴트 값은 false임
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", readOnly);
        }
    }
}
