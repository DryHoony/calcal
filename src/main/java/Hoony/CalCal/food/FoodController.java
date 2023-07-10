package Hoony.CalCal.food;

import Hoony.CalCal.food.object.*;
import Hoony.CalCal.member.Member;
import Hoony.CalCal.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class FoodController {

    private final MemberService memberService;
    private final FoodService foodService;

    public FoodController(MemberService memberService, FoodService foodService) {
        this.memberService = memberService;
        this.foodService = foodService;
    }


    // 세션쿠키로 받아온 loginId 값이 있다면 "loginName"을 Model 에 추가
    public Member loginCheck(String loginId, Model model){
        Member loginMember = null;
        if(loginId != null){
            loginMember = memberService.findMemberById(Integer.parseInt(loginId));
            model.addAttribute("loginName", loginMember.getName());
        }
        return loginMember;
    }


    //////////////////////////////////////////////////////////////////////////////
    // 음식 검색

    @GetMapping("/food/searchFood")
    public String foodSearchPage(@CookieValue(name = "loginId", required = false) String loginId, Model model){

        loginCheck(loginId, model);

        // 페이지 이동
        return "/food/searchFood";
    }

    @PostMapping("/food/search1")
    public String search1ByName(@RequestParam String searchName,
                                @CookieValue(name = "loginId", required = false) String loginId, Model model) {
        loginCheck(loginId, model);
        log.info("{} 으로 검색1", searchName);

        List<Food> foods = foodService.findFoodByEqualName(searchName);
        model.addAttribute("foods", foods);
        model.addAttribute("searchName", searchName);// 검색어 전달(검색창에 검색기록 유지)

        return "/food/searchFood";
    }

    @PostMapping("/food/search2")
    public String search2ByName(@RequestParam String searchName,
                                @CookieValue(name = "loginId", required = false) String loginId, Model model) {
        loginCheck(loginId, model);
        log.info("{} 으로 검색2", searchName);

        List<Food> foods = foodService.findFoodByBothEndName(searchName);
        model.addAttribute("foods", foods);
        model.addAttribute("searchName", searchName);// 검색어 전달(검색창에 검색기록 유지)
        return "/food/searchFood";
    }

    @PostMapping("/food/search3")
    public String search3ByName(@RequestParam String searchName,
                                @CookieValue(name = "loginId", required = false) String loginId, Model model) {
        loginCheck(loginId, model);
        log.info("{} 으로 검색3", searchName);

        List<Food> foods = foodService.findFoodByIncludingName(searchName);
        model.addAttribute("foods", foods);
        model.addAttribute("searchName", searchName);// 검색어 전달(검색창에 검색기록 유지)
        return "/food/searchFood";
    }




    //////////////////////////////////////////////////////////////////////////////
    // 내 음식

    @GetMapping("/food/myFood")
    public String myFoodPage(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                             @RequestParam(name = "referredMyFood", required = false) String referredMyFood){

        // 비 로그인 - 빠꾸
        if(loginId == null){ // 먼저 로그인을 해주세요 - 알림창
            model.addAttribute("requiredLogin", "먼저 로그인을 해주세요");
            return "home";
        }

        // 로그인시 - 정상실행
        Member loginMember = loginCheck(loginId, model);
        // myFood 로직 - 호출, 출력
        List<MyFood> myFoods = foodService.findMyFoodByMemberId(loginMember.getId());
        model.addAttribute("myFoods", myFoods);

        // mealDTO 로직
        List<MealDTO> mealDTOList = foodService.findMealDTOByMemberId(loginMember.getId());
        model.addAttribute("mealDTOList", mealDTOList);


        if (referredMyFood != null) { // myFood 삭제요청 - 오류 메시지
            model.addAttribute("referredMyFood", "내 식단에 포함된 음식은 삭제할 수 없습니다. 먼저 식단을 수정하거나 삭제해 주세요.");
        }

        return "/food/myFood";
    }


    @PostMapping("/food/myFood/add") // 검색한 음식을 '내음식'에 추가
    public ResponseEntity<String> addMyFood(@RequestBody Map<String, Object> request,
                                            @CookieValue(name = "loginId", required = false) String loginId, Model model) {
        // JSON값 처리
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(foodIdJSON); // 예외 던짐. try-catch 구문으로 처리해도 됨
//        String foodId = jsonNode.get("foodId").asText();
        String foodId = (String) request.get("foodId");
        log.info("myFood에 추가할 foodId값 >> {}", foodId); // ok

        Member loginMember = loginCheck(loginId, model);

        if(loginId != null){
            // 로그인 - 정상로직
            foodService.myFoodAdd(loginMember.getId(), foodId);
            return ResponseEntity.ok("Food added Ok!");
        }
        else{
            System.out.println("로그인 필요! 빠꾸!");
            // 비로그인 - 빠꾸
            // 로그인이 필요한 서비스 입니다. 알림창 출력
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
        }



    }


    @GetMapping("/food/myFood/create")
    public String createMyFood(@CookieValue(name = "loginId", required = false) String loginId, Model model){
        Member loginMember = loginCheck(loginId, model);
        foodService.myFoodCreate(loginMember.getId());

        return "redirect:/food/myFood";
    }

    @GetMapping("/food/myFood/delete/{id}")
    public String deleteMyFood(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                               @PathVariable("id") int myFoodId){
        loginCheck(loginId, model);
        // 비 정상 로직 - 알림 메시지 출력
        List<MealFood> mealFoods = foodService.findMealFoodByMyFoodId(myFoodId);
        log.info("myFood 를 참조하는 mealFood 가 있는지 조회 >> {}", mealFoods);
        if(mealFoods.get(0).getId() != 0){ // 참조하는 값이 존재
            // alert 로직
//            model.addAttribute("referredMyFood", "내 식단에 포함된 음식은 삭제할 수 없습니다. 먼저 식단을 수정하거나 삭제해 주세요.");
            // redirect 하면 Model 값이 전달이 안된다. >> 어떻게 전달할 수 있을까?
            return "redirect:/food/myFood?referredMyFood=yes";
        }else{
            // 정상 로직
            foodService.deleteMyFood(myFoodId);
            return "redirect:/food/myFood";
        }


//        return "/food/myFood"; // myFood 출력기능 안됨
    }

    @GetMapping("/food/myFood/edit/{id}")
    public String editMyFoodForm(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                                 @PathVariable("id") int myFoodId){
        loginCheck(loginId, model);

        // myFood 에 대한 정보를 받아 출력
        MyFood myFood = foodService.findMyFoodByMyFoodId(myFoodId);
        model.addAttribute("myFood", myFood);
        return "/food/myFoodEditForm";
    }

    @PostMapping("/food/myFood/edit")
    public String editMyFood(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                             @ModelAttribute MyFood myFood){
        loginCheck(loginId, model);

        foodService.editMyFood(myFood);
        return "redirect:/food/myFood";
    }




    //////////////////////////////////////////////////////////////////////////////
    // 내 식단

    @GetMapping("/food/myMeal")
    public String myMealPage(@CookieValue(name = "loginId", required = false) String loginId, Model model){
        // 먼저 로그인을 해주세요 - 알림창
        if(loginId == null){
            model.addAttribute("requiredLogin", "먼저 로그인을 해주세요");
            return "home";
        }

        // 로그인시 - 정상실행
        Member loginMember = loginCheck(loginId, model);
        // mealDTO 로직
        List<MealDTO> mealDTOList = foodService.findMealDTOByMemberId(loginMember.getId());
        model.addAttribute("mealDTOList", mealDTOList);


        return "/food/myMeal";
    }


    @GetMapping("/food/myMeal/create")
    public String createMyMeal(@CookieValue(name = "loginId", required = false) String loginId, Model model){
        Member loginMember = loginCheck(loginId, model);
        foodService.createMeal(loginMember.getId());

        return "redirect:/food/myMeal";
    }

    @GetMapping("/food/myMeal/delete/{mealId}")
    public String deleteMyMeal(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                               @PathVariable("mealId") int mealId){
        loginCheck(loginId, model);
        foodService.deleteMeal(mealId);

        return "redirect:/food/myMeal";
    }

    @PostMapping("/food/myMeal/Edit")
    public String editMeal(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                           @RequestBody Meal meal){
        loginCheck(loginId, model);
        log.info("수정할 Meal 데이터 >. {}", meal);
        foodService.editMeal(meal);

        return "redirect:/food/myMeal";
    }

    @PostMapping("/food/mealFood/add")
    public String addMealFood(@CookieValue(name = "loginId", required = false) String loginId, Model model,
                              @RequestParam(name = "myFoodId", required = false) List<Integer> myFoodIdList,
                              @RequestParam(name = "mealId") int mealId){
        loginCheck(loginId, model);

        if(myFoodIdList != null){
            log.info("mealFood '추가' 기능 myFoodId 값 잘 받아오는지 확인 >> {}", myFoodIdList.size());
            log.info("어떤 식단에 추가 할 것인지 확인 >> {}", mealId);

            // Service 로직
            foodService.addMealFood(mealId, myFoodIdList);


        }else {
            log.info("myFood를 Meal에 추가하는데 선택(체크)한 데이터가 없음");
        }

        return "redirect:/food/myFood";
    }

}
