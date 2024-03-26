package com.moneyline.srvauth.service;

import com.moneyline.srvauth.config.CustomAuthenticationManager;
import com.moneyline.srvauth.config.internationalization.Translate;
import com.moneyline.srvauth.dto.Response.*;
import com.moneyline.srvauth.dto.Response.user.UserDataResponse;
import com.moneyline.srvauth.dto.Response.user.UserRemoteResponse;
import com.moneyline.srvauth.dto.request.*;
import com.moneyline.srvauth.dto.request.user.UserRequest;
import com.moneyline.srvauth.entity.Country;
import com.moneyline.srvauth.entity.RaisonCompte;
import com.moneyline.srvauth.entity.UserCredential;
import com.moneyline.srvauth.repository.CountryRepository;
import com.moneyline.srvauth.repository.RaisonCompteRepository;
import com.moneyline.srvauth.repository.UserCredentialRepository;
import com.moneyline.srvauth.utils.FnUtilities;
import com.moneyline.srvauth.utils.Messagerie;
import com.moneyline.srvauth.utils.Parameter;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService extends Message {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    RaisonCompteRepository raisonCompteRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomAuthenticationManager authenticationManager;

    public String saveUser(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        repository.save(userCredential);
        return "User Register succes";
    }

    public AuthResponse authenticate(AuthRequest user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        AuthResponse response = new AuthResponse();
        if (authentication != null && authentication.isAuthenticated()) {

            Optional<UserCredential> credential = repository.findByUsername(user.getUsername());

            if (credential.isEmpty()) {
                String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource());
                this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
                this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
                this.setMessage(msg);

                response.setCode(this.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }

            UserCredential userTable = credential.get();

            String token = generateToken(user.getUsername());

            userTable.setDateLastConnection(new Date());
            userTable.setUserconnected(Boolean.TRUE);
            userTable.setToken(token);

            repository.save(userTable);


            response.setToken(token);
            response.setSuccess(Boolean.TRUE);
            response.setPhone(user.getUsername());
            response.setFirstname(userTable.getFirstname());
            response.setLastname(userTable.getLastname());

            response.setCode(Messagerie.AUTH_AUTHENTICATE_DONE.getInternalcode());
            this.setMessage(Translate.get(Messagerie.AUTH_AUTHENTICATE_DONE, FnUtilities.getlang(""), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_AUTHENTICATE_DONE.getCode());
            response.setMessage(this.getMessage());
        } else {

            this.setMessage(authenticationManager.getMessage());
            this.setCode(authenticationManager.getCode());
            response.setCode(authenticationManager.getInternalcode());
            response.setMessage(this.getMessage());
        }
        return response;
    }

    public SendDigitResponse sendDigitToPhoneNUmber(SendDigitRequest request) {
        SendDigitResponse response = new SendDigitResponse();
        Optional<UserCredential> credential = repository.findByUsername(request.getUsername());

        if (credential.isEmpty()) {
            String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        String digitToSend = FnUtilities.random6digits();
        digitToSend = "13790";
        LOG.info("Digits : " + digitToSend);

        UserCredential user = credential.get();

        user.setVerificationCode(passwordEncoder.encode(digitToSend));
        user.setDateCodeGenerated(new Date());

        repository.save(user);

        //Appel de la fonction envoi de sms ou de mail ici

        String msg = Translate.get(Messagerie.AUTH_SENDCODETOPHONE_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_SENDCODETOPHONE_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_SENDCODETOPHONE_DONE.getInternalcode());
        this.setMessage(msg);
        response.setSuccess(Boolean.TRUE);
        response.setCode(this.getInternalcode());
        response.setMessage(this.getMessage());
        return response;
    }

    public RegistrationResponse UserSubscription(RegistrationRequest registrationRequest) {

        RegistrationResponse response = new RegistrationResponse();

        if (FnUtilities.isNullOrEmpty(registrationRequest.getSname())) {

            this.setMessage(Translate.get(Messagerie.AUTH_FIRST_NAME_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_FIRST_NAME_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_FIRST_NAME_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (FnUtilities.isNullOrEmpty(registrationRequest.getName())) {

            this.setMessage(Translate.get(Messagerie.AUTH_LAST_NAME_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_LAST_NAME_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_LAST_NAME_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (FnUtilities.isNullOrEmpty(registrationRequest.getEmail())) {

            this.setMessage(Translate.get(Messagerie.AUTH_MAIL_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_MAIL_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_MAIL_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (FnUtilities.isNullOrEmpty(registrationRequest.getInd())) {

            this.setMessage(Translate.get(Messagerie.AUTH_COUNTRY_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_COUNTRY_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_COUNTRY_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        Optional<Country> optionalCountry = countryRepository.findById(registrationRequest.getInd());

        if (optionalCountry.isEmpty()) {

            this.setMessage(Translate.get(Messagerie.AUTH_COUNTRY_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_COUNTRY_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_COUNTRY_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        if (FnUtilities.isNullOrEmpty(registrationRequest.getPhn())) {

            this.setMessage(Translate.get(Messagerie.AUTH_PHONE_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_PHONE_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_PHONE_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        if (FnUtilities.isNullOrEmpty(registrationRequest.getAdress())) {

            this.setMessage(Translate.get(Messagerie.AUTH_ADRESS_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_ADRESS_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_ADRESS_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        if (FnUtilities.isNullOrEmpty(registrationRequest.getCp())) {

            this.setMessage(Translate.get(Messagerie.AUTH_POSTALCODE_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_POSTALCODE_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_POSTALCODE_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        if (FnUtilities.isNullOrEmpty(registrationRequest.getRsn())) {

            this.setMessage(Translate.get(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        Optional<RaisonCompte> optionalRaisonCompte = raisonCompteRepository.findByCode(registrationRequest.getRsn());

        if (optionalRaisonCompte.isEmpty()) {

            this.setMessage(Translate.get(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (FnUtilities.isNullOrEmpty(registrationRequest.getScd())) {

            this.setMessage(Translate.get(Messagerie.AUTH_CODESECRET_REQUIS, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_CODESECRET_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_CODESECRET_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (registrationRequest.getScd().length() != Parameter.SECRET_CODE_LENGTH
                && FnUtilities.isNumeric(registrationRequest.getScd())) {

            this.setMessage(Translate.get(Messagerie.AUTH_CODESECRET_INVALIDE, registrationRequest.getLng(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_CODESECRET_INVALIDE.getCode());
            response.setCode(Messagerie.AUTH_CODESECRET_INVALIDE.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        registrationRequest.setScd(passwordEncoder.encode(registrationRequest.getScd()));

        UserCredential userCredential = new UserCredential();
        userCredential.setPassword(registrationRequest.getScd());
        userCredential.setAdressline(registrationRequest.getAdress());
        userCredential.setCredentialsNonExpired(Boolean.FALSE);
        userCredential.setEmail(registrationRequest.getEmail());
        userCredential.setEnabled(Boolean.FALSE);
        userCredential.setAccountReason(registrationRequest.getRsn());
        userCredential.setFirstname(registrationRequest.getName());
        userCredential.setLastname(registrationRequest.getSname());
        userCredential.setDateCreated(new Date());
        userCredential.setCountryId(optionalCountry.get().getId());
        userCredential.setCountry(optionalCountry.get().getName());
        userCredential.setLangue(registrationRequest.getLng());
        userCredential.setPhone(registrationRequest.getPhn());
        userCredential.setPostalCode(registrationRequest.getCp());
        userCredential.setUsername(registrationRequest.getPhn());
        userCredential.setAccountNonExpired(Boolean.FALSE);
        userCredential.setAccountNonLocked(Boolean.FALSE);

        repository.save(userCredential);

        this.setMessage(Translate.get(Messagerie.AUTH_SUBSCRIBE_DONE, registrationRequest.getLng(), this.getMessageSource()));
        this.setCode(Messagerie.AUTH_SUBSCRIBE_DONE.getCode());

        response.setSuccess(Boolean.TRUE);
        response.setUsername(registrationRequest.getPhn());
        response.setCode(Messagerie.AUTH_SUBSCRIBE_DONE.getInternalcode());
        response.setMessage(this.getMessage());
        return response;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();
        try {
            jwtService.validateToken(request.getToken());
            this.setMessage(Translate.get(Messagerie.AUTH_TOKENVALIDATION_DONE, FnUtilities.getlang(""), this.getMessageSource()));
            this.setInternalcode(Messagerie.AUTH_TOKENVALIDATION_DONE.getInternalcode());
            this.setCode(Messagerie.AUTH_TOKENVALIDATION_DONE.getCode());

            response.setSuccess(Boolean.TRUE);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
        } catch (ExpiredJwtException e) {
            //LOG.info();

            this.setMessage(Translate.get(Messagerie.AUTH_TOKENVALIDATION_EXPIRED, FnUtilities.getlang(""), this.getMessageSource()));
            this.setInternalcode(Messagerie.AUTH_TOKENVALIDATION_EXPIRED.getInternalcode());
            this.setCode(Messagerie.AUTH_TOKENVALIDATION_EXPIRED.getCode());

            response.setSuccess(Boolean.FALSE);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());

            LOG.error(e);
        }
        return response;
    }

    public VerifyDigitResponse verifyDigit(VerifyDigitRequest request) {

        VerifyDigitResponse response = new VerifyDigitResponse();

        if (!Parameter.ACTIVATE_ACCOUNT.equals(request.getAction()) && !Parameter.FORGET_PWD.equals(request.getAction())) {
            String msg = Translate.get(Messagerie.AUTH_VERIFICATIONCODE_NOTALLOW, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_VERIFICATIONCODE_NOTALLOW.getCode());
            this.setInternalcode(Messagerie.AUTH_VERIFICATIONCODE_NOTALLOW.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        Optional<UserCredential> credential = repository.findByUsername(request.getUsername());

        if (credential.isEmpty()) {
            String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        UserCredential user = credential.get();


        if (!passwordEncoder.matches(request.getCode(), user.getVerificationCode())) {
            String msg = Translate.get(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH.getCode());
            this.setInternalcode(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        Date now = new Date();
        Date generatedTime = user.getDateCodeGenerated();

        if (generatedTime == null) {
            String msg = Translate.get(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED.getCode());
            this.setInternalcode(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (now.getTime() - generatedTime.getTime() > Parameter.TIMEOUT_CODE_DURATION) {
            String msg = Translate.get(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED.getCode());
            this.setInternalcode(Messagerie.AUTH_VERIFICATION_CODE_EXPIRED.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        if (request.getAction().equals(Parameter.ACTIVATE_ACCOUNT)) {
            user.setEnabled(Boolean.TRUE);
            user.setDateUpdated(new Date());
        }
        if (request.getAction().equals(Parameter.FORGET_PWD)) {
            //A determiner
        }


        repository.save(user);

        String msg = Translate.get(Messagerie.AUTH_VERIFICATION_CODE_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_VERIFICATION_CODE_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_VERIFICATION_CODE_DONE.getInternalcode());
        this.setMessage(msg);
        response.setSuccess(Boolean.TRUE);
        response.setCode(this.getInternalcode());
        response.setMessage(this.getMessage());
        return response;
    }

    public ChangePinResponse changePin(ChangePinRequest request) {

        ChangePinResponse response = new ChangePinResponse();

        if (!Parameter.ACTIVATE_ACCOUNT.equals(request.getAction()) && !Parameter.FORGET_PWD.equals(request.getAction())) {
            String msg = Translate.get(Messagerie.AUTH_CHANGEPIN_NOTALLOW, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_CHANGEPIN_NOTALLOW.getCode());
            this.setInternalcode(Messagerie.AUTH_CHANGEPIN_NOTALLOW.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        Optional<UserCredential> credential = repository.findByUsername(request.getUsername());

        if (credential.isEmpty()) {
            String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        UserCredential user = credential.get();

        if (FnUtilities.isNullOrEmpty(request.getScd())) {

            this.setMessage(Translate.get(Messagerie.AUTH_CODESECRET_REQUIS, user.getLangue(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_CODESECRET_REQUIS.getCode());
            response.setCode(Messagerie.AUTH_CODESECRET_REQUIS.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        if (request.getScd().length() != Parameter.SECRET_CODE_LENGTH
                || !FnUtilities.isNumeric(request.getScd())) {

            this.setMessage(Translate.get(Messagerie.AUTH_CODESECRET_INVALIDE, user.getLangue(), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_CODESECRET_INVALIDE.getCode());
            response.setCode(Messagerie.AUTH_CODESECRET_INVALIDE.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }


        if (request.getAction().equals(Parameter.ACTIVATE_ACCOUNT)) {
            if (!passwordEncoder.matches(request.getCode(), user.getVerificationCode())) {
                String msg = Translate.get(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH, FnUtilities.getlang(""), this.getMessageSource());
                this.setCode(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH.getCode());
                this.setInternalcode(Messagerie.AUTH_VERIFICATION_CODE_NOT_MATCH.getInternalcode());
                this.setMessage(msg);
                response.setCode(this.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
        }

        user.setPassword(passwordEncoder.encode(request.getScd()));
        user.setDateUpdated(new Date());

        user.setVerificationCode(null);
        user.setDateCodeGenerated(null);

        repository.save(user);


        String msg = Translate.get(Messagerie.AUTH_CHANGEPIN_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_CHANGEPIN_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_CHANGEPIN_DONE.getInternalcode());
        this.setMessage(msg);
        response.setSuccess(Boolean.TRUE);
        response.setCode(this.getInternalcode());
        response.setMessage(this.getMessage());
        return response;
    }

    public UpdateUserResponse updateAccount(UpdateUserRequest request) {

        UpdateUserResponse response = new UpdateUserResponse();

        Optional<UserCredential> credential = repository.findByUsername(request.getUsername());

        if (credential.isEmpty()) {
            String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource());
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            this.setMessage(msg);
            response.setCode(this.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        UserCredential user = credential.get();

        if (request.getKey().equals(Parameter.KEY_UPDATE_SNAME)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {
                this.setMessage(Translate.get(Messagerie.AUTH_FIRST_NAME_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_FIRST_NAME_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_FIRST_NAME_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            user.setFirstname(request.getValue());

        } else if (request.getKey().equals(Parameter.KEY_UPDATE_NAME)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {
                this.setMessage(Translate.get(Messagerie.AUTH_LAST_NAME_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_LAST_NAME_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_LAST_NAME_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            user.setLastname(request.getValue());

        } else if (request.getKey().equals(Parameter.KEY_UPDATE_MAIL)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {
                this.setMessage(Translate.get(Messagerie.AUTH_MAIL_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_MAIL_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_MAIL_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            user.setEmail(request.getValue());

        } else if (request.getKey().equals(Parameter.KEY_UPDATE_LANG)) {

        } else if (request.getKey().equals(Parameter.KEY_UPDATE_REASON)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {
                this.setMessage(Translate.get(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            Optional<RaisonCompte> optionalRaisonCompte = raisonCompteRepository.findByCode(request.getValue());

            if (optionalRaisonCompte.isEmpty()) {

                this.setMessage(Translate.get(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_RAISONCREATIONCOMPTE_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
        } else if (request.getKey().equals(Parameter.KEY_UPDATE_ADRESS)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {

                this.setMessage(Translate.get(Messagerie.AUTH_ADRESS_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_ADRESS_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_ADRESS_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            user.setEmail(request.getValue());

        } else if (request.getKey().equals(Parameter.KEY_UPDATE_POSTALCODE)) {

            if (FnUtilities.isNullOrEmpty(request.getValue())) {

                this.setMessage(Translate.get(Messagerie.AUTH_POSTALCODE_REQUIS, user.getLangue(), this.getMessageSource()));
                this.setCode(Messagerie.AUTH_POSTALCODE_REQUIS.getCode());
                response.setCode(Messagerie.AUTH_POSTALCODE_REQUIS.getInternalcode());
                response.setMessage(this.getMessage());
                return response;
            }
            user.setPostalCode(request.getValue());

        }

        repository.save(user);
        String msg = Translate.get(Messagerie.AUTH_UPDATEACCOUNT_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_UPDATEACCOUNT_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_UPDATEACCOUNT_DONE.getInternalcode());
        this.setMessage(msg);
        response.setSuccess(Boolean.TRUE);
        response.setCode(this.getInternalcode());
        response.setMessage(this.getMessage());
        return response;
    }

    public List<Country> getCountries(){
        return countryRepository.findAll();
    }

    public CountryResponse getCountry(CountryRequest request){

        CountryResponse response = new CountryResponse();

        Optional<Country> optionalCountry = countryRepository.findById(request.getId());

        if (optionalCountry.isEmpty()) {

            this.setMessage(Translate.get(Messagerie.AUTH_COUNTRY_INCONNU, FnUtilities.getlang(""), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_COUNTRY_INCONNU.getCode());
            response.setCode(Messagerie.AUTH_COUNTRY_INCONNU.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }
        response.setCode(optionalCountry.get().getCode());
        response.setName(optionalCountry.get().getName());
        response.setPhoneregex(optionalCountry.get().getPhoneregex());
        response.setId(optionalCountry.get().getId());

        String msg = Translate.get(Messagerie.AUTH_GET_COUNTRY_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_GET_COUNTRY_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_GET_COUNTRY_DONE.getInternalcode());
        this.setMessage(msg);

        response.setSuccess(Boolean.TRUE);
        response.setMessage(this.getMessage());
        return response;
    }

    public UserRemoteResponse getUser(UserRequest request) {

        UserRemoteResponse response = new UserRemoteResponse();

        Optional<UserCredential> optionalUserCredential = repository.findByUsername(request.getUsername());

        if (optionalUserCredential.isEmpty()) {

            this.setMessage(Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            response.setCode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        response.setUsername(optionalUserCredential.get().getUsername());
        response.setId(optionalUserCredential.get().getId());

        String msg = Translate.get(Messagerie.AUTH_GET_USER_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_GET_USER_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_GET_USER_DONE.getInternalcode());
        this.setMessage(msg);

        response.setSuccess(Boolean.TRUE);
        return response;
    }
    public UserDataResponse getUserInformation(UserRequest request) {

        UserDataResponse response = new UserDataResponse();

        Optional<UserCredential> optionalUserCredential = repository.findByUsername(request.getUsername());

        if (optionalUserCredential.isEmpty()) {

            this.setMessage(Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""), this.getMessageSource()));
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            response.setCode(Messagerie.AUTH_USER_NOTFOUND.getInternalcode());
            response.setMessage(this.getMessage());
            return response;
        }

        response.setLastname(optionalUserCredential.get().getLastname());
        response.setFirstname(optionalUserCredential.get().getFirstname());
        response.setEmail(optionalUserCredential.get().getEmail());
        response.setAdressline(optionalUserCredential.get().getAdressline());
        response.setPostalCode(optionalUserCredential.get().getPostalCode());

        String msg = Translate.get(Messagerie.AUTH_GET_USER_DONE, FnUtilities.getlang(""), this.getMessageSource());
        this.setCode(Messagerie.AUTH_GET_USER_DONE.getCode());
        this.setInternalcode(Messagerie.AUTH_GET_USER_DONE.getInternalcode());
        this.setMessage(msg);

        response.setSuccess(Boolean.TRUE);
        return response;
    }
}
