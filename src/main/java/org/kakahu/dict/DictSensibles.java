package org.kakahu.dict;

import java.lang.annotation.*;

/**
 * @Author: huangjun
 * @Date: 2021/9/26 15:55
 * @Version 1.0
 * 多个字典
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DictSensibles {
    DictSensible[] value();
}
