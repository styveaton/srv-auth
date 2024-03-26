package com.moneyline.srvauth.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse extends DefaultResponse {

    private String token;
    private String phone;
    private String firstname;
    private String lastname;
}
