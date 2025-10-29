package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {// 해당 객체가 basicService로 의존관계 주입 요청
    // 해당 객체는 Transacationl이 붙은 클래스 이므로
    // 스프링 컨테이너에 실제 객체 대신에 프록시가 등록됨

    @Autowired BasicService basicService; // 스프링빈으로 등록된 빈을 자동 주입 -> 실제로는 프록시 객체

    @Test
    void proxyCheck(){
        log.info("aop class={}", basicService.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.nonTx();
    }


    @TestConfiguration // 테스트 전용 설정 클래스 -> 내부를 스캔해서 빈으로 등록함
    static class TxApplyBasicConfig{ // 생성자로 의존성 주입
        @Bean // basicService가 빈으로 등록됨
        BasicService basicService(){
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void tx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }

        public void nonTx(){
            log.info("call nontx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
