package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//~~/*하위 루트로 들어올 시 이 서블릿이 실행됨 -> 모든 요청을 가로챔
@WebServlet(name="frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI(); // ctrl+alt+v

        ControllerV4 controller = controllerMap.get(requestURI); // 매핑정보를 확인해서 컨트롤러 조회 (인스턴스 조회)
        if(controller == null) { // 404 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request); // http서블릿
        Map<String, Object> model = new HashMap<>(); // 추가
        String viewName = controller.process(paramMap, model);//논리이름 new-form 반환해줌


        MyView view = viewReslover(viewName); // 완전체 합성

        view.render(model, request, response); // 뷰 호출
    }

    private MyView viewReslover(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    //HTTP 서블릿에 있던 모든 파라미터를 뽑아서 paramMap 형식으로 반환해줌
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
}
