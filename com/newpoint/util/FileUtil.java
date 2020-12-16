package com.newpoint.util;

import com.newpoint.marketdata.MarketDataCache;
import com.newpoint.marketdata.MarketDataCacheManager;
import com.newpoint.marketdata.MarketDataKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

    private MarketDataCacheManager cacheManager = MarketDataCacheManager.getInstance();

    /**
     * 获取文件夹里面的所有文件路径
     * @param path
     * @return
     */
    public static List<Map<String,String>> getFilePath(String path) {
        List<Map<String,String>> fileNameList = new ArrayList<>();
        File file = new File(path);
        File[] array = file.listFiles();
        for(int i=0;i< array.length;i++) {
            if(array[i].isFile()) {
                Map<String, String> dataFile = new HashMap<>();
                dataFile.put("fileName",array[i].getName());
                dataFile.put("filePath",array[i].getPath());
                fileNameList.add(dataFile);
            }
        }
        return fileNameList;
    }


    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            System.out.println("删除单个文件" + fileName + "成功！");
            return true;
        } else {
            System.out.println("删除单个文件" + fileName + "失败！");
            return false;
        }
    }

    /**
     * 读取历史数据文件
     */
    public static Map<MarketDataKey, MarketDataCache> readHistoricalDataTemplateData(String fileName) {
        Map<MarketDataKey, MarketDataCache> marketDataMap = new HashMap<>();
        List<Map<String,String>> list = FileUtil.getFilePath("historicalDataTemplate/");
        for (int i = 0; i < list.size(); i++) {
            if (fileName.equals(list.get(i).get("fileName"))) {
                marketDataMap = MarketDataCacheManager.getInstance().retrieveDataFromFile(list.get(i).get("filePath"));
            }
        }
        return marketDataMap;
    }
}
