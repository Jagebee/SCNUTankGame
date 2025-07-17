package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @说明 所有元素的基类
 * @author mooken
 *
 */
public abstract class ElementOrigin {
    private int x;
    private int y;
    private int w;
    private int h;
    private ImageIcon icon;
    private boolean live = true;//生存状态 true代表存活 false代表死亡
                            //可以采用枚举值来定义这个（生存、死亡、隐身、无敌）
//  注明：当重新定义一个用于判定状态的变量，需要思考：1.初始化 2.值的改变 3.值的判定
    //还有。。。各种必要的状态值，例如：是否生存。
    private int hp = 100; //对象的血量

    private boolean isExploding = false;

    private boolean isDying = false;

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public void setExploding(boolean exploding) {
        isExploding = exploding;
    }

    public ElementOrigin() {} //没有实际作用，为了不报错写的

    /**
     * @说明 带参数的构造方法； 可以由子类传输数据到父类
     * @param x 左上角x坐标
     * @param y
     * @param w
     * @param h
     * @param icon 图片
     */
    public ElementOrigin(int x, int y, int w, int h, ImageIcon icon) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.icon = icon;
    }

    /**
     * @说明 抽象方法，显示元素
     * @param g 画笔 用于进行绘画
     */
    public abstract void showElement(Graphics g);

    /**
     * @说明 使用父类定义接收键盘事件的方法
     * 只有需要实现键盘监听的子类重写这个方法（约定）
     * @说明 方式2 使用接口的方式；使用接口方式需要在监听类进行类型转换
     *
     * @题外话 约定 配置 现在大部分的java框架都是需要进行配置的
     *  约定优于配置
     *
     * @param b1 点击的类型 true代表按下，false代表松开
     * @param key 代表出发的键盘的code值
     * @扩展 本方法是否可以分为两个方法？ 1个接收按下，一个接收松开
     */
    public void keyClick(boolean b1,int key) { //这个方法不是强制必须重写的

    }
    /**
     * @说明 移动方法；需要移动的子类，请实现这个方法
     */
    protected void move() {

    }

    /**
     * @设计模式 模版模式；在模版模式中定义对象执行方法的先后顺序，由子类选择性重写方法
     *      1.移动 2.换装 3.子弹发射
     */
    public final void model(long gameTime) {
//  先换装
        updateImage(gameTime);
//  再移动
        move();
//  再发射子弹
        add(gameTime);
    }
    protected void updateImage(long gameTime) {}
    protected void add(long gameTime) {}

//  死亡方法
    public void die() {//死亡也是一个对象
        setLive(false);
        setExploding(false);
    }

    public ElementOrigin createElement(String str) {
        return null;
    }

    /**
     * @说明 本方法返回元素的碰撞矩形对象（实时返回）
     * @return
     */
    public Rectangle getRectangle() {
        //可以将这个数据进行处理
        return new Rectangle(x, y, w, h);
    }
    /**
     * @说明 碰撞方法
     * 一个是this对象，一个是传入值org
     * @return boolean 返回true说明有碰撞  返回false说明没有碰撞
     */
    public boolean pk(ElementOrigin org) {

        return this.getRectangle().intersects(org.getRectangle());
    }

    /**
     * @说明 墙壁重合检测方法 用来判断是否与墙壁发生重合的方法
     * @return
     */
    public boolean isCollidingWithWall() {
        ElementManager em = ElementManager.getManager();
        List<ElementOrigin> walls = em.getElementsByKey(GameElement.MAPS);
        Rectangle thisRect = this.getRectangle();

        for(ElementOrigin wall : walls) {
            if(thisRect.intersects(wall.getRectangle())) {return true;}
        }

        return false;
    }

    /**
     * @说明 边界检测方法 用于判断元素是否出边界
     */
    public boolean isOutOfBounds() {
        int gameWidth = GameJFrame.GameX;
        int gameHeight = GameJFrame.GameY;
        return getX() < 0 || getY() < 0 || getX()+getW() > gameWidth || getY()+getH()+30 > gameHeight;
    }


    /**
     * 只要是VO类，就要为属性生成getter和setter方法
     */

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void reduceHP(int damage) {
        this.setHp(this.getHp() - damage);
        if(this.getHp() < 0) {
            this.setHp(0);
            setLive(false);
            this.die();
        }
    }
}
