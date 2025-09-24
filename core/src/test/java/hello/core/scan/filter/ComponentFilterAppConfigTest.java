package hello.core.scan.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

public class ComponentFilterAppConfigTest {

//    @Test
//    void filterScan() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfigTest.class);
//        BeanA beanA = ac.getBean("beanA", BeanA.class);
//        Assertions.assertThat(beanA).isNotNull();
//
//        org.junit.jupiter.api.Assertions.assertThrows(
//                NoSuchBeanDefinitionException.class,
//                () -> ac.getBean("beanA", BeanA.class)); // 필터 처리로 빈 등록 안됨)
//    }

    @Configuration // 어~ 설정파일이고 일단 컴포넌트 어노테이션 붙은거 빈으로 등록 ㄱㄱ
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type =FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )

    static class ComponentFilterAppConfig{ // 추가로 설정할건데 MyIncludeComponent가 붙은 것도 빈으로 등록해줘~

    }
}
