package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 1. 파라미터 전송 기능
 * htttp://localhost:8080/request-param?username=hello&age=20
 */


@WebServlet(name ="requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");

        req.getHeaderNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + "=" + req.getParameter(paramName)));
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String userName = req.getParameter("username");
        String age = req.getParameter("age");

        System.out.println("userName = " + userName);
        System.out.println("age = " + age);
        System.out.println();

                //이거 안쓰면 그냥 먼저 들어온거를 처리함
        System.out.println("[이름이 같은 파라미터 조회]");
        String [] usernames = req.getParameterValues("username");
        for(String name : usernames) {
            System.out.println("username = " + name);
        }

        resp.getWriter().write("OK");

    }
}
