package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 트랜잭션 - 트랜잭션 매너저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 */
@Slf4j
public class MemberRepositoryV3 {

    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource); // 트랜젝션 동기화
//        JdbcUtils.closeConnection(con); > jbdc 의존성 제거
    }

    // 저장소에서 DB에 접근하려면 트랜잭션 동기화 매니저가 가지고 있는 커넥션을 조회해야됨
    private Connection getConnection() throws SQLException {
        // 스프링에서 제공하는 DataSourceUtils를 사용해서 트랜젝션 동기화 사용ㅇ
        Connection con = DataSourceUtils.getConnection(dataSource); // 동기화된 커넥션 조회
//        Connection con = dataSource.getConnection();
        // dataSource 자체는 JDBC에 의존되어 있음
        log.info("get connection={}, class={}", con, con.getClass());
        return con;

    }
}
