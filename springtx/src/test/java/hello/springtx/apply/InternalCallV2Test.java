package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

//    @Test
//    void internalCall(){ // 바로 트랜잭션 적용
//        callService.internal();
//    }

    @Test
    void externalCallV2(){
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig{
        @Bean
        CallService callService(){
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService(){
         return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{ // 프록시 객체로 생성하지않음
        // 다만 테스트 환경이라 CallService도 프록시 객체로 등록됨

        private final InternalService internalService;


        public void external(){
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", readOnly);
        }
    }

    // 프록시 객체로 대리 생성
    static class InternalService{
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
