package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRespository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor // final, Notnull 키워드 붙으면 생성자 자동 생성
public class HomeController {

    private final MemberRespository memberRespository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    // required = false 로그인 안한 사용자도 입장, 쿠키에서 가져오는 기본 타입은 String임
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model){

        if(memberId==null){
            return "home";
        }

        //로그인 성공 시
        Member loginMember = memberRespository.findById(memberId);
        if(loginMember==null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "members/loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인 성공 시
        if(member==null){
            return "home";
        }

        model.addAttribute("member", member);
        return "members/loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        // 세션이 없으면 -> 신규회원이면 홈페이지로 가렴
        HttpSession session = request.getSession(false);
        if(session == null){
            return "home";
        }

        // 세션에 회원 데이터가 없으면 -> 홈페이지로 가ㅕㅁ
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 -> 로그인 화면으로 이동
        model.addAttribute("member", loginMember);
        return "members/loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        // 세션에 회원 데이터가 없으면 -> 홈페이지로 가ㅕㅁ
        if(loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 -> 로그인 화면으로 이동
        model.addAttribute("member", loginMember);
        return "members/loginHome";
    }

}