package com.crm.task1.validation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentType {
    String message() default "Invalid payment type. Allowed values are CASH, CARD, or TRANSFER.";
}