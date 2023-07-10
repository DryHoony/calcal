package Hoony.CalCal.food.object;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyFood {

    private int id; // auto
    private int memberId; // foreign key
    private String foodName; // 식품명

    // 유효성 설정 필요 - 음수
    // 0 과 null 을 구분하기 위해 기본생성자가 아닌 Wrapper Class 이용, 초기값 null (기본 데이터 타입은 초기값 0)
    private Float carbohydrate; // 탄수화물
    private Float protein; // 단백질
    private Float fat; // 지방
    private Float energy; // 에너지(kcal)
    private Float water; // 수분(g)
    private Float servingSize; // 1회제공량
    private String information; // 추가정보

}
