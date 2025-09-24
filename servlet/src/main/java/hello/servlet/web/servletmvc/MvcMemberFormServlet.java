package hello.servlet.web.servletmvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 컨트롤러 역할 -> jsp로 뷰로 보여줌, 모델은 내부 저장구조 사용
@WebServlet(name ="mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    // 페이지 로딩에는 forward방법과 sendRedirect 방법이 있음
    // forward는 A.jsp -> Servlet -> B.jsp로 넘어갈 때 파라미터를 전달
    // sednRedirect는 파라미터 정보를 제외하고 단순 페이지를 호출함
    // 이때 forward를 RequestDispathcer없이 하면 그 다음 단계에서 파라미터가 소실됨
    // 즉, 파라미터 정보 유지를 위해 RequestDispathcer를 사용하고
    // 현재 request에 담긴 정보를 request에 이름을 지정하여 보관함 (모델)
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String viewPath = "/WEB-INF/views/new-form.jsp";
      RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
      dispatcher.forward(req, resp); // 파라미터를 저장한 채로 페이지 렌더링
        // 근데 리다이렉트가 아니라서 주소는 똑같이 /servlet-mvc/members/new-form 이거임
    }
}
