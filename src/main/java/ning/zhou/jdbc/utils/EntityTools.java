package ning.zhou.jdbc.utils;


import ning.zhou.jdbc.annotation.Column;
import ning.zhou.jdbc.annotation.Table;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 数据库实体映射工具类
 *
 * @data 2018年3月6日
 */
public class EntityTools {

    /**
     * 获取主键名称
     *
     * @param entity
     * @return
     */
    public static String getPk(Class<?> entity) {
        Class<?> modelType = entity.getClass();
        Table table = modelType.getAnnotation(Table.class);
        if (null != table) {
            return table.pk();
        }
        return "id";
    }

    /**
     * 判断是否为主键
     * @return
     */
    public static boolean isPk(Class<?> entity,Field field){
        String pk = getPk(entity);
        String columnName = field.getName();
        //该字段是主键
        if(pk.equals(columnName)){
            return true;
        }
        return false;
    }
    /**
     * 根据实体类名，获取表名称
     *
     * @param entity
     * @return
     */
    public static String getTableName(Class<?> entity) {
        if (StringUtils.isEmpty(entity)) {
            return null;
        }
        StringBuilder tableName = new StringBuilder();
        Table anno = entity.getAnnotation(Table.class);
        if (anno != null) {
            Method me = anno.annotationType().getDeclaredMethods()[0];
            if (!me.isAccessible()) {
                me.setAccessible(true);
            }
            try {
                tableName.append(me.invoke(anno, null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            String entityName = entity.getSimpleName();
            StringBuilder str = new StringBuilder();
            char[] subStr = entityName.toCharArray();
            int i = 0;
            char z = 'Z';
            while (i < entityName.length()) {
                while (i < entityName.length() && subStr[i] > z) {
                    if (Character.isLowerCase(subStr[i])) {
                        str.append(String.valueOf(subStr[i]).toUpperCase());
                    } else {
                        str.append(subStr[i]);
                    }
                    i++;
                }
                if (str.toString().length() > 0) {
                    if ((i - entityName.length() == 0)) {
                        tableName.append(str.toString());
                    } else {
                        tableName.append(str.toString() + "_");
                    }
                }
                if (i < entityName.length()) {
                    str = new StringBuilder();
                    str.append(subStr[i++]);
                }
            }
        }
        return tableName.toString();
    }

    public static String getColumnName(Field field) {
        String columnName = field.getName();
        Column anno = field.getAnnotation(Column.class);
        if (anno != null) {
            Method[] meth = anno.annotationType().getDeclaredMethods();
            if (meth != null) {
                for (Method me : meth) {
                    if (!me.isAccessible()) {
                        me.setAccessible(true);
                    }
                    try {
                        if ("name".equals(me.getName())) {
                            columnName = me.invoke(anno, null).toString();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return columnName;
    }
}
