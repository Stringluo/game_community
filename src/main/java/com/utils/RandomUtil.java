package com.utils;

import java.util.Random;

public class RandomUtil {

    public static String getVCode(int i){
        String str = "";
        for (int j = 0; j < i; j++) {
            Random random = new Random();
            str += random.nextInt(10);
        }
        return str;
    }
}
