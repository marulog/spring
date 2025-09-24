package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {

    // 동시성 문제가 고려되지 않았음 실무에서는 다른 자료구조 선택
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository();

    // 해당 메소드를 통하여 인스턴스로 접근 가능, statc이므로 객체로 접근
    public static MemberRepository getInstance() {
        return instance;
    }


    // 외부에서 생성해서 객체 더 만들지 못하게 막음 -> 싱글톤
    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }


    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
