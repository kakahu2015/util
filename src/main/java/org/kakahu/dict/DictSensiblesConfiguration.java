package org.kakahu.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huangjun
 * @Date: 2021/9/26 14:12
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass
public class DictSensiblesConfiguration {
    @Autowired
    private ApplicationContext context;

    @Bean
    @ConditionalOnMissingBean
    public DictSensiblesBean createDict(){
        return DictSensiblesBean.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public DictTypeSensibleAspect createDictTypeSensibleAspect(){
        return new DictTypeSensibleAspect(context);
    }


}
