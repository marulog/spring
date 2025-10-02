package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 요건 이제 원래 내부에서 에러가 터져버리면 500에러를 주게됨
        // 근데 사용자가 잘못한거데 500에러는 좀 그렇자너 ㅋㅋ
        // 그래서 내부에서 이걸로 쇼부를 치고 해결이 되면 정상적인 응답을 보내주고
        // 뷰도 정상적이게 만들어줌 ㅇㅇ 특히 API일때 그럼

        try {
            if (ex instanceof IllegalArgumentException) { // 이 에러면 400으로 바꿀거임
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
                // 정상 작동하는 것처럼 보이기 위해서 HTTP 상태 코드를 400으로 지정하고
                // 빈 ModelAndView를 반환함 -> 그냥 정상작동을함
                // 다만 모델이나 뷰에 정보를 전달하면 뷰를 렌더링함
                // null을 반환하면 다음 리졸버를 실행하는데 없으면 500에러처리함
            }
        } catch (IOException e) {
                log.error("resolver ex", e);
            }


        return null;
    }
}
