package com.moneyline.srvauth.dto.request;

import lombok.Data;

@Data
public class ChangePinRequest {
    private String username;
    private String scd;
    private String rscd;
    private String code;
    private String action;// ACTIVATE_ACCOUNT , FORGET_PWD refer to parameter class
}
