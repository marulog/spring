package hello.core.common;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // 프록시 객체 사용
// 처음 scan으로 모든 빈을 조회하고 등록하는데 이 과정에서 http요청이 들어와야지 생성되는 웹 스코프가 문제임
// 이거를 해결하기 위해서 프록시라는 가짜 객체를 미리 컨테이너에 등록해놓고 진짜 http요청이 들어왔을 때 호출함
public class MyLogger {

    private  String uuid;
    private  String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "]" + "[" + message + "]");
    }

        @PostConstruct
        public void init() {
            uuid = UUID.randomUUID().toString();
            System.out.println("[" + uuid + "]  request scope been created: " +  this   );
        }

        @PreDestroy
        public void close() {
            System.out.println("[" + uuid + "]  request scope been closed: " +  this   );

        }
    }

