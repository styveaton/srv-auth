package com.moneyline.srvauth.config.internationalization;

import com.moneyline.srvauth.utils.FnUtilities;
import com.moneyline.srvauth.utils.Messagerie;
import org.springframework.context.MessageSource;
import java.util.Locale;

public class Translate {
    public static String get(Messagerie msgCode, String lang, MessageSource messageSource) {
        try {
            Locale locale = new Locale(FnUtilities.getlang(lang));
            return messageSource.getMessage(msgCode.getKEY(), new Object[]{msgCode.getKEY()},msgCode.getMdefault(), locale);
        } catch (Exception e) {
            return msgCode.getMdefault();
        }
    }
}
