package com.uwindsor.gobang;

/**
 * Created by Sherry on 2017/3/18.
 */

public class ConvertData {
    static String seperate = "\n";

    static String writeTotal(String head, String data, int size){
        String Info = head + data;
        for(int i= Info.length(); i < size; i++){
            Info += seperate;
        }
        return Info;
    }

    static String readHead(String Info){
        return Info.substring(0,1);
    }

    static String readData(String Info){
        int pos = Info.indexOf(seperate);
        return Info.substring(1, pos );
    }
}
