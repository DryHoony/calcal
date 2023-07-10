package Hoony.CalCal.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }




    // 회원가입
    public void join(Member member){

//        if(findMemberByName(member.getName()) != null){
//            log.info("이미 존재하는 Id");
//        }

        // 중복검사
        memberRepository.findByName(member.getName())
                .ifPresent(m ->{
                    throw new IllegalStateException("이미 존재하는 회원!!");
                });

        memberRepository.save(member);
        log.info("{}님 회원가입", member.getName());

    }

    // 회원조회 - 이름(name)으로 찾기
    public Member findMemberByName(String name){
        Optional<Member> getMember = memberRepository.findByName(name);

        if(getMember.isPresent()){
            return getMember.get();
        } else {
            return null; // return 값이 null 이면 조회 결과가 없는 것
        }

        // try-catch 문으로 Exception 관리 ver - 추후 upgrade
//        try{
//            getMember = memberRepository.findByName(name).get();
//        }catch (NoSuchElementException e){
//
//        }

    }

    // 회원조회 - id로 찾기
    public Member findMemberById(int id){
        Optional<Member> getMember = memberRepository.findById(id);
//        log.info("id 로 검색한 member 확인 >> {}", getMember);

        if(getMember.isPresent()){
            return getMember.get();
        } else {
            return null; // return 값이 null 이면 조회 결과가 없는 것
        }

        // try-catch 문으로 Exception 관리 ver - 추후 upgrade
//        try{
//            getMember = memberRepository.findByName(name).get();
//        }catch (NoSuchElementException e){
//
//        }

    }


    // 로그인
    public Member login(Member member){
        Member getMember = findMemberByName(member.getName());

        if(getMember == null){ // equal() >> NullPointException
            return null; // Id 없음
        }
        if(!getMember.getPassword().equals(member.getPassword())){
            return null; // 비밀번호 불일치
        }
        // 세션쿠기에 사용하도록 id값 받아와야 한다

        return getMember; // 로그인 ok

    }




//    // 회원목록 - 전체조회
//    public List<Member> findMembers() {
//        return memberRepository.findAll();
//    }
//    // 비밀번호 수정
//    public void modifyPassword(Member member){
////        System.out.println("Service 에서 처리 >> " + member.getName() + "의 수정할 비밀번호 " + member.getPassword());
//        memberRepository.update(member);
//    }
//    // 회원탈퇴
//    public void withdraw(Member member){
//        memberRepository.delete(member);
//    }





}
