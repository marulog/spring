package hello.core.singleton;

public class StatefulService {

//    private  int price; //상태를 유지하는 필드를 없애야됨

    public int order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
//        this.price = pirce;
          return price;
    }

//    public int getPrice(){
//        return price;
//    }
}
