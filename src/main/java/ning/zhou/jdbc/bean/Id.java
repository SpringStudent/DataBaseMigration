package ning.zhou.jdbc.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义注解主键
 * @data 2018年3月7日
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Id {
}
