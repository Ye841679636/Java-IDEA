package com.newpoint.marketdata;

import com.newpoint.util.FileUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MarketDataCacheManager {
    private final static MarketDataCacheManager cacheManager = new MarketDataCacheManager();

    private Map<MarketDataKey, MarketDataCache> cacheMap = new HashMap<MarketDataKey, MarketDataCache>();

    public static MarketDataCacheManager getInstance() {
        return cacheManager;
    }

    public MarketDataCache getDataCache(MarketDataKey dataKey) {
        return cacheMap.get(dataKey);
    }

    public void createCacheForKey(MarketDataKey dataKey) {
        cacheMap.put(dataKey,new MarketDataCache(dataKey));
    }

    public void saveDataToFile() {
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("test");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(cacheMap);
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }
    }

    public void retrieveDataFromFile() {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("test");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            cacheMap = (Map<MarketDataKey, MarketDataCache>) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            ex.printStackTrace();
        }
    }

    /**
     * 把cacheMap数据存到本地
     */
    public void writeDataToFile() {
        try {
            for (MarketDataKey key : cacheMap.keySet()) {
                String fileName = key.getBarSize() + key.getSecurity().symbol + ".ser";
                FileUtil.deleteFile("historicalDataTemplate/" + fileName);
                //Saving of object in a file
                FileOutputStream file = new FileOutputStream("historicalDataTemplate/" + fileName);
                ObjectOutputStream out = new ObjectOutputStream(file);

                // Method for serialization of object

                Map<MarketDataKey, MarketDataCache> newCacheMap = new HashMap<MarketDataKey, MarketDataCache>();
                newCacheMap.put(key,cacheMap.get(key));
                out.writeObject(newCacheMap);
                out.close();
                file.close();
            }
            System.out.println("Object has been serialized");
        } catch(IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }
    }

    /**
     * 读取文件到cacheMap
     * @param filePath
     * @return
     */
    public Map<MarketDataKey, MarketDataCache> retrieveDataFromFile(String filePath) {
        Map<MarketDataKey, MarketDataCache> marketDataMap = new HashMap<>();
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            marketDataMap = (Map<MarketDataKey, MarketDataCache>) in.readObject();
            for (MarketDataKey key : marketDataMap.keySet()) {
                cacheMap.put(key,marketDataMap.get(key));
            }
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch(IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
            ex.printStackTrace();
        }
        return marketDataMap;
    }
}
