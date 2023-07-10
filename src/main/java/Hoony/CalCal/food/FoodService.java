package Hoony.CalCal.food;

import Hoony.CalCal.food.object.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    // Food 메소드
    private Food findFoodById(String foodId) {
        Food food = foodRepository.findFoodById(foodId);
        return food;
    }
    public List<Food> findFoodByEqualName(String searchName){ // 검색1
        List<Food> foods = foodRepository.findFoodByEqualName(searchName);
        log.info("검색1 결과 {}개", foods.size());
        return foods;
    }
    public List<Food> findFoodByStartName(String searchName){
        List<Food> foods = foodRepository.findFoodByStartName(searchName);
        return foods;
    }
    public List<Food> findFoodByEndName(String searchName){
        List<Food> foods = foodRepository.findFoodByEndName(searchName);
        return foods;
    }
    public List<Food> findFoodByBothEndName(String searchName){
        List<Food> foods = foodRepository.findFoodByBothEndName(searchName);
        log.info("검색2 결과 {}개", foods.size());
        return foods;
    }
    public List<Food> findFoodByIncludingName(String searchName){
        List<Food> foods = foodRepository.findFoodByIncludingName(searchName);
        log.info("검색3 결과 {}개", foods.size());
        return foods;
    }




    ////////////////////////////////////////////////////////////////////////////////////
    // MyFood 메소드

    public void myFoodAdd(int memberId, String foodId) { // 검색해서 등록
        // foodId 에 해당하는 food 찾기
        Food food = findFoodById(foodId);
        log.info("foodId 에 해당하는 food 찾기 결과 >> {}", food.toString());

        // myFood 객체에 데이터 매핑 (Float 타입변환)
        MyFood myFood = mappingFoodToMyFood(food, memberId);
        log.info("myFood 객체에 데이터 매핑 결과 >> {}", myFood.toString());

        // repository 에 저장
        foodRepository.saveMyFood(myFood);
    }
    public void myFoodAdd(MyFood myFood){ // myFoodAdd 오버로딩 - 직접 입력, 등록
        // repository 에 저장
    }
    public void myFoodCreate(int memberId) {
        foodRepository.createMyFood(memberId);
    }

    public MyFood findMyFoodByMyFoodId(int myFoodId){
        MyFood myFood = foodRepository.findMyFoodByMyFoodId(myFoodId);
        return myFood;
    }
    public List<MyFood> findMyFoodByMemberId(int memberId) {
        List<MyFood> myFoods = foodRepository.findMyFoodByMemberId(memberId);
        return myFoods;
    }

    public void editMyFood(MyFood myFood) {
        foodRepository.editMyFood(myFood);
    }
    public void deleteMyFood(int myFoodId) {
        foodRepository.deleteMyFood(myFoodId);
    }

    private MyFood mappingFoodToMyFood(Food food, int memberId) {
        MyFood myFood = new MyFood();
        // id는 auto_increment
        myFood.setMemberId(memberId);
        myFood.setFoodName(food.getFoodName());

        // null 이 아니고 숫자 일때만 추가
        // 현 데이터는 숫자가 아니면 '-'로 저장되어 있음
        if (food.getCarbohydrate() != null && !food.getCarbohydrate().equals("-")) {
            myFood.setCarbohydrate(Float.parseFloat(food.getCarbohydrate()));
        }

        if (food.getProtein() != null && !food.getProtein().equals("-")) {
            myFood.setProtein(Float.parseFloat(food.getProtein()));
        }

        if (food.getFat() != null && !food.getFat().equals("-")) {
            myFood.setFat(Float.parseFloat(food.getFat()));
        }

        if (food.getEnergy() != null && !food.getEnergy().equals("-")) {
            myFood.setEnergy(Float.parseFloat(food.getEnergy()));
        }

        if (food.getWater() != null && !food.getWater().equals("-")) {
            myFood.setWater(Float.parseFloat(food.getWater()));
        }

        if (food.getServingSize() != null && !food.getServingSize().equals("-")) {
            myFood.setServingSize(Float.parseFloat(food.getServingSize()));
        }

        // information 은 기본값 null 로 등록

        return myFood;
    }



    ////////////////////////////////////////////////////////////////////////////////////
    // Meal, MealFood 메소드

    public void createMeal(int memberId) {
        foodRepository.createMeal(memberId);
    }
    public void addMealFood(int mealId, List<Integer> myFoodIdList) {
        foodRepository.insertMealFood(mealId, myFoodIdList);
    }

    public List<MealFood> findMealFoodByMyFoodId(int myFoodID){
        List<MealFood> mealFoods = foodRepository.findMealFoodByMyFoodId(myFoodID);
        return mealFoods;
    }

    public List<Meal> findMealByMemberId(int memberId) {
        List<Meal> meals = foodRepository.findMealByMemberId(memberId);
        return meals;
    }
    public void editMeal(Meal meal) {
        foodRepository.editMeal(meal);
    }
    public void deleteMeal(int mealId) {
        // meal에 포함되는(mealId 값을 참조하는) mealFood 를 모두 삭제
        foodRepository.truncateMeal(mealId);

        // 해당 meal 삭제
        foodRepository.deleteMeal(mealId);

    }



    public List<MealDTO> findMealDTOByMemberId(int memberId) {
        // memberId에 해당하는 Meal을 받아온다
        List<Meal> meals = findMealByMemberId(memberId);

        // 각각의 Meal에 해당하는 MealDTO 를 받아온다
        List<MealDTO> mealDTOList = new ArrayList<MealDTO>();
        MealDTO mealDTO;

        for(Meal meal : meals){
            mealDTO = new MealDTO(); // 초기화
            mealDTO.setId(meal.getId());
            mealDTO.setMemberId(meal.getMemberId());
            mealDTO.setMealName(meal.getMealName());
            mealDTO.setInformation(meal.getInformation());

            // List<MyFood> 데이터 조회 - 할당
            mealDTO.setMyFoods(foodRepository.findMyFoodByMealId(meal.getId())); // meal 에 포함되는 myFood 를 저장

            mealDTOList.add(mealDTO);
        }

        // 각각의 mealDTO 마다 영양소 합계 데이터 추가
        MyFood nutrientSum;
        for(MealDTO mealDTO0 : mealDTOList){
            nutrientSum = calculateNutrient(mealDTO0.getMyFoods());
            mealDTO0.setNutrientSum(nutrientSum);
        }

        return mealDTOList; // Controller 에서 log 찍음
    }
    public MyFood calculateNutrient(List<MyFood> myFoods) {
        MyFood nutrientSum = new MyFood();

        nutrientSum.setFoodName("영양소 합계");
        nutrientSum.setInformation("내 식단의 영양소 총 합계 입니다.");

        Float carbohydrate = 0f; // 탄수화물
        Float protein = 0f; // 단백질
        Float fat = 0f; // 지방
        Float energy = 0f; // 에너지(kcal)
        Float water = 0f; // 수분(g)

        for (MyFood myFood : myFoods) {
            if (myFood.getCarbohydrate() != null)
                carbohydrate += myFood.getCarbohydrate();

            if (myFood.getProtein() != null)
                protein += myFood.getProtein();

            if (myFood.getFat() != null)
                fat += myFood.getFat();

            if (myFood.getEnergy() != null)
                energy += myFood.getEnergy();

            if (myFood.getWater() != null)
                water += myFood.getWater();
        }

        nutrientSum.setCarbohydrate(carbohydrate);
        nutrientSum.setProtein(protein);
        nutrientSum.setFat(fat);
        nutrientSum.setEnergy(energy);
        nutrientSum.setWater(water);


        return nutrientSum;
    }


}
