//package com.cz.ex2;
//
//import com.cz.admin.Student;
//
//import java.lang.reflect.Field;
//
///**
// * @descriptions:
// * @author: Joker.
// * @date: 2024/11/19 21:18
// * @version: 1.0
// */
//
//public class Test1 {
//    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//
//        Student student1 = new Student("cz", 18, "河南省许昌市建安区", 99.0);
//
//        Teacher teacher = new Teacher();
//        copyBean(student1, teacher);
//        System.out.println(teacher);
//    }
//
//    public static void copyBean(Object source, Object target) {
//        Class<?> sourceClass = source.getClass();
//        Class<?> targetClass = target.getClass();
//        //遍历源字段
//        for (Field sourceField : sourceClass.getDeclaredFields()) {
//            sourceField.setAccessible(true);
//            //取出源字段的值
//            try {
//                Object sourceValue = sourceField.get(source);
//
//                // 根据源字段的名字得到目标字段
//                Field targetField = targetClass.getDeclaredField(sourceField.getName());
//                targetField.setAccessible(true);
//                // 判断字段的类型是否相同
//                if (sourceField.getType().equals(targetField.getType())) {
//                    targetField.set(target, sourceValue);
//                }
//            } catch (NoSuchFieldException e) {
//
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
