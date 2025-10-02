package hello.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("Request  [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(request, response); // 다음 필터 또는 서블릿 호출 -> 컨트롤러 실행[
        } catch (Exception e) { // 컨트롤러 호출 후 -> 에러발생 -> 그 에러를 잡음
            log.info("Exception! {}", e.getMessage());
            throw e; // 잡은 에러를 뜨로우 했으므로 서블릿 컨테이너까지 감
        } finally {
            log.info("Response [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        } // 서블릿 컨네이너에서 해당 에러를 확인하고 다른 디스패처 서블렛으로 변경전에
        // finally문을 마무리하고 다른 내부 요청을 보냄
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
