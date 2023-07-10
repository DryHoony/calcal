package Hoony.CalCal.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/login")
    public String loginForm(){

        return "member/loginForm";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute Member member, Model model, HttpServletResponse response){
//        log.info("로그인 - member 잘 받았는지 확인 >> {}", member); // ok

        Member loginMember = memberService.login(member);
        // Id값을 받아온 member 객체를 반환해야 한다


        if(loginMember != null){
            log.info("로그인 성공 - {}님 로그인 환영", loginMember.getName());
            // 세션 쿠키
//            Cookie loginCookie = new Cookie("loginName", member.getName());
            Cookie loginCookie = new Cookie("loginId", String.valueOf(loginMember.getId()));
            loginCookie.setPath("/"); // 모든 경로에서 사용
            response.addCookie(loginCookie);

            return "redirect:/";

        }else {
            log.info("로그인 실패");
//            return "redirect:/member/login";
            model.addAttribute("loginFailMessage", "id 또는 비밀번호를 확인해주세요");
            return "member/loginForm";
        }

    }

    @GetMapping("/member/logout")
    public String logout(HttpServletResponse response){
        // 세션만료
        Cookie nameCookie = new Cookie("loginId", null);
        nameCookie.setMaxAge(0);
        nameCookie.setPath("/");
        response.addCookie(nameCookie);

        return "redirect:/";
    }


    @GetMapping("/member/signUp")
    public String signUpForm(){
        return "member/signUpForm";
    }

    @PostMapping("/member/signUp")
    public String signUp(@ModelAttribute Member member, Model model){ // 회원가입
//        log.info("회원가입 - member 잘 받았는지 확인 >> {}", member); // ok

        Member getMember = memberService.findMemberByName(member.getName());

        if(getMember != null){ // 중복!
            log.info("회원가입 - 중복에 걸림!");
            model.addAttribute("signUpFailMessage", "사용할 수 없는 id 입니다.");
            return "member/signUpForm";
        }
        else{
            memberService.join(member);
            // 바로 로그인 되도록 구성

            return "redirect:/";
        }


    }


}
