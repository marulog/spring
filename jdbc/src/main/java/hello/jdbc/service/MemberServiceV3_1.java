package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜젝션 - 트랜젝션 매니저
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

//    private final DataSource dataSource; // 이거 직접 사용하면 안됨;;
    private final PlatformTransactionManager transactionManager; // 이거 사용
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money ) throws SQLException {

       // Connection con = dataSource.getConnection();
        // 트랜잭션 시작 위에꺼 대신 사용
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());


        try{
            //실제 비지니스 로직 실행
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);

        } catch (Exception e){ // 중간에 예외 발생시
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        } // 커밋 or 롤백 될때 알아서 트랜잭션 매니저가 커넥션 처리함

    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }


    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
