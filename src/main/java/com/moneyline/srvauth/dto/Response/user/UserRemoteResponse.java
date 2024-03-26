package com.moneyline.srvauth.dto.Response.user;

import com.moneyline.srvauth.dto.Response.DefaultResponse;
import lombok.Data;

@Data
public class UserRemoteResponse extends DefaultResponse {
    private Integer id;
    private String username;
}
