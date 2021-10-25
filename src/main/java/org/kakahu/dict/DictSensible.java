package org.kakahu.dict;

import java.lang.annotation.*;

/**
 * @Author: huangjun
 * @Date: 2021/8/27 14:42
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DictSensible {


    /**
     * 实体类字段
     */
    String codeValue();

    /**
     * 字典code,默认使用 value 转下划线形式
     */
    String codeFileName() default "";

    /**
     * 字典表的中文翻译字段名称
     */
    String chineseFieldName() default "";

    /**
     * 字典表名称
     */
    String tableName() default "";


    /**
     * 字典类型的值
     */
    String typeValue() default "";

    /**
     * 字典表中的字典类型字段名称
     */
    String typeFieldName() default "";

    /**
     * 翻译后放在实体类的字段，为空则放在代码字段
     */
    String toField() default "";

    /**
     * 状态的值
     */
    String statusValue() default "";

    /**
     * 状态字段的字段名
     */
    String statusFileName() default "";
}
