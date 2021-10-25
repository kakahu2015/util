package org.kakahu.dict;

import org.springframework.stereotype.Component;

/**
 * @Author: huangjun
 * @Date: 2021/9/17 11:49
 * @Version 1.0
 */
@Component
public class DictSensiblesBean {

    private static DictSensiblesBean dictSensiblesBean ;

    /**
     * 实体类字段
     */
    String codeValue;

    /**
     * 字典code,默认使用 value 转下划线形式
     */
    String codeFileName;

    /**
     * 字典表的中文翻译字段名称
     */
    String chineseFieldName;

    /**
     * 字典表名称
     */
    String tableName;


    /**
     * 字典类型的值
     */
    String typeValue;

    /**
     * 字典表中的字典类型字段名称
     */
    String typeFieldName;

    /**
     * 翻译后放在实体类的字段，为空则放在代码字段
     */
    String toField;

    /**
     * 状态的值
     */
    String statusValue;

    /**
     * 状态的字段名称
     */
    String statusFileName;


    public static DictSensiblesBean getInstance(){
        if (dictSensiblesBean == null){
            return dictSensiblesBean = new DictSensiblesBean();
        }
        return dictSensiblesBean;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public String getCodeFileName() {
        return codeFileName;
    }

    public String getChineseFieldName() {
        return chineseFieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public String getTypeFieldName() {
        return typeFieldName;
    }

    public String getToField() {
        return toField;
    }

    public String getStatusValue(){
        return statusValue;
    }

    public String getStatusFileName(){
        return statusFileName;
    }

    public DictSensiblesBean statusValue(String statusValue) {
        this.statusValue = statusValue;
        return dictSensiblesBean;
    }

    public DictSensiblesBean statusFileName(String statusFileName) {
        this.statusFileName = statusFileName;
        return dictSensiblesBean;
    }

    public DictSensiblesBean codeValue(String codeValue) {
        this.codeValue = codeValue;
        return dictSensiblesBean;
    }

    public DictSensiblesBean typeValue(String typeValue) {
        this.typeValue = typeValue;
        return dictSensiblesBean;
    }

    public DictSensiblesBean chineseFieldName(String chineseFieldName) {
        this.chineseFieldName = chineseFieldName;
        return dictSensiblesBean;
    }

    public DictSensiblesBean tableName(String tableName) {
        this.tableName = tableName;
        return dictSensiblesBean;
    }

    public DictSensiblesBean typeFieldName(String typeFieldName) {
        this.typeFieldName = typeFieldName;
        return dictSensiblesBean;
    }

    public DictSensiblesBean toField(String toField){
        this.toField = toField;
        return dictSensiblesBean;
    }


    public DictSensiblesBean codeFileName(String codeFileName) {
        this.codeFileName = codeFileName;
        return dictSensiblesBean;
    }
}
