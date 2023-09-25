package Hoony.CalCal;

import Hoony.CalCal.member.Member;
import Hoony.CalCal.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class HomeController {

    private final MemberService memberService;

    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(@CookieValue(name = "loginId", required = false) String loginId, Model model){

        // 쿠키값 추가 - 로그인 여부
//        log.info("세션에서 loginId 값 확인 >> {}", loginId);

        if(loginId != null){
            Member loginMember = memberService.findMemberById(Integer.parseInt(loginId));

            log.info("{}님 홈페이지 접속", loginMember.getName());
            model.addAttribute("loginName", loginMember.getName());
        }


        return "home";
    }

}
