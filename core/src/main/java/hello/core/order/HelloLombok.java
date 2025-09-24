package hello.core.order;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private  int age;

    public static void main(String[] args) {
        HelloLombok a = new HelloLombok();
        a.setName("asdas");

        String name = a.getName();
        System.out.println("name = " + name);
    }

}
