package com.moneyline.srvauth.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private String key;
    private String value;
}
