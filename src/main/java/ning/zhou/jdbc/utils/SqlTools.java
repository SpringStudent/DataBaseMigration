package ning.zhou.jdbc.utils;

import ning.zhou.jdbc.bean.SqlBean;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SQL 工具类
 *
 * @data 2018年3月9日
 */
public class SqlTools {

    /**
     * 批量插入分页大小值
     */
    private static final int BATCH_PARAM_SIZE = 500;

    /**
     * 去除掉所有value等于null、""、" "
     *
     * @param filterMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map<String, Object> clearFilterMap(Map<String, Object> filterMap) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : filterMap.keySet()) {
            Object value = filterMap.get(key);
            if (value instanceof Map) {
                Map subValue = (Map) value;
                if (EmptyUtils.isNotEmpty(subValue)) {
                    map.put(key, value);
                }
            }
            try {
                if (StringUtils.isEmpty(String.valueOf(value))) {
                    String[] keys = StringUtils.split(key, "_");
                    if (keys.length == 2) {
                        Map<String, Object> innermap = null;
                        if ((map.get(keys[0]) != null) && (map.get(keys[0]) instanceof Map)) {
                            innermap = (Map) map.get(keys[0]);
                        } else {
                            innermap = new HashMap<String, Object>();
                        }
                        innermap.put(keys[1], value);
                        map.put(keys[0], innermap);
                    } else {
                        map.put(key, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Filter property value is error");
            }

        }
        return map;
    }

    @SuppressWarnings({"unchecked"})
    public static SqlBean SqlHelp(Map<String, Object> filterMap, LinkedHashMap<String, String> sortMap, Class<?> entityClass) throws Exception {
        SqlBean sqlBean = new SqlBean();
        String primaryKey = "id";
        Field[] fields = entityClass.getDeclaredFields();
        primaryKey = EntityTools.getPrimaryKey(fields);
        filterMap = clearFilterMap(filterMap);
        StringBuffer sql = new StringBuffer();
        Object[] values = {};
        int[] argTypes = {};
        if (EmptyUtils.isNotEmpty(filterMap)) {
            for (String key : filterMap.keySet()) {
                int type = getTypes(entityClass.getDeclaredField(key));
                if (filterMap.get(key) instanceof Map) {
                    Map<String, Object> innerMap = (Map<String, Object>) filterMap.get(key);
                    if (innerMap.containsKey("like")) {
                        sql.append(" and " + key + " like ? ");
                        String value = String.valueOf(innerMap.get("like"));
                        values = ArrayUtils.add(values, value);
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("g")) {
                        sql.append(" and " + key + " > ? ");
                        values = ArrayUtils.add(values, innerMap.get("g"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("l")) {
                        sql.append(" and " + key + " < ? ");
                        values = ArrayUtils.add(values, innerMap.get("l"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("ge")) {
                        sql.append(" and " + key + " >= ? ");
                        values = ArrayUtils.add(values, innerMap.get("ge"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("or")) {
                        sql.append(" or " + key + " = ? ");
                        values = ArrayUtils.add(values, innerMap.get("or"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("le")) {
                        sql.append(" and " + key + " <= ? ");
                        values = ArrayUtils.add(values, innerMap.get("le"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("ne")) {
                        sql.append(" and " + key + " <> ? ");
                        values = ArrayUtils.add(values, innerMap.get("ne"));
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("null")) {
                        sql.append(" and (" + key + " is null or " + key + " = '') ");
                    }
                    if (innerMap.containsKey("notNull")) {
                        sql.append(" and " + key + " is not null ");
                    }
                    if (innerMap.containsKey("isNull")) {
                        sql.append(" and " + key + " is  null ");
                    }
                    if (innerMap.containsKey("between")) {
                        sql.append(" and (" + key + " between ? and ? ) ");
                        values = ArrayUtils.addAll(values, (Object[]) innerMap.get("between"));
                        argTypes = ArrayUtils.add(argTypes, type);
                        argTypes = ArrayUtils.add(argTypes, type);
                    }
                    if (innerMap.containsKey("in")) {//分层调用（避免in条件查询超过1000）
                        Object[] invalue = (Object[]) innerMap.get("in");
                        int cishu = (int) Math.ceil((invalue.length + 0.0) / BATCH_PARAM_SIZE);
                        for (int i = 1; i <= cishu; i++) {
                            if (i == 1) {
                                if (cishu == 1) {
                                    String inhql = " and ( " + key + " in(";
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < invalue.length; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != invalue.length - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    values = ArrayUtils.addAll(values, invalue);
                                } else {
                                    String inhql = " and ( " + key + " in(";
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < i * BATCH_PARAM_SIZE; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != i * BATCH_PARAM_SIZE - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, i * BATCH_PARAM_SIZE);
                                    values = ArrayUtils.addAll(values, iner);
                                }
                            } else {
                                String inhql = " or " + key + " in(";
                                if (i == cishu) {//如果是最后一层的时候
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < invalue.length; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != invalue.length - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, invalue.length);
                                    values = ArrayUtils.addAll(values, iner);
                                } else {
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < i * BATCH_PARAM_SIZE; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != i * BATCH_PARAM_SIZE - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, i * BATCH_PARAM_SIZE);
                                    values = ArrayUtils.addAll(values, iner);
                                }
                            }
                        }
                        sql.append(")");
                    }
                    if (innerMap.containsKey("notin")) {
                        //分层调用（避免oracle not in条件查询超过1000）
                        String[] invalue = ((String) innerMap.get("notin")).split(",");
                        int cishu = (int) Math.ceil((invalue.length + 0.0) / BATCH_PARAM_SIZE);
                        for (int i = 1; i <= cishu; i++) {
                            if (i == 1) {
                                if (cishu == 1) {
                                    String inhql = " and ( " + key + " not in(";
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < invalue.length; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != invalue.length - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    values = ArrayUtils.addAll(values, invalue);
                                } else {
                                    String inhql = " and ( " + key + " not in(";
                                    argTypes = ArrayUtils.add(argTypes, type);
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < i * BATCH_PARAM_SIZE; j++) {
                                        if (j != i * BATCH_PARAM_SIZE - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, i * BATCH_PARAM_SIZE);
                                    values = ArrayUtils.addAll(values, iner);
                                }
                            } else {
                                String inhql = " and " + key + " not in(";
                                if (i == cishu) {//如果是最后一层的时候
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < invalue.length; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != invalue.length - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, invalue.length);
                                    values = ArrayUtils.addAll(values, iner);
                                } else {
                                    for (int j = (i - 1) * BATCH_PARAM_SIZE; j < i * BATCH_PARAM_SIZE; j++) {
                                        argTypes = ArrayUtils.add(argTypes, type);
                                        if (j != i * BATCH_PARAM_SIZE - 1) {
                                            inhql += "?,";
                                        } else {
                                            inhql += "?";
                                        }
                                    }
                                    inhql += ")";
                                    sql.append(inhql);
                                    Object iner[] = ArrayUtils.subarray(invalue, (i - 1) * BATCH_PARAM_SIZE, i * BATCH_PARAM_SIZE);
                                    values = ArrayUtils.addAll(values, iner);
                                }
                            }
                        }
                        sql.append(")");
                    }
                } else {
                    sql.append(" and " + key + " = ? ");
                    values = ArrayUtils.add(values, filterMap.get(key));
                    argTypes = ArrayUtils.add(argTypes, type);
                }
            }
        }
        if (EmptyUtils.isNotEmpty(sortMap)) {
            sql.append(" order by ");
            for (String key : sortMap.keySet()) {
                if (sortMap.get(key).indexOf(",") != -1) {
                    String[] str = sortMap.get(key).split(",");
                    sql.append(" " + str[0] + "(" + key + ") " + (Boolean.parseBoolean(str[1]) ? " asc " : " desc ") + ",");
                } else {
                    sql.append(" " + key + (Boolean.parseBoolean(sortMap.get(key)) ? " asc " : " desc ") + ",");
                }
            }
            if (sql.lastIndexOf(",") == sql.length() - 1) {
                sql.append(" " + primaryKey + " asc ");
            }
        } else {
            sql.append(" order by " + primaryKey + " asc ");
        }
        sqlBean.setSqlString(sql);
        sqlBean.setAgrs(values);
        sqlBean.setTypes(argTypes);
        return sqlBean;
    }

    /**
     * 类型处理
     *
     * @param arg
     * @return
     */
    public static int getTypes(Field arg) {
        arg.setAccessible(true); // 暴力反射
        if (String.class.equals(arg.getType())) {
            return Types.VARCHAR;
        } else if (int.class.equals(arg.getType()) || Integer.class.equals(arg.getType())) {
            return Types.INTEGER;
        } else if (double.class.equals(arg.getType()) || Double.class.equals(arg.getType())) {
            return Types.DOUBLE;
        } else if (java.util.Date.class.isAssignableFrom(arg.getType())) {
            return Types.TIMESTAMP;
        } else if (long.class.equals(arg.getType()) || Long.class.equals(arg.getType())) {
            return Types.BIGINT;
        } else if (float.class.equals(arg.getType()) || Float.class.equals(arg.getType())) {
            return Types.FLOAT;
        } else if (boolean.class.equals(arg.getType()) || Boolean.class.equals(arg.getType())) {
            return Types.BOOLEAN;
        } else if (short.class.equals(arg.getType()) || Short.class.equals(arg.getType())) {
            return Types.INTEGER;
        } else if (byte.class.equals(arg.getType()) || Byte.class.equals(arg.getType())) {
            return Types.INTEGER;
        } else if (BigDecimal.class.equals(arg.getType())) {
            return Types.DECIMAL;
        } else {
            return Types.OTHER;
        }
    }
}
