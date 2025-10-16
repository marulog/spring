package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC- DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null; // h2와 실제 연결할 객체
        PreparedStatement pstmt = null; // sql문을 실행할 객체


        try {
            con = getConnection(); // h2에 대한 커넥션 획득
            pstmt = con.prepareStatement(sql); // sql 문장을 미리 컴파일함
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // 실제 쿼리가 DB에 적용됨
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
//            pstmt.executeUpdate(); 데이터를 변경할 때
            rs = pstmt.executeQuery(); // select문 쓸 때

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;

            } else{
                throw new NoSuchElementException("member not found memberId="+memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }



    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection(); // h2에 대한 커넥션 획득
            pstmt = con.prepareStatement(sql); // sql 문장을 미리 컴파일함
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();// 실제 쿼리가 DB에 적용됨
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection(); // h2에 대한 커넥션 획득
            pstmt = con.prepareStatement(sql); // sql 문장을 미리 컴파일함
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();// 실제 쿼리가 DB에 적용됨
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }



    private void close(Connection con, Statement stmt, ResultSet rs){

        if(rs != null){ // 결과
            try{
                rs.close();
            } catch (SQLException e){
                log.info("error", e);
            }
        }

       if(stmt != null){ // 명령 sql
           try {
               stmt.close();
           } catch (SQLException e) { // 예외가 발생해도 ㄱㅊ
               log.info("error", e);
           }
       }

       if(con != null){ //DB연결
           try{
               con.close();
           } catch (SQLException e){
                log.info("error", e);
           }
       }

    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
