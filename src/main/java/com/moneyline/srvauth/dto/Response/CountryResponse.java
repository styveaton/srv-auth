package com.moneyline.srvauth.dto.Response;

import lombok.Data;

@Data
public class CountryResponse extends DefaultResponse{
    private Integer id;
    private String code;
    private String name;
    private String phoneregex;
}
