package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜젝션 - 파라미터 연동, 풀을 고려한 종료
 * 실제 서비스 계층에서 커넥션을 열고 닫고 전달해야됨
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money ) throws SQLException {

        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false); // 트랜잭션 시작
            //실제 비지니스 로직 실행
            bizLogic(con, fromId, toId, money);
            con.commit(); // 성공시 커밋

        } catch (Exception e){ // 중간에 예외 발생시
            con.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void release(Connection con) {
        if(con != null){
            try {
                con.setAutoCommit(true); // 커넥션풀에 들어가기전에 변경 -> 디폴트가 false임
                con.close(); // 커넥션 풀에 돌아감
            } catch (Exception e){
                log.info("error", e);
            }
        }
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
