package com.tedu.manager;

import com.tedu.element.ElementOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @说明 本类是元素管理器，专门存储所有元素，同时提供方法给予视图和控制获取数据
 * @author mooken
 * @问题一：存储所有元素数据，如何存放？lsit,map,set 3大集合
 * @问题二：管理器是视图和控制要访问的，管理器就必须只有一个，单例模式
 */
public class ElementManager {
    /**
     * String 作为key匹配所有的元素 player -> List<Object> listplayer
     *                           enemy -> List<Object> lsitEnemy
     * 枚举类型，当作map的key用来区分不一样的资源，用于获取资源
     * List中元素的泛型应该是元素基类
     * 所有的元素都可以存放到map集合中，显示模块只需要获取到这个map就可以显示所有的界面所需要的元素(调用元素基类的showElement())
     */
    private Map<GameElement,List<ElementOrigin>> gameElements;
//  本方法一定不够用
    public Map<GameElement, List<ElementOrigin>> getGameElements() {
        return gameElements;
    }
//  添加元素
    public void addElement(ElementOrigin org,GameElement ge) {
        //List<ElementOrigin> list = gameElements.get(ge);
        //list.add(org);
        gameElements.get(ge).add(org);
    }
    //根据key返回list集合，取出某一类元素
    public List<ElementOrigin> getElementsByKey(GameElement ge){
        return gameElements.get(ge);
    }
    /**
     * 单例模式：内存中有且只有一个实例
     * 饿汉模式--启动就自动加载实例
     * 饱汉模式--需要使用的时候才加载实例
     *
     * 编写方式：
     * 1、需要一个静态的属性(定义一个常量)
     * 2、提供一个静态的方法（返回这个实例）return单例的引用
     * 3、一般，未防止其他人自己使用（类是可以实例化的）,所以会私有化构造方法
     *  ElementManager em = new ElementManager();
     */

    private static ElementManager EM = null; //引用

    //synchronized线程锁->保证本方法执行中只有一个线程
    public static synchronized ElementManager getManager() {
        if (EM == null) {//空值判定
            EM = new ElementManager();
        }
        return EM;
    }

    private ElementManager() {//私有化构造方法
        init();
    }

    //static  {//饿汉实例化对象 //静态语句块是在类被加载的时候直接执行
    //    EM = new ElementManager(); //只会执行一次
    //}

    /**
     * 本方法是为将来可能出现的功能扩展，重写init方法准备的
     */
    public void init() {//实例化在这里完成
        //hashmap hash散列
        gameElements = new HashMap<GameElement,List<ElementOrigin>>();
        //将每种元素集合都放入到map中
        //gameElements.put(GameElement.PLAYER,new ArrayList<ElementOrigin>());
        //gameElements.put(GameElement.MAPS,new ArrayList<ElementOrigin>());
        //gameElements.put(GameElement.ENEMY,new ArrayList<ElementOrigin>());
        //gameElements.put(GameElement.BOSS,new ArrayList<ElementOrigin>());
        for(GameElement ge : GameElement.values()){ //通过循环读取枚举类型的方式添加集合
            gameElements.put(ge,new ArrayList<ElementOrigin>());
        }
        //道具、子弹、爆炸效果、死亡效果。。。。
    }
}
