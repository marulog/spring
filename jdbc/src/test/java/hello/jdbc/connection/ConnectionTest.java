package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverManagerDataSource - 항상 새로운 커넥션 획득
        //Driver는 다른 애들과 달리 Datasource로 구현할 수 없어서
        //DriverManagerDataSource라는 것을 이용하여 구현함
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnnectionPoll() throws SQLException, InterruptedException {
        //커넥션 풀링 -> 커넥션풀의 대표적인 오픈소스 히카리
        //커넥션 풀링으로 미리 연결 시키거나, 요청시 마다 드라이버를 통해 커넥션 연결해야됨
        // DaataSource 커넥션을 제공하는 객체이고 표준 인터페이스임 -> 커넥션 풀을 구현할 때 지켜야 할 공통 규약
        // 그중에서 오픈소스로 유명한게 히카리임
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setMaximumPoolSize(10); // 디폴트가 10개임
        dataSource.setPoolName("MyPoll");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }



    //DataSOurce DB연결을 제공하는 객체임
    // 공통으로 사용하는 DataSource 이걸 통해서 커넥션을 획득함 ㅇㅇ
    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }
}
