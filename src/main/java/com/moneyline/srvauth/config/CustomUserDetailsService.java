package com.moneyline.srvauth.config;

import com.moneyline.srvauth.config.internationalization.Translate;
import com.moneyline.srvauth.entity.UserCredential;
import com.moneyline.srvauth.repository.UserCredentialRepository;
import com.moneyline.srvauth.service.Message;
import com.moneyline.srvauth.utils.FnUtilities;
import com.moneyline.srvauth.utils.Messagerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService extends Message implements UserDetailsService {

    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<UserCredential> credential = userCredentialRepository.findByUsername(username);

        if(!credential.isEmpty() && !credential.get().isEnabled()){
            String msg = Translate.get(Messagerie.AUTH_ACCOUNT_NEED_ACTIVATION, FnUtilities.getlang(""),this.getMessageSource());
            this.setCode(Messagerie.AUTH_ACCOUNT_NEED_ACTIVATION.getCode());
            this.setInternalcode(Messagerie.AUTH_ACCOUNT_NEED_ACTIVATION  .getInternalcode());
            throw  new UsernameNotFoundException(msg);
        }
        if(!credential.isEmpty() && credential.get().isUserconnected()){
           //ajouter la limitation de connexion et possiblement deconnected le compte
        }
        return credential.map(CustomUserDetails::new).orElseThrow(() -> {
            String msg = Translate.get(Messagerie.AUTH_USER_NOTFOUND, FnUtilities.getlang(""),this.getMessageSource());
            this.setCode(Messagerie.AUTH_USER_NOTFOUND.getCode());
            this.setInternalcode(Messagerie.AUTH_USER_NOTFOUND  .getInternalcode());
            return new UsernameNotFoundException(msg);
        });
    }

}
