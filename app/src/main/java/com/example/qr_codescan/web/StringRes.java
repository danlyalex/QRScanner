package com.example.qr_codescan.web;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by py on 2016/8/11.
 */
@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
public @interface StringRes {
}