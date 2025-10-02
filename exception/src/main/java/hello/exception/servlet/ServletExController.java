package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {
    // 서블릿 자체는 예외가 발생해서 서블릿 밖으로 전달되거나 response.sendError()
    // 가 호출 되었을 때 설정된 페이지를 찾음
    // 여기로 들어오면 강제로 에러를 발생시킴
    // 각 경로에 맞게 response안의 response.isError필드를 true로 변경
    // 에러가 발생했으니 계속 에러를 넘기다가 최상위인 서블릿 컨테이너가 받음
    // 받은 에러안의 response를 뒤져보고 사용자가 만든 에러페이지와 일치하는지 확인함
    // 일치 할 경우 매핑된 해당 에러 페이지 컨트롤러를 다시 호출함
    // 매핑된 진짜 에러 페이지의 경로에 맞는 뷰를 렌더링함
    @GetMapping("/error-ex")
    public void exrrorEx() {
        throw new RuntimeException("예외 발생!"); // 예외 발생 시 서블릿 컨테이너까지 올라감
    }

    @GetMapping("error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }

    @GetMapping("error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400 오류!");
    }

    @GetMapping("error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500 오류!");
    }

}
