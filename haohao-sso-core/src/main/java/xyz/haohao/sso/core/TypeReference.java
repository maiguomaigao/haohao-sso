package xyz.haohao.sso.core;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Json序反序列化时的多层嵌套泛型
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class TypeReference {

    private Type type;

    private Map<Type, Type> classTypeCache = new ConcurrentHashMap<>();

    protected TypeReference(Type... actualTypeArguments) {
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();
        ParameterizedType argType = (ParameterizedType)((ParameterizedType)superClass).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();
        int actualIndex = 0;

        for(int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable) {
                argTypes[i] = actualTypeArguments[actualIndex++];
                if (actualIndex >= actualTypeArguments.length) {
                    break;
                }
            }
        }

        Type key = new ParameterizedTypeImpl(argTypes, thisClass, rawType);
        Type cachedType = (Type)classTypeCache.get(key);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(key, key);
            cachedType = (Type)classTypeCache.get(key);
        }

        this.type = cachedType;
    }
}
