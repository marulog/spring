package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlderAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlderAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlderAdapter()); //매핑될 어댑터에다가 v3를 집어넣음
        handlerAdapters.add(new ControllerV4HandlderAdapter()); //매핑될 어댑터에다가 v3를 집어넣음

    }

    private void initHandlerMappingMap() { // ctrl+shfit+m으로 메소드 뽑아내기
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request); // 핸들러 찾기
        if(handler == null) { // 404 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 해당 요청을 해결할 핸들러를 확인하고 해당 컨트럴러와 연결할 어댑터 찾음
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        // 어댑터핸들러를 통해서 컨트롤러에서 처리를 하고 모델뷰를 반환함
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();// 논리 url 획득
        MyView view = viewReslover(viewName); // 완전체 합성

        view.render(mv.getModel(), request, response); // 뷰 호출
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for(MyHandlerAdapter adapter : handlerAdapters){ // 현재 v3어댑터만 들어가 있음
            if(adapter.supports(handler)) { // boolean값 반환
               return adapter;
            } // for
        }
        throw new IllegalArgumentException("handler adpater를 찾을 수 없습니다! handlers=" + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI(); // ctrl+alt+v
        return handlerMappingMap.get(requestURI); // http요청이 들어왔을 때 어떤 컨트롤러를 호출해야할지 리턴함
    }

    private MyView viewReslover(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
