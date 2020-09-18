package com.ailink.util;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class WifiManage {


    // 获取当前的Wifi连接
    public static WifiConfiguration getCurrentWifiConfiguration(WifiManager wifiManager) {
        if (!wifiManager.isWifiEnabled())
            return null;
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int cur = wifiManager.getConnectionInfo().getNetworkId();
        for (int i = 0; i < configurationList.size(); ++i) {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == cur)
                configuration = wifiConfiguration;
        }
        return configuration;
    }


    // API 17 可以用
// 其它可以用的版本需要再测试和处理
// @exclList 那些不走代理， 没有传null,多个数据以逗号隔开
    public static void setWifiProxySettingsFor17And(Context context, String host, int port, String exclList) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config;

        config = getCurrentWifiConfiguration(wifiManager);
        if (config == null) return;

        try {
//get the link properties from the wifi configuration
            Object linkProperties = getFieldObject(config, "linkProperties");
            if (null == linkProperties) return;

//获取类 LinkProperties的setHttpProxy方法
            Class<?> proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class<?>[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyPropertiesClass;
            Class<?> lpClass = Class.forName("android.net.LinkProperties");

            Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);


// 获取类 ProxyProperties的构造函数
            Constructor<?> proxyPropertiesCtor = proxyPropertiesClass.getConstructor(String.class, int.class, String.class);
            // 实例化类

//            proxyPropertiesClass  proxySettings = proxyPropertiesCtor.newInstance(host, port, exclList);


//pass the new object to setHttpProxy

            Object[] params = new Object[1];
            params[0] = proxyPropertiesCtor.newInstance(host, port, exclList);;
            setHttpProxy.invoke(linkProperties, params);
            setEnumField(config, "STATIC", "proxySettings");

            //save the settings
            wifiManager.updateNetwork(config);
            wifiManager.disconnect();
            wifiManager.reconnect();
        } catch (Exception e) {
        }
    }


    // 取消代理设置
    public static void unsetWifiProxySettingsFor17And(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config;
        config = getCurrentWifiConfiguration(wifiManager);
        if (null == config) return;

        try {
            //get the link properties from the wifi configuration
            Object linkProperties = getFieldObject(config, "linkProperties");
            if (null == linkProperties) return;
//get the setHttpProxy method for LinkProperties

            Class<?> proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class<?>[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyPropertiesClass;

            Class<?> lpClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

//pass null as the proxy
            Object[] params = new Object[1];

            params[0] = null;
            setHttpProxy.invoke(linkProperties, params);
            setEnumField(config, "NONE", "proxySettings");

//save the config
            wifiManager.updateNetwork(config);

            wifiManager.disconnect();
            wifiManager.reconnect();
        } catch (Exception e) {
        }
    }


    // getField只能获取类的public 字段.
    public static Object getFieldObject(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f =
                obj.getClass().getField(name);
        Object out = f.get(obj);
        return out;
    }


    // 设置公共成员常量值
    public static void setEnumField(Object obj, String value, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }


}
