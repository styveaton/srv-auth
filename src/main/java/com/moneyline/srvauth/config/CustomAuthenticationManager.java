package com.moneyline.srvauth.config;

import com.moneyline.srvauth.config.internationalization.Translate;
import com.moneyline.srvauth.service.Message;
import com.moneyline.srvauth.utils.FnUtilities;
import com.moneyline.srvauth.utils.Messagerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager extends Message implements AuthenticationManager {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)  {
        try {
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());

            if(!passwordEncoder.matches(authentication.getCredentials().toString(),userDetails.getPassword())){
                String msg = Translate.get(Messagerie.AUTH_BAD_PASSWORD, FnUtilities.getlang(""),this.getMessageSource());
                this.setCode(Messagerie.AUTH_BAD_PASSWORD.getCode());
                this.setInternalcode(Messagerie.AUTH_BAD_PASSWORD.getInternalcode());
                throw new BadCredentialsException(msg);
            }
            UsernamePasswordAuthenticationToken  usernamePasswordAuthenticationToken =  new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),userDetails.getPassword(),null);

            return usernamePasswordAuthenticationToken;

        }catch (UsernameNotFoundException e){
            this.setCode(customUserDetailsService.getCode());
            this.setInternalcode(customUserDetailsService.getInternalcode());
            this.setMessage(e.getMessage());

            return null;
        }catch (BadCredentialsException e){
            this.setMessage(e.getMessage());

            return null;
        }
    }
}
