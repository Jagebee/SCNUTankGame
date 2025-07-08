package com.tedu.manager;

import com.tedu.element.ElementOrigin;
import com.tedu.element.Maps;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @说明 加载器（工具：用于读取配置文件的工具） 工具类，大多提供的是static方法
 * @author mooken
 */
public class GameLoad {
    //得到资源管理器
    private static ElementManager em = ElementManager.getManager();
    //  图片集合 使用map来进行存储 枚举类型配合移动
    public static Map<String, ImageIcon> imgMap;

    static {
        imgMap = new HashMap<>();
        imgMap.put("left",new ImageIcon("image/tank/play1/player1_left.png"));
        imgMap.put("right",new ImageIcon("image/tank/play1/player1_right.png"));
        imgMap.put("up",new ImageIcon("image/tank/play1/player1_up.png"));
        imgMap.put("down",new ImageIcon("image/tank/play1/player1_down.png"));

//      Collections 用于集合排序的工具类，可以为所有的对象类型的记录进行排序 排序只能为Collection的子类
    }
//  用户读取文件的类
    private static Properties pro = new Properties();
    /**
     * @说明 传入地图的ID 有加载方法依据文件规则自动产生地图文件名称 加载文件
     * @param mapID 文件ID
     */
    public static void MapLoad(int mapID) {
//      得到了我们的文件路径
        String mapName = "com/tedu/text/" + mapID + ".map";
//      使用io流来获取文件对象
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream maps = classLoader.getResourceAsStream(mapName);
        if(maps == null) {
            System.out.println("error");
            return;
        }

        try {
            pro.load(maps);
            Enumeration<?> names = pro.propertyNames();
            while(names.hasMoreElements()) {//获取是无序的
                String key = names.nextElement().toString();
//                System.out.println(pro.getProperty(key));
                String [] arrs = pro.getProperty(key).split(";");
                for(int i = 0; i < arrs.length; i++) {
                    ElementOrigin element  = new Maps().createElement(key + "," + arrs[i]);
                    em.addElement(element,GameElement.MAPS);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @说明 加载图片代码
     * 加载图片 代码和图片之间差一个路径问题
     */
    public static void loadImg() {

    }
}
