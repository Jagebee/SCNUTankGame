package com.tedu.controller;

import com.tedu.element.*;
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

    private int currentLevel = 1;
    private static final int MAX_LEVEL = 3;

    public GameThread() {
        em = ElementManager.getManager();
    }

    @Override
    public void run() {//游戏的run方法 主线程
        while (true) {//扩展 可以将true变为一个变量用于控制结束
//      游戏开始前 读进度条 加载游戏资源或场景资源
            gameLoad();
//      游戏进行时 游戏过程中
            boolean isClear = gameRun();
//      游戏场景结束 游戏资源回收（场景资源）
            gameOver(isClear);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(currentLevel > MAX_LEVEL){
                System.out.println("恭喜通过所有关卡");
                System.exit(0);
            }
        }
    }

    /**
     * 游戏的加载
     */
    private void gameLoad() {
        if(currentLevel == 1) {
            GameLoad.MapLoad(10);
            load(currentLevel);
        }else if(currentLevel == 2) {
            GameLoad.MapLoad(9);
            load(currentLevel);
        }else if(currentLevel == 3) {
            GameLoad.MapLoad(3);
            load(currentLevel);
        }
    }

    /**
     * @说明 游戏进行时
     * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动、碰撞、死亡 2.新元素的增加（NPC死亡后出现道具） 3.暂停等等
     * 先实现主角的移动
     */
    private long gameTime = 0L;
    private boolean gameRun() {
        long gameTime = 0L;
        boolean gameOver = false;
        boolean isClear = false;

        while (!gameOver) {//预留扩展 true可以变为变量 用于控制关卡结束等
            if (!gameOver) {
                Map<GameElement, List<ElementOrigin>> all = em.getGameElements();
                List<ElementOrigin> enemys = em.getElementsByKey(GameElement.ENEMY);
                List<ElementOrigin> files = em.getElementsByKey(GameElement.PLAYFILE);
                List<ElementOrigin> maps = em.getElementsByKey(GameElement.MAPS);
                List<ElementOrigin> players = em.getElementsByKey(GameElement.PLAYER);

                gameElementAuto(all, gameTime);
                ElementPK(maps, files);
                ElementPK(enemys, files);
                ElementPK(players, files);
                ElementPK(players, enemys);

//                for (ElementOrigin player : players) {
//                    for (ElementOrigin enemy : enemys) {
//                        if (player.pk(enemy) && player instanceof Player) {
//                            ((Player) player).reduceHP(((Player) player).getHp());
//                        }
//                    }
//                }

                for (ElementOrigin player : players) {
                    if (player instanceof Player && ((Player) player).getHp() <= 0 || !player.isLive()) {
                        System.out.println("游戏失败！");
                        player.die();
                        gameOver = true; // 设置游戏结束标志
                        break;
                    }
                }

                if(enemys.isEmpty()) {
                    System.out.println("第" + currentLevel + "通关");
                    isClear = true;
                    gameOver = true;
                }
                gameTime++;
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isClear;
    }

    public void ElementPK(List<ElementOrigin> listA, List<ElementOrigin> listB) {
        for (int i = 0; i < listA.size(); i++) {
            ElementOrigin elementA = listA.get(i);
            for (int j = 0; j < listB.size(); j++) {
                ElementOrigin elementB = listB.get(j);
                if (elementA.pk(elementB)) {
                    if (elementA instanceof Maps && elementB instanceof PlayFile) {
                        Maps wall = (Maps) elementA;
                        PlayFile bullet = (PlayFile) elementB;
                        if (!wall.isBulletPenetrable()) {
                            wall.setLive(false);
                            bullet.setLive(false);
                        }
                    } else if (elementA instanceof Player && elementB instanceof PlayFile) {
                        Player player = (Player) elementA;
                        PlayFile bullet = (PlayFile) elementB;
                        player.setLive(false);
                        player.die();
                        bullet.setLive(false); // 子弹碰撞后消失
                    } else if(elementA instanceof Player && elementB instanceof Enemy) {
                        Player player = (Player) elementA;
                        player.setLive(false);
                        player.die();
                    }
                    else {
                        elementA.setLive(false);
                        elementB.setLive(false);
                        elementB.die();
                    }
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
    private void gameOver(boolean isClear) {
        em.getGameElements().values().forEach(List::clear);

        if(isClear){
            currentLevel++;
        } else {
            currentLevel = currentLevel;
            System.out.println("重新开始");
        }
    }

//    public void load(int currentLevel) {
//        //图片导入
//        ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");
//
//        //创建主角和敌人
//        switch (currentLevel) {//每个关卡单独设计主角和敌人位置
//            case 1:
//                ElementOrigin org = new Player(100,100,50,50,icon);//创建主角
//                em.addElement(org,GameElement.PLAYER);
//                em.addElement(new Enemy().createElement("0,0,300,0,right"),GameElement.ENEMY);//创建敌人
//                break;
//            case 2:break;
//            case 3:break;
//        }
//    }

    public void load(int currentLevel) {
        // 图片导入，加载玩家向上的图标
        ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");

        // 创建主角和敌人，根据关卡不同设置不同位置
        switch (currentLevel) {
            case 1:

                ElementOrigin org1 = new Player(100, 100, 50, 50, icon);//创建第一关主角
                em.addElement(org1, GameElement.PLAYER);
                em.addElement(new Enemy().createElement("0,0,300,0,right"), GameElement.ENEMY);//创建第一关敌人
                break;
            case 2:
                // 第二关：主角位置可调整，比如(200, 200)，敌人位置等也按需改
                ElementOrigin org2 = new Player(200, 200, 50, 50, icon);//创建第二关主角
                em.addElement(org2, GameElement.PLAYER);
                // 假设敌人位置改为 "100,100,300,0,down" ，方向向下等，按需调整参数
                em.addElement(new Enemy().createElement("100,100,300,0,down"), GameElement.ENEMY);//创建第二关敌人
                break;
            case 3:
                // 第三关：主角位置再调整，比如(300, 300)，敌人位置等继续改
                ElementOrigin org3 = new Player(300, 300, 50, 50, icon);//创建第三关主角
                em.addElement(org3, GameElement.PLAYER);
                // 敌人位置等参数按需设置，比如 "200,200,300,0,left" ，方向向左
                em.addElement(new Enemy().createElement("200,200,300,0,left"), GameElement.ENEMY);//创建第三关敌人
                break;

        }
    }

}
