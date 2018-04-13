package ning.zhou.jdbc.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 实体映射表自定义注解
 * @data 2018年3月7日
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
	/**
	 * 表名称
	 * @return
	 */
	String name() default "";
}
