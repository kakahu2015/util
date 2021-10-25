package org.kakahu.dict;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangjun
 * @Date: 2021/8/27 14:43
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
@AllArgsConstructor
@Component
public class DictTypeSensibleAspect implements ResponseBodyAdvice<Object>{

    @Autowired
    DictCustomConfig dictConfig;

    @Autowired
    private DictSensiblesBean dictSensiblesBean;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public DictTypeSensibleAspect(ApplicationContext context) {
        Map<String, DictCustomConfig> beansOfType = context.getBeansOfType(DictCustomConfig.class);
        if (beansOfType.size() > 0){
            this.dictConfig = beansOfType.get("dictConfig");
            if (this.dictSensiblesBean == null){
                this.dictSensiblesBean = dictSensiblesBean.getInstance();
            }
            dictConfig.initDictConfig(dictSensiblesBean);
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(DictSensible.class)
                || methodParameter.hasMethodAnnotation(DictSensibles.class);
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (obj == null) {
            return null;
        }
        // 获取方法上的注解参数
        DictSensible[] sensibles;
        if (methodParameter.hasMethodAnnotation(DictSensible.class)) {
            DictSensible sensible = methodParameter.getMethodAnnotation(DictSensible.class);
            sensibles = new DictSensible[]{sensible};
        } else {
            DictSensibles typeSensibles = methodParameter.getMethodAnnotation(DictSensibles.class);
            sensibles = typeSensibles == null ? new DictSensible[]{} : typeSensibles.value();
        }
        // 处理返回值
        // 处理返回类型
        if (obj instanceof R) {
            if (((R) obj).getData() instanceof List) {
                ((List) ((R) obj).getData()).forEach(e -> this.dist(e, sensibles));
            } else {
                this.dist(((R) obj).getData(), sensibles);
            }
        } else if (obj instanceof IPage) {
            ((IPage) obj).getRecords().forEach(e -> this.dist(e, sensibles));
        } else {
            dictConfig.getData(obj).forEach(e -> this.dist(e, sensibles));
        }
        return obj;
    }

    private void dist(Object obj, DictSensible[] sensibles) {
        Class<?> clazz = obj.getClass();
        for (DictSensible sensible : sensibles) {
            try {
                Field field = ReflectUtil.getField(clazz, sensible.codeValue());
                if (field == null) {
                    log.warn("字典 {} 进行忽略,不存在", sensible.codeValue());
                    continue;
                }
                //反射获取该字段的值
                Object val = ReflectUtil.getFieldValue(obj, field);
                if (val == null) {
                    continue;
                }

                String codeFile = StrUtil.isEmpty(sensible.codeFileName())
                        ? StrUtil.toUnderlineCase(sensible.codeValue()) : sensible.codeFileName();


                String label;
                //自定义翻译
                dictSensiblesBean = "".equals(sensible.chineseFieldName()) ? dictSensiblesBean : dictSensiblesBean.chineseFieldName(sensible.chineseFieldName());
                dictSensiblesBean = "".equals(sensible.codeFileName()) ? dictSensiblesBean : dictSensiblesBean.codeFileName(sensible.codeFileName());
                dictSensiblesBean = "".equals(sensible.codeValue()) ? dictSensiblesBean : dictSensiblesBean.codeValue(sensible.codeValue());
                dictSensiblesBean = "".equals(sensible.statusFileName()) ? dictSensiblesBean : dictSensiblesBean.statusFileName(sensible.statusFileName());
                dictSensiblesBean = "".equals(sensible.statusValue()) ? dictSensiblesBean : dictSensiblesBean.statusValue(sensible.statusValue());
                dictSensiblesBean = "".equals(sensible.tableName()) ? dictSensiblesBean : dictSensiblesBean.tableName(sensible.tableName());
                dictSensiblesBean = "".equals(sensible.toField()) ? dictSensiblesBean : dictSensiblesBean.toField(sensible.toField());
                dictSensiblesBean = "".equals(sensible.typeFieldName()) ? dictSensiblesBean : dictSensiblesBean.typeFieldName(sensible.typeFieldName());
                dictSensiblesBean = "".equals(sensible.typeValue()) ? dictSensiblesBean : dictSensiblesBean.typeValue(sensible.typeValue());
                label = dictConfig.getLableInChinese(dictSensiblesBean);

                if (label == null) {
                    //代翻译

                    String tableName = StrUtil.isEmpty(sensible.tableName())
                            ? dictSensiblesBean.getTableName() : sensible.tableName();

                    String chineseField = StrUtil.isEmpty(sensible.chineseFieldName())
                            ? dictSensiblesBean.getChineseFieldName() : sensible.chineseFieldName();

                    if (tableName == null || chineseField == null) {
                        throw new RuntimeException("没有指定表名或库表中对应的中文翻译字段");
                    }

                    String sql = "select " + chineseField + " from " + tableName + " where " + codeFile + " = ? ";
                    List<Object> param = new ArrayList<>();

                    param.add(val);

                    String typeValue = StrUtil.isEmpty(sensible.typeValue())
                            ? dictSensiblesBean.getTypeValue() : sensible.typeValue();
                    if (typeValue != null) {
                        String typeFile = StrUtil.isEmpty(sensible.typeFieldName())
                                ? dictSensiblesBean.getTypeFieldName() : sensible.typeFieldName();
                        if (typeFile == null) {
                            throw new RuntimeException("没有指定类型字段");
                        }
                        sql += " and " + typeFile + " = ?";
                        param.add(typeValue);
                    }

                    String statusValue = StrUtil.isEmpty(sensible.statusValue())
                            ? dictSensiblesBean.getStatusValue() : sensible.statusValue();
                    if (statusValue != null){
                        String statusFile = StrUtil.isEmpty(sensible.statusFileName())
                                ? dictSensiblesBean.getStatusFileName() : sensible.statusFileName();
                        if (statusFile == null) {
                            throw new RuntimeException("没有指定状态字段");
                        }

                        sql += " and " + statusFile + " = ?";
                        
                        param.add(statusValue);
                    }

                    label = jdbcTemplate.queryForObject(sql, param.toArray(), String.class);

                }
                if (StringUtils.isEmpty(sensible.toField())) {
                    ReflectUtil.setFieldValue(obj, field, label);
                } else {
                    Field toField = ReflectUtil.getField(clazz, sensible.toField());
                    ReflectUtil.setFieldValue(obj, toField, label);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
