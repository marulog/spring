package hello.springtx.propagation;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Log {

    @Id
    @GeneratedValue
    private Long id;

    private String message;

    public Log() { // JPA는 기본 생성자가 항상 있어야됨
    }

    public Log(String message) {
        this.message = message;
    }
}
