package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    // 해당 예외 컨트롤러마다 하나하나 만들기 어려움 -> Advice로 변경
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegerExHandler(IllegalArgumentException e){
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }
//    // 컨트롤러 내부에서 예외가 터지면 ExceptionResolver으로 감 -> 개가 이제
//    // 해당컨트롤러에 ExceptionHanderExceptionResolver가 있는지 찾음
//    // 지금 찾아서 거기 내부 코드를 실행시켜버림-> 정상 작동로직이라 -> 200 상태코드로 변경
//    // 상태 코드를 직접 변경하고 싶으면 ResponseStatus로 변경하면됨
//    /*
//    {
//    "data": "BAD",
//    "message": "잘못된 입력 값"
//    }
//     */
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandler(UserException e){
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//    // 최상위 에러임 -> 위에서 처리하지 못한 모든 에러를 여기서 처리함
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e){
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }


    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }

        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        } // UserEcetion 발생 -> 핸들러리졸버 호출?

        return new MemberDto(id, "hello" + id);
    }
    /* -> 이건 컨트롤러 에러 -> 서블릿까지 왔다가 다시 반환하는 형식임 -> 바꿀거임
    -> 어떻게? -> ExceptionHandler 애노테이션 사용
    {
    "timestamp": "2025-10-08T06:59:12.825+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "exception": "java.lang.RuntimeException",
    "path": "/api2/members/ex"
}
     */

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }


}
