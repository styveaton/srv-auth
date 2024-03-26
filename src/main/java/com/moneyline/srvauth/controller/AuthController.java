package com.moneyline.srvauth.controller;

import com.moneyline.srvauth.dto.Response.*;
import com.moneyline.srvauth.dto.Response.user.UserDataResponse;
import com.moneyline.srvauth.dto.Response.user.UserRemoteResponse;
import com.moneyline.srvauth.dto.request.*;
import com.moneyline.srvauth.dto.request.user.UserRequest;
import com.moneyline.srvauth.entity.UserCredential;
import com.moneyline.srvauth.service.AuthService;
import com.moneyline.srvauth.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_AUTH)
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping(Constants.POST_MAPPING_REGISTRATION_PATH)
    public String registerUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

    @PostMapping(Constants.POST_MAPPING_SUBSCRIBE_USER)
    public ResponseEntity<RegistrationResponse> UserSubscription(@RequestBody RegistrationRequest user) {

        RegistrationResponse response = service.UserSubscription(user);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_LOGIN_PATH)
    @RequestMapping(value = {Constants.POST_MAPPING_LOGIN_PATH}, method = RequestMethod.POST,produces ={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest user) {

        AuthResponse response = service.authenticate(user);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.GET_MAPPING_VALIDATE_TOKEN_PATH)
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest token) {
        ValidateTokenResponse response = service.validateToken(token);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_SEND_CODE)
    public ResponseEntity<SendDigitResponse> sendDigitToPhoneNUmber(@RequestBody SendDigitRequest request) {
        SendDigitResponse response = service.sendDigitToPhoneNUmber(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_VERIFY_CODE)
    public ResponseEntity<VerifyDigitResponse> verifyDigit(@RequestBody VerifyDigitRequest request) {
        VerifyDigitResponse response = service.verifyDigit(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_CHANGEPIN)
    public ResponseEntity<ChangePinResponse> changePin(@RequestBody ChangePinRequest request) {
        ChangePinResponse response = service.changePin(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_UPDATEACCOUNT)
    public ResponseEntity<UpdateUserResponse> updateAccount(@RequestBody UpdateUserRequest request) {
        UpdateUserResponse response = service.updateAccount(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(Constants.POST_MAPPING_FETCH_COUNTRY)
    public ResponseEntity<CountryResponse> getCountry(@RequestBody CountryRequest request) {
        CountryResponse response = service.getCountry(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }
    @PostMapping(Constants.POST_MAPPING_FETCH_USER)
    public ResponseEntity<UserRemoteResponse> getUser(@RequestBody UserRequest request) {
        UserRemoteResponse response = service.getUser(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }
    @PostMapping(Constants.POST_MAPPING_FETCH_USER_INFORMATION)
    public ResponseEntity<UserDataResponse> getUserInformation(@RequestBody UserRequest request) {
        UserDataResponse response = service.getUserInformation(request);

        HttpHeaders responseHeaders = new HttpHeaders();

        if(!response.isSuccess()){
            responseHeaders.set("internal-code",String.valueOf(service.getCode()));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
    }
}
