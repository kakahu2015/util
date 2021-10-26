# A simple java dictionary translation tool class

## Introduction

When we are developing, there may be scenarios where some code values are translated into Chinese. There will be a dictionary table in the library table, which records the corresponding code values and Chinese translation, 
as shown in the figure below (the table name is dict)：

![image-20211020154224837](src/main/resources/image/dict.png)

![image-20211020152005035](src/main/resources/image/dict_data.png)

And our user table (for example), the design is as follows (table name is sys_user):

<img src="src/main/resources/image/sys_user.png" alt="image-20211020154359187" style="zoom:80%;" />

![image-20211020152216837](src/main/resources/image/sys_user_data.png)



In the sys_user table, a column is designed as sex (gender), this column holds the value of 1 or 2, and this value corresponds to the dictionary data of the SEX_TYPE type in the dict table, and the dict table describes the corresponding code value Chinese translation

When we query the corresponding user information in the back-end code and return it to the front-end, we need to translate the sex field, that is, translate the code value (1 or 2) into the corresponding Chinese, as shown in the following figure:

![image-20211020154938242](src/main/resources/image/util_postman.png)

The value of sex saved in the library table is 1 or 2, but in the data returned to the front end, the value of sex is the Chinese translation

## Instructions for use

### Introduce maven dependency
```
 <dependency>
        <groupId>org.kakahu.dict</groupId>
        <artifactId>util</artifactId>
        <version>1.0-SNAPSHOT</version>
 </dependency>
```

### java code

Just declare the `@DictSensible` annotation on the method of the **controller** class

![image-20211020155247006](src/main/resources/image/util_code.png)

#### `@DictSensible` annotation description

Please check the source code for the specific code

| 字段名           | 含义                                       | 是否必须                     |
| ---------------- | ------------------------------------------ | ---------------------------- |
| codeValues       | 对应实体类中需要翻译的列名                 | 是                           |
| codeFileName     | 字典表中代码值的列名                       | 是                           |
| tableName        | 字典表的表名                               | 是                           |
| chineseFieldName | 字典表的中文翻译字段名称                   | 是                           |
| typeFieldName    | 字典表中的字典类型字段名称                 | 否                           |
| typeValue        | 字典类型的值                               | 如果有typeFieldName，则必须  |
| statusFileName   | 字典表中的字典状态字段名称                 | 否                           |
| statusValue      | 字典表中状态的值                           | 如果有statusFileName，则必须 |
| toField          | 翻译后放在实体类的字段，为空则放在代码字段 | 否                           |

#### Custom translation instructions

The current version retains the `DictCustomConfig` interface, which defines three methods (please check the source code for the specific code)

| 方法名            | 入参                     | 返回值                  | 说明                                                         |
| ----------------- | ------------------------ | ----------------------- | ------------------------------------------------------------ |
| initDictConfig    | DictSensiblesBean.classs | DictSensiblesBean.class | 对于一些统一的注解配置，可以在这里做统一设置，如tableName、chineseFieldName，但如果在注解上也设置了相同的属性，注解的设置优先 |
| getLableInChinese | DictSensiblesBean.class  | String.class            | 自定义翻译，这里会传入一个DictSensiblesBean.class，包含initDictConfig方法中设置的值和注解上设置的值，可自定义进行翻译。如果不想自定义翻译，该工具会自动翻译。自定义翻译的优先级高于自动翻译，如果实现了自定义翻译，将不会进行自动翻译。**推荐使用自定义翻译** |
| getData           | Object.class             | List.class              | 这里会传入返回的对象，目前自动解析`com.baomidou.mybatisplus.core.metadata.IPage`和`com.baomidou.mybatisplus.extension.api.R`两种类型的返回对象，对于其他类型的返回对象，需要自己在这个方法进行解析，获取到需要翻译的List |

#### DictSensiblesBean.class description

| 字段名           | 含义                                       |
| ---------------- | ------------------------------------------ |
| codeValues       | 对应实体类中需要翻译的列名                 |
| codeFileName     | 字典表中代码值的列名                       |
| tableName        | 字典表的表名                               |
| chineseFieldName | 字典表的中文翻译字段名称                   |
| typeFieldName    | 字典表中的字典类型字段名称                 |
| typeValue        | 字典类型的值                               |
| statusFileName   | 字典表中的字典状态字段名称                 |
| statusValue      | 字典表中状态的值                           |
| toField          | 翻译后放在实体类的字段，为空则放在代码字段 |

#### Implement the demo of `DictCustomConfig`

```java
@Configuration
public class DictConfig implements DictCustomConfig {

    @Override
    public DictSensiblesBean initDictConfig(DictSensiblesBean dict) {
        return  dict.tableName("dict").chineseFieldName("dict_chinese").typeFieldName("dict_type");
    }

    @Override
    public String getLableInChinese(DictSensiblesBean dict) {
        //自行翻译，如不需要自定义翻译，请直接返回null
		//Line translation, if you don’t need custom translation, please return null directly
        return null;
    }

    @Override
    public List<Object> getData(Object o) {
       	Map map = (Map) o;
        return (List<Object>) map.get("data");
    }
}
```

**Note:** The class that implements the `DictCustomConfig` interface must be decorated with the `@Configuration` annotation
Special thanks to https://github.com/simple-mine/util
