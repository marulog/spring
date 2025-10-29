package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired LogRepository logRepository;

    /*
    memberService    @Transaction: OFF
    memberRepository @Transaction: ON
    logRepository    @Transaction: ON
     */
    @Test
    void outerTxOff_success(){
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username); // 트랜잭션 시작!


        //when: 모든데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }



    /*
  memberService    @Transaction: OFF
  memberRepository @Transaction: ON
  logRepository    @Transaction: ON 여기서 Exception
   */
    @Test
    void outerTxOff_fail(){
        //given
        String username = "로그예외_outerTxOff_fail";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(()->
                memberService.joinV1(username))
                        .isInstanceOf(RuntimeException.class);

        //when: log 데이터는 롤백됨 -> isEmpty
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }



    /* 단일 트랜잭션 사용
    memberService    @Transaction: ON
    memberRepository @Transaction: OFF
    logRepository    @Transaction: OFF
     */
    @Test
    void singleTx(){
        //given
        String username = "singleTx";
        //when
        memberService.joinV1(username); // 트랜잭션 시작!


        //when: 모든데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /* 논리, 물리 트랜잭션으로 구분 모든 트랙잭션은 성공
memberService    @Transaction: ON
memberRepository @Transaction: ON
logRepository    @Transaction: ON
 */
    @Test
    void outerTxOn_success(){
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username); // 트랜잭션 시작!


        //when: 모든데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }


    /* 모든 트랙잭션이 걸렸는데 내부에서 예외가 터짐
memberService    @Transaction: ON
memberRepository @Transaction: ON
logRepository    @Transaction: ON 여기서 Exception
 */
    @Test
    void outerTxOn_fail(){
        //given
        String username = "로그예외_outerTxOn_fail";

        //when 예외가 터질때 rollback_log에 롤백하라고 찍음!
        org.assertj.core.api.Assertions.assertThatThrownBy(()->
                        memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //when: 내부에서 예외가 터졌으므로 모든 트랜잭션이 롤백됨
        // logRepository에서 예외를 잡지 않음 -> 호출한 MemberService로 예외가 이동
        // outer 트랜잭션에서 예외가 발생 -> 해당 트랜잭션 롤백
        // 신규 트랜잭션이므로 진짜로 롤백함 -> 하위 트랜잭션도 모두 롤백함
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }


    /* 모든 트랙잭션이 걸렸는데 내부에서 예외가 터짐
memberService    @Transaction: ON
memberRepository @Transaction: ON
logRepository    @Transaction: ON 여기서 Exception
*/
    @Test
    void recoverException_fail(){
        //given
        String username = "로그예외_recoverException_fail";

        //when 예외가 터질때 rollback_log에 롤백하라고 찍음!
        org.assertj.core.api.Assertions.assertThatThrownBy(()->
                        memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //when: 내부에서 예외가 터졌으므로 모든 트랜잭션이 롤백됨
        // logRepository에서 예외를 잡고 정상 로직처리를함!
        // 다만 logRepository에서 예외가 터질 때 rollback_log를 남김
        // outer 트랜잭션에서 commit을 실행할 때 -> 내부 rollback_log를 봄
        // commit을 하려 했으나 로그를 보고 롤백을 시도함 -> UnexpectedRollbackExcetpion 발생함!
        //
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }


    /* 모든 트랙잭션이 걸렸는데 내부에서 예외가 터짐 -> 그럼에도 불구하고 정상 처리를 하고 싶음
memberService    @Transaction: ON
memberRepository @Transaction: ON
logRepository    @Transaction: ON(REQUIRES_NEW) 여기서 Exception
*/
    @Test
    void recoverException_success(){
        //given
        String username = "로그예외_recoverException_success";

        //when
        memberService.joinV2(username);


        //when: 내부에서 예외가 터졌으므로 모든 트랜잭션이 롤백됨
        // log쪽에 예외가 터져도 REQURIES_NEW로 나머지 트랜잭션 정상 커밋되게함
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
}