package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient {
//    public class NetworkClient implements InitializingBean, DisposableBean { 1번째 방법
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = "+ url);
//        connect();
//        call("초기화 연결 메시지"); -> 콜백함수로 위치 변경
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){
        System.out.println("call: " + url + " message = " + message);
    }

    //서비스 종료 시 호출
    public void disconnect(){
        System.out.println("close: " + url);
    }
// 1번째 방법
//    @Override // 의존성 주입이 끝나면 시작할 콜백 함수
//    public void afterPropertiesSet() throws Exception {
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    @Override // 스프링 컨테이너가 소멸 직전 불리는 콜백 함수
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destory");
//        disconnect();
//    }
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}

