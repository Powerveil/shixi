package com.power.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MyBeanUtils {

    @Data
    @AllArgsConstructor
    static class Test01 {
        private String name;
        private Integer age;
        private String address;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Test02 {
        private String name;
        private Integer age2;
        private String address2;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Test01 test01 = new Test01("张三", 20, "北京海淀区");
        Test02 test02 = new Test02();

        System.out.println("===============================");
        System.out.println(test01);
        System.out.println(test02);
        copy(test01, test02);

        System.out.println("===============================");
        System.out.println(test01);
        System.out.println(test02);
    }

    public static void copy(Object source, Object target) {
        List<String> fieldNameList = Arrays.stream(source.getClass().getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        for (String field : fieldNameList) {

            Method targetSet = findMethodByTypeAndName(target, "set", field);
            if (targetSet == null) continue;

            try {
                Object sourceGet = findMethodByTypeAndName(source, "get", field).invoke(source);

                targetSet.invoke(target, sourceGet);
            } catch (InvocationTargetException | IllegalAccessException ignored) {

            }
        }
    }

    /**
     * 根据属性，获取对应的 get 方法
     *
     * @param obj          要操作的对象
     * @param methodPrefix 方法类型，此处限定为 "set" 或 "get"
     * @param name         属性名，用于确定要获取的方法是针对哪个属性的
     * @return 如果找到匹配的方法则返回该方法对象，否则返回 null
     */
    public static Method findMethodByTypeAndName(Object obj, String methodPrefix, String name) {
        Method[] m = obj.getClass().getMethods();
        for (Method method : m) {
            if ((methodPrefix + name).equalsIgnoreCase(method.getName())) {
                return method;
            }
        }
        return null;
    }
}
