package com.newpoint.util;

import java.text.DecimalFormat;

public class NumberUtil {

    /**
     * double 类型千位分割 保留两位小数
     * pattern 格式 列：（“,###,###.00”）
     * */
    public static String parseDoubleToString(String pattern, double value) {
        String resultStr = "";
        if(!(value+"").isEmpty()){
            if(value == 0){
                resultStr = "0";
            }else{
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                resultStr = decimalFormat.format(value);
            }
        }
        return resultStr;
    }
}
