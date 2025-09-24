package hello.core.order;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
// ComponenScan을 쓰면 Configuration은 단지 이 클래스가 설정 파일이다라고 알려준느 정도임
@Configuration // 이거 쓰고 @Bean 하던가~ 아님 빈 자동 등록하는 @Component 쓰던가~
@ComponentScan( // Component로 붙여진 구현체를 빈으로 자동 등록해줌
        basePackages = "hello.core",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
) // @Bean 안쓰고 하는법임 기존 Appconfig파일을 제외하기 위해 필터 사용
public class AutoAppConfig {

    // 로그를 통해 알려주지만 수동 빈 등록이 우선권을 가짐
    // 그래서 부트는 수동과 자동빈이 충돌이 나면 에러나게 함
//    @Bean(name ="memoryMemberRespository") // 이름이 같은 빈을 등록하여 조회할 때..
//    MemberRepository memberRepository(){
//        return new MemoryMemberRepository();
//    }
}
