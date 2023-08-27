package com.ewm.server.visit.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpValidator implements ConstraintValidator<Ip, String> {

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext constraintValidatorContext) {
        String ipAdressPattern =
                "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        Pattern pattern = Pattern.compile(ipAdressPattern);
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }
}