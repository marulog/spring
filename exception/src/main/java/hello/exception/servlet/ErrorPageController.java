package hello.exception.servlet;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {
    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION =
            "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE =
            "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE =
            "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI =
            "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME =
            "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE =
            "jakarta.servlet.error.status_code";


    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500"; // 뷰를 반환 -> HTML 렌더링
    }

    // 같은 url로 받아도 클라이언트의 메디어타입에 따라서 우선순위 부여
    @RequestMapping(value="/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api( // 리턴타입으로 ResponseEntity에 map을 담으면 Jackson라이브러리가 자동으로 JSON 변환
            HttpServletRequest request, HttpServletResponse response){
        log.info("API errorPage 500");

       Map<String, Object> result = new HashMap<>();

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));

        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION); // 서블릿 컨테이너가 예외 처리 시점에 담아둔 정보 꺼내기
        result.put("message", ex.getMessage());
//        if(ex != null){ // throw new RuntimeException("~~")으로 호출시 에러객체 생성
//            result.put("message", ex.getMessage());
//        } else{
//            result.put("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
//        }

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode)); // JSON 바디와, 상태 코드르 함게 반환
    }


    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }
}
