package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")   //쿠키 이용.
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) { //로그인 안한 사용자도 들어와야하므로 required = false로.
                                                                                                            // 쿠키 아이디 String에서 자동으로 Long으로 타입 변환 해줌.
        if (memberId == null) {
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);

        //디비에 없는 경우
        if (loginMember == null) {
            return "home";
        }

        //있으면
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")   //직접 만든 세션 이용.
    public String homeLoginV2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }
        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }
}