package net.noboard.fastconverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanxm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    /**
     * 域名,如果此处使用默认的空字符串，则输出数据将使用该域
     * 定义在程序中的域名为key输出，否则使用此处定义的字符串作为key
     * 输出。
     *
     * 例如：
     * @DocField(name = firstName)
     * String name = "wanxm";     // 输出到前端时，name将会被替换成firstName，及 "firstName":"wanxm"
     * @return
     */
    String name() default "";

    /**
     * 是否隐藏，默认为false，表示不隐藏，如果改为true，则被注解域不会出现在输出数据中
     * @return
     */
    boolean isHide() default false;
}
