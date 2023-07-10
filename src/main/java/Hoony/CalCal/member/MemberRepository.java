package Hoony.CalCal.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MemberRepository {
    // JDBC

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Member member) {
        String sql = "insert into member(name, password) values (?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPassword());

            pstmt.executeUpdate();


        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, null);
        }

    }


    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()){
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setName(rs.getString("name"));
                member.setPassword(rs.getString("password"));
                members.add(member);
            }
            return members;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }

    }

    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setName(rs.getString("name"));
                member.setPassword(rs.getString("password"));
                return Optional.of(member);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }


    }

    public Optional<Member> findById(int id){
        String sql = "select * from member where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(id);
                member.setName(rs.getString("name"));
                member.setPassword(rs.getString("password"));
                return Optional.of(member);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public void update(Member member) {

        String sql = "update member set password=? where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
//        ResultSet rs = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getPassword());
            pstmt.setInt(2, member.getId());

            pstmt.executeUpdate();


        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, null);
        }
    }


    public void delete(Member member) {
        String sql = "delete from member where id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, member.getId());
            pstmt.executeUpdate();

        } catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            close(conn, pstmt, null);
        }

    }








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
