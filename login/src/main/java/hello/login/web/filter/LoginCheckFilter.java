package hello.login.web.filter;

import hello.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "logout", "/css/*"};

    //init, destory는 default 메소드라서 구현 안해도됨
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try{
            log.info("인증 체크 필터 시작{}", requestURI);

            // http 요청이 화이트리스트에 있어서 필터로 걸릴경우
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청{}", requestURI);

                    //로그인을 하지 않는 사용자의 경우 로그인으로 redirect
                    //로그인을 하면 가려고했던 페이지 정보도 같이 보냄
                    httpResponse.sendRedirect("login?redirectURL="+ requestURI);
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch(Exception e){
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI );
        }

    }

    /**
     * 화이x트 리스트의 경우 인증 체크X
     * 화이트리스트에 속하지 않는 경우에만 true 반환
     */
    private boolean isLoginCheckPath(String requestURL) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURL);
    }
}
