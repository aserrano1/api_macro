package com.dcatech.labviewer.macro.util;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author LuiFer
 */
@Component
public class TranslateUtil implements Serializable {

    @Autowired
    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public TranslateUtil() {

    }

    public String localizeMessage(String localizedCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage(localizedCode, null, locale);
        return errorMessage;
    }
}
