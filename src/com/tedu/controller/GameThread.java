package com.tedu.controller;

import com.tedu.element.ElementOrigin;
import com.tedu.element.Enemy;
import com.tedu.element.Player;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @说明 游戏的主线程 用于控制游戏加载，游戏关卡 游戏运行时的自动化 游戏判定 地图切换 资源释放和重新读取
 * @author mooken
 * @继承 使用继承的方式实现多线程（一般建议使用接口实现）
 */
public class GameThread extends Thread {
    private ElementManager em;

    public GameThread() {
        em = ElementManager.getManager();
    }

    @Override
    public void run() {//游戏的run方法 主线程
        while (true) {//扩展 可以将true变为一个变量用于控制结束
//      游戏开始前 读进度条 加载游戏资源或场景资源
            gameLoad();
//      游戏进行时 游戏过程中
            gameRun();
//      游戏场景结束 游戏资源回收（场景资源）
            gameOver();

            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 游戏的加载
     */
    private void gameLoad() {
        GameLoad.MapLoad(10);//可以变为变量 每一关重新加载
        load();
    }

    /**
     * @说明 游戏进行时
     * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动、碰撞、死亡 2.新元素的增加（NPC死亡后出现道具） 3.暂停等等
     * 先实现主角的移动
     */
    private long gameTime = 0L;
    private void gameRun() {
        long gameTime = 0L;
        while (true) {//预留扩展 true可以变为变量 用于控制关卡结束等
            Map<GameElement, List<ElementOrigin>> all = em.getGameElements();
            List<ElementOrigin> enemys = em.getElementsByKey(GameElement.ENEMY);
            List<ElementOrigin> files = em.getElementsByKey(GameElement.PLAYFILE);
            List<ElementOrigin> maps = em.getElementsByKey(GameElement.MAPS);

            gameElementAuto(all,gameTime);//游戏元素自动化方法

            ElementPK(maps,files);
            ElementPK(enemys,files);

            gameTime++;//唯一的时间控制
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ElementPK(List<ElementOrigin> listA, List<ElementOrigin> listB) {

        //请大家在这里使用循环，做一对一判定，如果为真，就设置两个对象的死亡状态
        for(int i = 0;i < listA.size();i++) {
            ElementOrigin enemy = listA.get(i);
            for(int j = 0;j < listB.size();j++) {
                ElementOrigin file = listB.get(j);
                if(enemy.pk(file)) {
                    //问题：如果是boss，那么也一枪一个吗？
                    //将setLive（false）方法变为一个受攻击方法，还可以传入另外一个对象的攻击力
                    //当受攻击方法执行时，如果血量减为0，再进行设置生存为false
                    enemy.setLive(false);
                    file.setLive(false);
                    break;
                }
            }
        }
    }

    //游戏元素自动化方法
    public void gameElementAuto(Map<GameElement, List<ElementOrigin>> all,long gameTime) {
        //GameElement.values() 隐藏方法，返回值是一个数组，数组的顺序就是定义枚举的顺序
        for(GameElement ge : GameElement.values()) {
            List<ElementOrigin> list = all.get(ge);
            for(int i = list.size()-1;i >= 0;i--) {
                ElementOrigin org = list.get(i);//读取为基类
                if(!org.isLive()) {//如果死亡
                    //启动一个死亡方法 方法中可以做很多事情 例如：死亡动画、掉装备等
                    org.die();
                    list.remove(i);
                    continue;
                }
                org.model(gameTime);
            }
        }
    }


    /**
     * 游戏切换关卡
     */
    private void gameOver() {
    }

    public void load() {
        //图片导入
        ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");
        ElementOrigin org = new Player(100,100,50,50,icon);
        //将对象放入到元素管理器中
        //em.getElementsByKey(GameElement.PLAYER).add(org);
        em.addElement(org,GameElement.PLAYER);//直接添加

        //创建敌人
        for(int i = 0;i < 1;i++) {
            em.addElement(new Enemy().createElement("0,0,300,0,right"),GameElement.ENEMY);
        }
    }

}
