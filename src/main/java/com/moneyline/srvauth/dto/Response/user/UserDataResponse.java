package com.moneyline.srvauth.dto.Response.user;

import com.moneyline.srvauth.dto.Response.DefaultResponse;
import lombok.Data;

@Data
public class UserDataResponse extends DefaultResponse {
    private String lastname;
    private String firstname;
    private String email;
    private String adressline;
    private String postalCode;
}
