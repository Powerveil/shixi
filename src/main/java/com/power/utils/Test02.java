package com.power.utils;

public class Test02 {
    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE);
        System.out.println(Math.abs((long) Integer.MIN_VALUE));
        System.out.println((int)Math.abs((long) Integer.MIN_VALUE));

        System.out.println(-2147483648 % 4);

        int a = -2147483648 % 4;
        System.out.println(a);
        System.out.println((int) Math.abs((long) Integer.MIN_VALUE) % 4);
    }
}
