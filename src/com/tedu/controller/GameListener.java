package com.tedu.controller;

import com.tedu.element.ElementOrigin;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @说明 监听类，用于监听用户的操作
 * @author mooken
 *
 */
public class GameListener implements KeyListener {
    private ElementManager em = ElementManager.getManager();

    /*能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束
    * 同时，第一次按下，记录到集合中，第二次判定集合中是否有 松开就直接删除集合中的记录
    * set集合*/

    private Set<Integer> set = new HashSet<Integer>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 按下：左37、上38、右39、下40 按tab没反应
     * 实现主角的移动
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //拿到玩家集合
        int key = e.getKeyCode();

        if(set.contains(key)) {//判定集合中是否已经存在或包含这个对象
            return ;//如果包含  直接结束方法
        }
        set.add(key);
        List<ElementOrigin> player = em.getElementsByKey(GameElement.PLAYER);
        for(ElementOrigin org : player){
            org.keyClick(true,e.getKeyCode());
        }
    }

    /**
     * 松开
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(!set.contains(e.getKeyCode())) {
            return ;
        }
        set.remove(e.getKeyCode());//移除数据
        List<ElementOrigin> player = em.getElementsByKey(GameElement.PLAYER);
        for(ElementOrigin org : player){
            org.keyClick(false,e.getKeyCode());
        }
    }
}
