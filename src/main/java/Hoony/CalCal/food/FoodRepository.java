package Hoony.CalCal.food;

import Hoony.CalCal.food.object.Food;
import Hoony.CalCal.food.object.Meal;
import Hoony.CalCal.food.object.MealFood;
import Hoony.CalCal.food.object.MyFood;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class FoodRepository {

    private final DataSource dataSource;
    public FoodRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Food 메소드 - DB updateQuery X - 조회만 가능

    public Food findFoodById(String foodId) {
        String sql = "select * from food where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, foodId);
            rs = pstmt.executeQuery();

            Food food = new Food();
//            foodMapping(food, rs); // 데이터 매핑
            while(rs.next()){
                foodMapping(food, rs);
                log.info("데이터 매핑 시도, 확인 >> {}", food.toString());
            }

            return food;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }
    public List<Food> findFoodByEqualName(String searchName){
//        String sql = "select * from food where foodName=?";
        String sql = "select * from food where foodName=? " +
                "union select * from food where foodName like ? and foodName like '생것%' " +
                "union select * from food where foodName like ? and foodName like '%생것%'";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchName);
            pstmt.setString(2, searchName + "%");
            pstmt.setString(3, "%" + searchName + "%");
            rs = pstmt.executeQuery();

            List<Food> foods = new ArrayList<>();
            Food food; // 객체생성

            while (rs.next()){
                food = new Food(); // 초기화
                // 변수 26개,,매핑하는 방법 추후 upgrade - 1. ORM 라이브러리(Hibernate) 2. ModelMapper 3. 빌더 패턴
                foodMapping(food, rs);
                foods.add(food);
            }
            return foods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }
    public List<Food> findFoodByStartName(String searchName){
        String sql = "select * from food where foodName like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchName + "%");
            rs = pstmt.executeQuery();

            List<Food> foods = new ArrayList<>();
            Food food; // 객체생성

            while (rs.next()){
                food = new Food(); // 초기화
                // 변수 26개,,매핑하는 방법 추후 upgrade - 1. ORM 라이브러리(Hibernate) 2. ModelMapper 3. 빌더 패턴
                foodMapping(food, rs);
                foods.add(food);
            }
            return foods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }
    public List<Food> findFoodByEndName(String searchName){
        String sql = "select * from food where foodName like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchName);
            rs = pstmt.executeQuery();

            List<Food> foods = new ArrayList<>();
            Food food; // 객체생성

            while (rs.next()){
                food = new Food(); // 초기화
                // 변수 26개,,매핑하는 방법 추후 upgrade - 1. ORM 라이브러리(Hibernate) 2. ModelMapper 3. 빌더 패턴
                foodMapping(food, rs);
                foods.add(food);
            }
            return foods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }
    public List<Food> findFoodByBothEndName(String searchName){
        String sql = "select * from food where foodName like ? or foodName like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchName + "%");
            pstmt.setString(2, "%" + searchName);
            rs = pstmt.executeQuery();

            List<Food> foods = new ArrayList<>();
            Food food; // 객체생성

            while (rs.next()){
                food = new Food(); // 초기화
                // 변수 26개,,매핑하는 방법 추후 upgrade - 1. ORM 라이브러리(Hibernate) 2. ModelMapper 3. 빌더 패턴
                foodMapping(food, rs);
                foods.add(food);
            }
            return foods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }
    public List<Food> findFoodByIncludingName(String searchName){
        String sql = "select * from food where foodName like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchName + "%");
            rs = pstmt.executeQuery();

            List<Food> foods = new ArrayList<>();
            Food food; // 객체생성

            while (rs.next()){
                food = new Food(); // 초기화
                // 변수 26개,,매핑하는 방법 추후 upgrade - 1. ORM 라이브러리(Hibernate) 2. ModelMapper 3. 빌더 패턴
                foodMapping(food, rs);
                foods.add(food);
            }
            return foods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }

    private void foodMapping(Food food, ResultSet rs) throws SQLException {
        // 26개 변수 각각 매핑
        food.setId(rs.getString("id"));
        food.setFoodName(rs.getString("foodName"));

        food.setCarbohydrate(rs.getString("carbohydrate"));
        food.setProtein(rs.getString("protein"));
        food.setFat(rs.getString("fat"));
        food.setEnergy(rs.getString("energy"));
        food.setWater(rs.getString("water"));
        food.setServingSize(rs.getString("servingSize"));

        food.setCapacity(rs.getString("capacity"));
        food.setSugars(rs.getString("sugars"));
        food.setCholesterol(rs.getString("cholesterol"));
        food.setTransFattyAcid(rs.getString("transFattyAcid"));
        food.setDietaryFibre(rs.getString("dietaryFibre"));

        food.setCalcium(rs.getString("calcium"));
        food.setMagnesium(rs.getString("magnesium"));
        food.setPhosphorus(rs.getString("phosphorus"));
        food.setKalium(rs.getString("kalium"));
        food.setNatrium(rs.getString("natrium"));
        food.setCopper(rs.getString("copper"));
        food.setIodine(rs.getString("iodine"));
        food.setFerrum(rs.getString("ferrum"));
        food.setMangan(rs.getString("mangan"));
        food.setMolybdenum(rs.getString("molybdenum"));
        food.setSelenium(rs.getString("selenium"));
        food.setZinc(rs.getString("zinc"));

    }

    ////////////////////////////////////////////////////////////////////////////////////
    // MyFood 메소드

    public void saveMyFood(MyFood myFood) {
        String sql = "insert into myFood (memberId, foodName, carbohydrate, protein, fat, energy, water, servingSize, information) " +
                "values (?,?,?,?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, myFood.getMemberId());
            pstmt.setString(2, myFood.getFoodName());


//            pstmt.setFloat(3, myFood.getCarbohydrate());
//            pstmt.setFloat(4, myFood.getProtein());
//            pstmt.setFloat(5, myFood.getFat());
//            pstmt.setFloat(6, myFood.getEnergy());
////            pstmt.setFloat(7, myFood.getWater()); // NullPointException
//            // test
//            if (myFood.getWater() != null){
//                pstmt.setFloat(7, myFood.getWater());
//            } else {
//                pstmt.setNull(7, Types.FLOAT);
//            }

            // carbohydrate
            if (myFood.getCarbohydrate() != null) {
                pstmt.setFloat(3, myFood.getCarbohydrate());
            } else {
                pstmt.setNull(3, Types.FLOAT);
            }

            // protein
            if (myFood.getProtein() != null) {
                pstmt.setFloat(4, myFood.getProtein());
            } else {
                pstmt.setNull(4, Types.FLOAT);
            }

            // fat
            if (myFood.getFat() != null) {
                pstmt.setFloat(5, myFood.getFat());
            } else {
                pstmt.setNull(5, Types.FLOAT);
            }

            // energy
            if (myFood.getEnergy() != null) {
                pstmt.setFloat(6, myFood.getEnergy());
            } else {
                pstmt.setNull(6, Types.FLOAT);
            }

            // water
            if (myFood.getWater() != null) {
                pstmt.setFloat(7, myFood.getWater());
            } else {
                pstmt.setNull(7, Types.FLOAT);
            }

            // servingSize
            if (myFood.getServingSize() != null) {
                pstmt.setFloat(8, myFood.getServingSize());
            } else {
                pstmt.setNull(8, Types.FLOAT);
            }

            pstmt.setString(9, myFood.getInformation()); // 얘는 왜 null 값이 그대로 들어가지? String 타입은 괜찮은가?
//            // information
//            if (myFood.getInformation() != null) {
//                pstmt.setString(9, myFood.getInformation());
//            } else {
//                pstmt.setNull(9, Types.VARCHAR);
//            }

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }
    public void createMyFood(int memberId) {
        String sql = "insert into myfood (memberId, foodName) values (?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, "수정 버튼을 클릭하세요");

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }
    }

    public MyFood findMyFoodByMyFoodId(int myFoodId) {
        String sql = "select * from myFood where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myFoodId);
            rs = pstmt.executeQuery();

            MyFood myFood = new MyFood();
            int memberId = 1;
            // 데이터 매핑
            while(rs.next()){ // 데이터가 하나뿐 이지만 myFoodMapping() 메소드를 재사용하기 위해 while 문 구성
                myFoodMapping(myFood, rs, memberId);
            }
//            log.info("myFood 가 잘 매핑되었나 확인 >> {}", myFood.toString());
            return myFood;


        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }
    public List<MyFood> findMyFoodByMemberId(int memberId) {
        String sql = "select * from myFood where memberId=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            List<MyFood> myFoods = new ArrayList<>();
            MyFood myFood;

//            log.info("myFood 데이터 매핑");
            while(rs.next()){
                myFood = new MyFood(); // 초기화

                // 데이터 매핑
                myFoodMapping(myFood, rs, memberId);

                myFoods.add(myFood);
            }

            return myFoods;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }
    public List<MyFood> findMyFoodByMealId(int mealId) {
        String sql = "select myfood.* from mealfood " +
                "left join myfood on mealfood.myFoodId = myfood.id " +
                "where mealfood.mealId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mealId);
            rs = pstmt.executeQuery();

            List<MyFood> myFoods = new ArrayList<>();
            MyFood myFood;

//            log.info("myFood 데이터 매핑");
            while(rs.next()){
                myFood = new MyFood(); // 초기화

                // 데이터 매핑
                myFoodMapping(myFood, rs, rs.getInt("memberId"));

                myFoods.add(myFood);
            }

            return myFoods;

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }

    public void editMyFood(MyFood myFood) {
        String sql = "update myfood set foodname=?, carbohydrate=?, protein=?, fat=?, energy=?, water=?, " +
                "servingSize=?, information=? where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, myFood.getFoodName());

            // Float 타입 NullPointException 처리
            if (myFood.getCarbohydrate() == null) pstmt.setNull(2, Types.FLOAT);
            else pstmt.setFloat(2, myFood.getCarbohydrate());
            if (myFood.getProtein() == null) pstmt.setNull(3, Types.FLOAT);
            else pstmt.setFloat(3, myFood.getProtein());
            if (myFood.getFat() == null) pstmt.setNull(4, Types.FLOAT);
            else pstmt.setFloat(4, myFood.getFat());
            if (myFood.getEnergy() == null) pstmt.setNull(5, Types.FLOAT);
            else pstmt.setFloat(5, myFood.getEnergy());
            if (myFood.getWater() == null) pstmt.setNull(6, Types.FLOAT);
            else pstmt.setFloat(6, myFood.getWater());
            if (myFood.getServingSize() == null) pstmt.setNull(7, Types.FLOAT);
            else pstmt.setFloat(7, myFood.getServingSize());

            pstmt.setString(8, myFood.getInformation());
            pstmt.setInt(9, myFood.getId());

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }
    public void deleteMyFood(int myFoodId) {
        String sql = "delete from myfood where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myFoodId);

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }
    public void myFoodMapping(MyFood myFood, ResultSet rs, int memberId) throws SQLException {
        myFood.setId(rs.getInt("id"));
        myFood.setMemberId(memberId);
        myFood.setFoodName(rs.getString("foodName"));

//                myFood.setCarbohydrate(rs.getFloat("carbohydrate"));
//                myFood.setProtein(rs.getFloat("protein"));
//                myFood.setFat(rs.getFloat("fat"));
//                myFood.setEnergy(rs.getFloat("energy"));
//                myFood.setWater(rs.getFloat("water"));
//                myFood.setServingSize(rs.getFloat("servingSize"));

        // null 값이 아니라 0.0 값이 할당된다 >> 수정
        // carbohydrate
        float carbohydrate = rs.getFloat("carbohydrate");
        if (!rs.wasNull()) {
            myFood.setCarbohydrate(carbohydrate);
        }

        // protein
        float protein = rs.getFloat("protein");
        if (!rs.wasNull()) {
            myFood.setProtein(protein);
        }

        // fat
        float fat = rs.getFloat("fat");
        if (!rs.wasNull()) {
            myFood.setFat(fat);
        }

        // energy
        float energy = rs.getFloat("energy");
        if (!rs.wasNull()) {
            myFood.setEnergy(energy);
        }

        // water
        float water = rs.getFloat("water");
        if (!rs.wasNull()) {
            myFood.setWater(water);
        }

        // servingSize
        float servingSize = rs.getFloat("servingSize");
        if (!rs.wasNull()) {
            myFood.setServingSize(servingSize);
        }

        myFood.setInformation(rs.getString("information"));
    }


    ////////////////////////////////////////////////////////////////////////////////////
    // Meal, MealFood 메소드

    public void createMeal(int memberId) {
        String mealName = "내 식단 이름";
        String information = "내 식단 설명";
        String sql = "insert into meal(memberId, mealName, information) values(?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, mealName);
            pstmt.setString(3, information);

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }
    }
    public void insertMealFood(int mealId, List<Integer> myFoodIdList) {
        //SQL을 myFoodIdList.size() 에 따라 동적으로 생성
        String sql0 = "insert into mealFood (mealId, myFoodId) values ";
        for (int i = 0; i < myFoodIdList.size(); i++) {
            // (mealId, myFoodId)
            sql0 += "(" + mealId + ",?)," ;
        }
        // sql 끝부분 , 자름
        String sql = sql0.replaceAll(",$", "");// $는 입력 문자열의 끝을 의미
        log.info("sql 문 동적으로 잘 작성되었는지 확인 >> {}", sql);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // sql 문 매핑
            int i = 1;
            for(int myFoodId : myFoodIdList){
                pstmt.setInt(i, myFoodId);
                i++;
            }

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }


    }
    public List<Meal> findMealByMemberId(int memberId) {
        String sql = "select * from meal where memberId=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            List<Meal> meals = new ArrayList<>();
            Meal meal;

            while(rs.next()){
                meal = new Meal();
                meal.setId(rs.getInt("id"));
                meal.setMemberId(memberId);
                // null 일 수 있는 필드가 String 타입이라 따로 처리안해도 괜찮을 듯
                meal.setMealName(rs.getString("mealName"));
                meal.setInformation(rs.getString("information"));

                meals.add(meal);
            }

            return meals;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }

    public List<MealFood> findMealFoodByMyFoodId(int myFoodId){
        String sql="select mealfood.* from myFood left join mealfood on myfood.id = mealfood.myFoodId where myFood.id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myFoodId);
            rs = pstmt.executeQuery();


            List<MealFood> mealFoods = new ArrayList<>();
            MealFood mealFood;

            while(rs.next()){
                mealFood = new MealFood();
                mealFood.setId(rs.getInt("id"));
                mealFood.setMealId(rs.getInt("mealId"));
                mealFood.setMyFoodId(myFoodId);

                mealFoods.add(mealFood);
            }

            return mealFoods;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, rs);
        }
    }

    public void editMeal(Meal meal) {
        String sql = "update meal set mealName=?, information=? where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, meal.getMealName());
            pstmt.setString(2, meal.getInformation());
            pstmt.setInt(3, meal.getId());

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }


    }
    public void truncateMeal(int mealId) {
        String sql = "delete from mealfood where mealId=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mealId);

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }
    public void deleteMeal(int mealId) {
        String sql = "delete from meal where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mealId);

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }


    public void myMealMapping(Meal meal, ResultSet rs, int memberId) throws SQLException {
        meal.setId(rs.getInt("id"));
        meal.setMemberId(memberId);

        // Null 처리해야 하는 필드가 String 타입이라 굳이 안해도 괜찮을듯
    }


    ////////////////////////////////////////////////////////////////////////////////////
    // Connection 메소드
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try{
            if (rs != null){
                rs.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        try{
            if(pstmt != null){
                pstmt.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        try{
            if (conn != null){
//                conn.close();
                close(conn);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }



}
