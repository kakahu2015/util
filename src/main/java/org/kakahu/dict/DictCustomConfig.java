package org.kakahu.dict;

import java.util.List;

/**
 * @Author: huangjun
 * @Date: 2021/8/27 16:45
 * @Version 1.0
 */
public interface DictCustomConfig {


    DictSensiblesBean initDictConfig(DictSensiblesBean dictSensiblesBean);

    /**
     *
     * @param dictSensiblesBean 字段对象
     * @return 返回对应的中文翻译
     */
    String getLableInChinese(DictSensiblesBean dictSensiblesBean);


    /**
     * 获取需要翻译的对象
     * @param obj   初始对象
     * @return  解析出需要翻译的对象
     */
    List<Object> getData(Object obj);

}
