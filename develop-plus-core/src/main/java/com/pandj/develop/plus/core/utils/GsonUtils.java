package com.pandj.develop.plus.core.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pandj.develop.plus.core.enums.GsonSingleton;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * object to json / json to Object
 * </P>
 * &#064;Author pandujun
 * </P>
 * &#064;Date 2024/12/27
 */
public class GsonUtils {

    /**
     * 获取Gson对象
     *
     * @return Gson对象
     */
    public static Gson getGson() {
        return GsonSingleton.INSTANCE.getGson();
    }

    /**
     * 获取List<clazz>类型的对象
     *
     * @param clazz 转换类型class
     * @return gson转换类型的type
     */
    public static Type getListType(Class<?> clazz) {
        return TypeToken.getParameterized(List.class, clazz).getType();
    }

    /**
     * 获取Set<clazz>类型的对象
     *
     * @param clazz 转换类型class
     * @return gson转换类型的type
     */
    public static Type getSetType(Class<?> clazz) {
        return TypeToken.getParameterized(Set.class, clazz).getType();
    }

    /**
     * 获取Map<keyClazz, valueClazz>类型的对象
     *
     * @param keyClazz map的key class
     * @param valueClazz map的value class
     * @return gson转换类型的type
     */
    public static Type getMapType(Class<?> keyClazz, Class<?> valueClazz) {
        return TypeToken.getParameterized(Map.class, keyClazz, valueClazz).getType();
    }

    /**
     * 获取组合式class [type] 类型的对象
     *
     * @param clazz 最外层类型
     * @param type 里面组合的类型
     * @return gson转换类型的type
     */
    public static Type getCombinationType(Class<?> clazz, Type type) {
        return TypeToken.getParameterized(clazz, type).getType();
    }
}
