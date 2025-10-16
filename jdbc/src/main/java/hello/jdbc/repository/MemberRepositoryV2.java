package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC- 커넥션을 파라미러로 넘겨서 같은 세션 유지
 */
@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    // 커넥션 세션 유지를  위한 ver2
    public Member findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

//        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
//            con = getConnection();
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
            // 커넥션은 전달되어 유지해야되기 때문에 여기서 닫지 않음
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
//            JdbcUtils.closeConnection(con);
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


    // 커넥션 유지를 위한 ver2
    public void update(Connection con ,String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

//        Connection con = null;
        PreparedStatement pstmt = null;

        try {
//            con = getConnection(); // h2에 대한 커넥션 획득
            pstmt = con.prepareStatement(sql); // sql 문장을 미리 컴파일함
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();// 실제 쿼리가 DB에 적용됨
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
//            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
//            JdbcUtils.closeConnection(con);
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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;

    }
}
