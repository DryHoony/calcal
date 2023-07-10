package Hoony.CalCal.food.object;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    private String id;
    private String foodName; // 식품명

    private String carbohydrate; // 탄수화문
    private String protein; // 단백질
    private String fat; // 지방
    private String energy; // 에너지(kcal)
    private String water; // 수분(g)
    private String servingSize; // 1회제공량

    private String capacity; // 총내용량(g)
    private String sugars; // 당류(g)
    private String cholesterol; // 콜레스테롤(mg)
    private String transFattyAcid; // 트랜스지방산(g)
    private String dietaryFibre; // 식이섬유(g)

    private String calcium; // 칼슘(mg)
    private String magnesium; // 마그네슘(mg)
    private String phosphorus; // 인(mg)
    private String kalium; // 칼륨(mg)
    private String natrium; // 나트륨(mg)
    private String copper; // 구리(mg)
    private String iodine; // 요오드(ug)
    private String ferrum; // 철(mg)
    private String mangan; // 망간(mg)
    private String molybdenum; // 몰리브덴(ug)
    private String selenium; // 셀레늄(ug)
    private String zinc; // 아연(mg)


}
