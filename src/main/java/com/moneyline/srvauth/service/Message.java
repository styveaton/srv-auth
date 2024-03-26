package com.moneyline.srvauth.service;

import com.moneyline.srvauth.utils.FnUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
public class Message {

    @Autowired
    private MessageSource messageSource;
    private String message;
    private int code;
    private String internalcode;
    public static final Logger LOG = LogManager.getLogger();

    public String getMessage() {
        return FnUtilities.isNullOrEmpty(message) ? "Message Not defined" : message;
    }

    public void setMessage(String message) {
        LOG.info(message);
        this.message = message;
    }

    public Message getContext() {
        return this;
    }

    public int getCode() {
        return code;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getInternalcode() {
        return internalcode;
    }

    public void setInternalcode(String internalcode) {
        this.internalcode = internalcode;
    }
}
