package com.uwindsor.gobang;

/**
 * Created by Sherry on 2017/3/18.
 */

public class ConvertXY {
    static String convertCordinate(int x, int y){
        String str="";
        str+= (char)(97 + x);
        str+=Integer.toString(y);
        return str;
    }

    static int convertX(String cordinate){
        int x;
        char cordinateX;
        cordinateX = cordinate.charAt(0);
        x = cordinateX - 97;
        //cordinateX = cordinate.substring(0,1);
        //x = Integer.parseInt(cordinateX);
        return x;
    }
    static int convertY(String cordinate){
        int y;
        String cordinateY="";
        int pos = cordinate.length();
        cordinateY = cordinate.substring(1, pos);
        y = Integer.parseInt(cordinateY);
        return y;
    }
}
