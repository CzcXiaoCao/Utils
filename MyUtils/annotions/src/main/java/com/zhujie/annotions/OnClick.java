package com.zhujie.annotions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by CaoZhiChao on 2018/12/4 14:38
 * 1. A primitive type ： 基本类型（java的八种基本类型：byte、short、int、long、float、double、char、boolean）

 2. String ： 字符串

 3. Class ：Class

 4. An enum type ： 枚举

 5. An annotation type ：注解

 6. An array type ：类型为以上任一类型的数组
 */
@Documented
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.CLASS)
public @interface OnClick {
    /** View IDs to which the method will be bound. */
    int[] value() default { 0 };
}
