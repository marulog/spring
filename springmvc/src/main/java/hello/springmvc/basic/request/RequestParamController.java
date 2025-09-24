package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse respone) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={} age={}", username, age);

        respone.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParmaV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge){

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParmaV3(
            @RequestParam String username,
            @RequestParam int age){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParmaV4(String username, int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParmaRequired(
            @RequestParam(required = true) String username, // 이름 안주면 400 bad request줌
            @RequestParam(required = false) Integer age){ // 나이는 없어도됨, 다만 int타입은 null을 넣을 수 없기 때문에 integer로 변경해야됨
            // requited가 false의 경우 파라미터가 없거나 쿼리로 안넘길때 null이 저장
            // required가 true의 경우 파라미터가 없으면 ""저장, 쿼리로 안넘기면 에러
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParmaDefault(
            @RequestParam(required = true, defaultValue = "guest") String username, // 이름 안주면 400 bad request줌
            @RequestParam(required = false, defaultValue = "-1") int age){ // 나이는 없어도됨, 다만 int타입은 null을 넣을 수 없기 때문에 integer로 변경해야됨
        // null이나 ""을 방지하기 위해 defulatvalue를 지정함
        log.info("username={}, age={}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParmaMap(
            @RequestParam Map<String, Object> paramMap){ // 나이는 없어도됨, 다만 int타입은 null을 넣을 수 없기 때문에 integer로 변경해야됨
        // null이나 ""을 방지하기 위해 defulatvalue를 지정함
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    //before
//    @ResponseBody
//    @RequestMapping("/model-arrtribute-v1")
//    public  String modelAttributeV1(@RequestParam String useranme, @RequestParam int age){
//        HelloData helloData = new HelloData();
//        helloData.setUsername(useranme);
//        helloData.setAge(age);
//
//        // 둘이 같은 거임
//        log.info("username={}. age={}", helloData.getUsername(), helloData.getAge());
//        log.info("helloData={}", helloData);
//        return "ok";
//    }

    //after
    @ResponseBody
    @RequestMapping("/model-arrtribute-v1")
    public  String modelAttributeV1(@ModelAttribute HelloData helloData){
        // 둘이 같은 거임
        log.info("username={}. age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    // after + 생략버전
    @ResponseBody
    @RequestMapping("/model-arrtribute-v2")
    public  String modelAttributeV2(HelloData helloData){
        // 둘이 같은 거임
        log.info("username={}. age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
}
