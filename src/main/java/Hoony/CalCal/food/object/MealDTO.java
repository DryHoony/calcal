package Hoony.CalCal.food.object;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO extends Meal {

//    private int id;
//    private int memberId;
//
//    private String mealName;
//    private String information;

    private List<MyFood> myFoods;
    // myFoodPage 에서 각각의 Meal 에 해당하는 myFood 정보를 출력

    // 식단의 총 영양소를 나타내는 필드 추가
    private MyFood nutrientSum;

//    private String test;


}
