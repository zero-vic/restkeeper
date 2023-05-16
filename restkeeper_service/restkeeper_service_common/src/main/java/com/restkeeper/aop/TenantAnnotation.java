package com.restkeeper.aop;

import java.lang.annotation.*;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/07
 * Description:
 * Version:V1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TenantAnnotation {
}
