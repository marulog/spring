package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRespository {

    // static 사용
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    // 멤버 저장
    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }
    
    //멤버 찾기
    public Member findById(Long id){
        return store.get(id);
    }

    //멤버 찾기 ver2
    public Optional<Member> findByLoginId(String loginId){
//        List<Member> all = findAll();
//        for (Member m : all) {
//            if(m.getLonginId().equals(loginId)){
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    //멤버 모두 찾기
    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
