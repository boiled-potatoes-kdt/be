package com.dain_review.global.api;


import com.dain_review.global.exception.GlobalException;
import com.dain_review.global.exception.GlobalResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Data
public class API {

    public static ResponseEntity OK(Object data) {
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity OK() {
        return ResponseEntity.ok(new GlobalResponse("성공", HttpStatus.OK));
    }

    public static ResponseEntity ERROR(GlobalException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getGlobalResponse());
    }

    public static ResponseEntity ERROR(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(400).body(new GlobalResponse(
                "잘못된 인자값 입니다.",
                HttpStatus.valueOf(400)
        ));
    }

}
