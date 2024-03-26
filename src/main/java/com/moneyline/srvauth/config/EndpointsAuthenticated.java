package com.moneyline.srvauth.config;

public enum EndpointsAuthenticated {

//    ENDPOINT_REGISTER_USER("/auth/security/subscribe"),
//    ENDPOINT_ACTIVATE_ACCOUNT("/auth/security/verify/code"),
//    ENDPOINT_SEND_CODE("/auth/security/verify/send"),
//    ENDPOINT_CHANGEPIN("/auth/security/change/pin"),
//    ENDPOINT_LOGIN_USER("/auth/signin"),
//    ENDPOINT_VALIDATE_TOKEN("/auth/validate"),
    //ENDPOINT_FETCH_COUNTRY("/fetch/country"),
    //ENDPOINT_FETCH_USER("/fetch/user"),
    ENDPOINT_UPDATE_USER("/security/update/account"),
    ENDPOINT_UPDATE_USER_INFORMATION("/security/get/account");

    private final String value;

    EndpointsAuthenticated(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
