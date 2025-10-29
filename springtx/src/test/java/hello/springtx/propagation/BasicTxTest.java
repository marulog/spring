package hello.springtx.propagation;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired // 아래에서 등록한 걸 기반으로 주입받을 수 있음
    PlatformTransactionManager txManager; // dataSource를 받아 트랜잭션을 제어하는 매니저



    @TestConfiguration
    static class Config {
        // @Transcational 없이 직접 트랜잭션 매니저를 호출하여 트랜잭션 시작
        @Bean // 원래 스프링부트가 자동으로 등록시킴 -> 근데 이번엔 수동으로함
        public PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit(){
        log.info("트랜잭션 시작");
        // connection을 가져와서 -> setAutoCommit(false)변경
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 커밋 시작");
        txManager.commit(status); // connection.commit -> AutoCommit true로 변경
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback(){
        log.info("트랜잭션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백 시작");
        txManager.rollback(status); // connection.commit -> AutoCommit true로 변경
        log.info("트랜잭션 롤백 완료");
    }

    @Test
    void double_commit(){
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋 시작");
        txManager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋 시작");
        txManager.commit(tx2);
    }

    @Test
    void double_commit_rollback(){
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋 시작");
        txManager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋 시작");
        txManager.rollback(tx2);
    }

    @Test
    void inner_commit(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction()); //새롭게 생성된 트랜잭션임?

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction()); // 이미 외부에서 실행중일 때 또 실행된 트랜잭션
        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);
    }

    @Test
    void outer_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);

        log.info("외부 트랜잭션 롤백");
        txManager.rollback(outer);
    }

    @Test
    void inner_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner); // 실제 롤백x -> rollback-only에 표시함

        log.info("외부 트랜잭션 커밋");
        // 물리 트랜잭션이 커밋했지만, rollback-only에서 rollback이 적혀있어서
        // 실제로 커밋을 요청했지만 롤백이됨 -> 스프링이 예상치 못한 예외 발생 UnexpectedRollback
        Assertions.assertThatThrownBy(()-> txManager.commit(outer))
                .isInstanceOf(UnexpectedRollbackException.class);

    }

    // 트랜잭션 당 트랜잭션 매니저가 커넥션을  관리함
    // 트랜잭션 동기화 매니저는 스레드 당 1개만 존재함
    // 1개의 스레드에 여러 트랜잭션이 생성될 수 있음
    // 즉, 어떤 트랜잭션 생성 옵션을 사용하냐에 따라 커넥션 개수가 정해짐
    @Test
    void inner_rollback_requires_new(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction()); //새롭게 생성된 트랜잭션임?


        // 외부 커넥션 잠시 중단하고 새로운 트랜잭션 생성 및 커넥션 획득
        log.info("내부 트랜잭션 시작");
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 참여하지말고, 새로운 트랜잭션 생성해라
        TransactionStatus inner = txManager.getTransaction(definition);
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction()); // 이미 외부에서 실행중일 때 또 실행된 트랜잭션


        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner); // 롤백

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer); // 커밋

    }

}
