package cn.henu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by junwei on 2016/6/16.
 */
public class PropertyUtil {
    private final static String filePath = "systemConfig.properties";
    private static Map configMap = new HashMap();
    private static Properties prop = new Properties();
    public static void initSysconfig() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = null;
        in = cl.getResourceAsStream(filePath);
        try {
            prop.load(in);

            Iterator it = prop.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();

                configMap.put(key, prop.get(key));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到水印名称
     * @return
     */
    public static String getWaterNmae() {
        initSysconfig();
        String waterName = prop.getProperty("system.waterName").trim();
        if("".equals(waterName)){
            return "waterName";
        }else{
            return "请保密";
        }
    }
    public  static void main(String args[]){
        System.out.println(getWaterNmae());
    }
}
