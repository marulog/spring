package hello.core.member;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class MemoryMemberRepository implements MemberRepository {

    private  static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) { // 회원아이디와 객체 저장
    store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) { // 아이디 반환
        return store.get(memberId);
    }
}
