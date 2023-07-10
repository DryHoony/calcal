package Hoony.CalCal.food.object;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Meal {

    private int id;
    private int memberId;

    private String mealName; // 식단명
    private String information; // 내용

}
