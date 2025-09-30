package hello.login.web.session;

import hello.login.domain.member.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){

        //세션 생성 -> 처음 세션 연결할 때 서버에 저장되어있음
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);
        // 서버 입장에서는 response에 쿠키 담아놓음, 이미 웹으로 응답이 나감

        //요청에 응답 쿠키 저장 -> 위에서 세선 생성하고 클라이언트에게 reponsebody에 cookie로 sessionId 넣어줌
        // 웹 브라우저의 요청으로 쿠키를 만들어서 저장?
        //이제 브라우저는 모든 요청을 보낼 때 서버에서 받은 쿠키(sessionId)를 넣어서 보냄
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId=1313-211;


        // 세션 조회
        // 이번 요청부터는 브라우저가 요청헤더에 쿠키를 넣어서 보냈음
        // 서버는 이걸 검증해서 사용자가 맞는지 판단해야됨
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request); // 세선 시간 0으로 세팅
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();

    }

}
