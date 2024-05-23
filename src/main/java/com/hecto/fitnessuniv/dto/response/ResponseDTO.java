package com.hecto.fitnessuniv.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hecto.fitnessuniv.common.ResponseCode;
import com.hecto.fitnessuniv.common.ResponseMessage;

public class ResponseDTO {
    private String code;
    private String message;

    public ResponseDTO(String code, String message) {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDTO> databaseError() {
        ResponseDTO responseBody =
                new ResponseDTO(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> validationFail() {
        ResponseDTO responseBody =
                new ResponseDTO(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
    // SignInFail 넣어줘야 함.
}
