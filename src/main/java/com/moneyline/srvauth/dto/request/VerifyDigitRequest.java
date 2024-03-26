package com.moneyline.srvauth.dto.request;

import lombok.Data;

@Data
public class VerifyDigitRequest {
    private String username;
    private String code;
    private String action;// ACTIVATE_ACCOUNT , FORGET_PWD refer to parameter class
}
