package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
@Slf4j
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionID";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();


    /**
     * 세선 생성
     * * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * * 세션 저장소에 sessionId와 보관할 값 저장
     * * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response){

        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        log.info("생성한 sessionId={}", sessionId);
        sessionStore.put(sessionId, value); // 세션 스토어에 랜덤한 세션아이디와 value 저장
//        key   : "550e8400-e29b-41d4-a716-446655440000"  (랜덤 세션ID)
//        value : Member{id=1, loginId="test", name="홍길동"}

        //쿠키 생성, 이름과 값 인자로 넘겨줘야함
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie); //클라이언트에 쿠키 전달
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
        // key   : "550e8400-e29b-41d4-a716-446655440000"  (랜덤 세션ID)
        // value : Member{id=1, loginId="test", name="홍길동"}

    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie != null){
            sessionStore.remove(cookie.getValue());
        }
    }

}
