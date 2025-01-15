package com.power;

import javafx.util.Pair;

import java.util.AbstractMap;

public class PairTest {
    public static void main(String[] args) {
        Pair<Integer, String> pair = new Pair<>(1, "One");
        Integer key = pair.getKey();
        String value = pair.getValue();



//        AbstractMap.SimpleEntry<Integer, String> entry
//                = new AbstractMap.SimpleEntry<>(1, "one");
//        Integer key = entry.getKey();
//        String value = entry.getValue();
//
//        System.out.println("key=" + key);
//        System.out.println("value=" + value);
    }
}
