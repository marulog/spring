package hello.login.web;

import hello.login.web.argumentresolver.LoignMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration // 이거 설정정보니까 스프링아 확인하렴
public class WebConfig implements WebMvcConfigurer { // 인터셉터를 추가할거임

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoignMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // 작성한 인터셉터 등록
                .order(1) // 순서, 숫자가 낮을수록 먼저 실행함
                .addPathPatterns("/**") // 인터셉터를 적용할 경로
                .excludePathPatterns("/css/", "/*.ico", "/error"); // 제외시킬 경로

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 들어오는 모든 경로에 적용
                .excludePathPatterns("/", "members/add", "/login", "/logout", "/css/**", "/error");
                // 로그인 정보가 필요 없는 위 링크들은 거르지 않을거에욤
    }

//    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return  filterFilterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
        filterFilterRegistrationBean.setOrder(2);
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return  filterFilterRegistrationBean;
    }
}
