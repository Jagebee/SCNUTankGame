package com.tedu.show;

import com.tedu.element.ElementOrigin;
import com.tedu.element.Player;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * @说明 游戏的主要面板
 * @author mooken
 * @功能说明 主要进行元素的显示，同时进行界面的刷新（多线程）
 *
 * @题外话 java开发首先思考的应该是：做继承或接口实现
 *
 * @多线程刷新 1、本类实现线程接口
 *           2、本类中定义一个内部类来实现
 */
public class GameMainJPanel extends JPanel implements Runnable{
    //联动管理器
    private ElementManager em;

    public GameMainJPanel() {
        init();
    }
//        //  以下代码后面会换地方重写（测试代码）
//        load();
//    public void load() {
//        //图片导入
//        ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");
//        ElementOrigin org = new Player(100,100,50,50,icon);
//        //将对象放入到元素管理器中
//        //em.getElementsByKey(GameElement.PLAYER).add(org);
//        em.addElement(org,GameElement.PLAYER);//直接添加
//    }

    private void init() {
        em = ElementManager.getManager();// 得到元素管理器对象
    }

    /**
     * paint方法是进行绘画元素
     * 绘画时有固定的顺序，先绘画的图片会在底层，后绘画的图片会覆盖先绘画的
     * 约定：本方法只执行一次，想实时刷新  需要使用多线程
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override //用于绘画的 Graphics 画笔
    public void paint(Graphics g) {
        super.paint(g);
//      map key-value key是无序不可重复的
//      set 和map的key一样  无序不可重复
        Map<GameElement,List<ElementOrigin>> all = em.getGameElements();
        //GameElement.values() 隐藏方法，返回值是一个数组，数组的顺序就是定义枚举的顺序
        for(GameElement ge : GameElement.values()) {
            List<ElementOrigin> list = all.get(ge);
            for(int i = 0;i < list.size();i++) {
                ElementOrigin org = list.get(i);
                org.showElement(g);
            }
        }
        //Set<GameElement> set = all.keySet(); //得到所有的key集合
        //for(GameElement ge : set) { //迭代器
        //    List<ElementOrigin> list = all.get(ge);
        //    for(int i = 0;i < list.size();i++)
        //    {
        //        ElementOrigin org = list.get(i);//读取为基类
        //        org.showElement(g);//调用每个类的自己的show方法完成自己的显示
        //    }
        //}
    }

    @Override
    public void run() { //接口实现
        while(true) {
            this.repaint();
            //一般情况下，多线程都会使用一个休眠，控制速度
            try {
                Thread.sleep(50); //休眠50ms，一秒刷新20次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
