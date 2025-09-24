package hello.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // 타입말고 클래스, 인터페이스 등 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME) // 실행 시점까지유지됨
@Documented
public @interface MyExcludeComponent {

}
