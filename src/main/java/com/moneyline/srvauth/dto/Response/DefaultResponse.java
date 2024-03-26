package com.moneyline.srvauth.dto.Response;

import lombok.Data;

@Data
public class DefaultResponse {
    private boolean success = false;
    private String code;
    private String message;
}
