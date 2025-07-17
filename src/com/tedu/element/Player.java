package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Player extends ElementOrigin{
    /**
     * 移动属性：
     * 1.单属性 配合方向枚举类型使用；一次只能移动一个方向
     * 2.双属性 上下和左右 配合boolean值使用 例如：true代表上 false为下。。需要另外一个变量确定是否按下方向键
     * 约定 0代表不动 1代表上 2代表下
     * 3.4属性 上下左右都可以 boolean配合使用 true代表移动 false代表不移动 同时按上和下怎么办？ 后按的会重制先按的
     *
     * 说明：以上3种方式 只是代码编写和判定方式不一样
     * 说明：游戏中非常多的判定，建议灵活使用判定属性；很多状态值也使用判定属性 多状态可以使用map<泛型，boolean>;set<判定对象> 判定对象中有时间
     *
     *
     * @问题 1.图片要读取到内存中：加载器 临时处理方式，手动编写存储到内存中
     *      2.什么时候进行修改图片（因为图片是在父类中属性存储）
     *      3.图片应该使用什么集合进行存储
     */

    private boolean left = false;//左
    private boolean right = false;//右
    private boolean up = false;//上
    private boolean down = false;//下

    private static final ImageIcon EXPLOSIN_ICON = new ImageIcon("image/boom/boom.png");

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }


//  变量专门用来记录当前主角面向的方向，默认为up
    private String fx = "up";
    private boolean pkType = false;//攻击状态


    /**
     * 面向对象中第一个思想，对象自己的事情自己做
     */
    public Player(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }

    /**
     * @说明 重写方法：重写的要求：方法名称和参数类型序列必须和父类的一样
     * @重点 监听的数据需要改变状态值
     */
    @Override
    public void keyClick(boolean b1,int key) {
        if(b1) {
            switch (key) {
                case 32:
                    this.pkType = true;break;//开启攻击状态
                case 37:
                    this.up = false;this.down = false;
                    this.right = false;this.left = true;this.fx = "left";
                    break;
                case 38:
                    this.right = false;this.left = false;
                    this.down = false;this.up = true;this.fx = "up";break;
                case 39:
                    this.down = false;this.up = false;
                    this.left = false;this.right = true;this.fx = "right";break;
                case 40:
                    this.right = false;this.left = false;
                    this.up = false;this.down = true;this.fx = "down";break;
            }
        }else {
            switch (key) {
                case 32:this.pkType = false;break;
                case 37:this.left = false;break;
                case 38:this.up = false;break;
                case 39:this.right = false;break;
                case 40:this.down = false;break;
            }
        }
    }

    @Override
    public void move() {

        int newX = this.getX();
        int newY = this.getY();

        if(this.left && this.getX() > 0) {
            newX -= 5;
        }
        if(this.right && this.getX() < 800-this.getW()) {
            newX += 5;
        }
        if(this.up && this.getY() > 0) {
            newY -= 5;
        }
        if(this.down && this.getY() < 600-this.getH()-30) {
            newY += 5;
        }

        if(!isCollidingWithWall(newX,newY)) {
            setX(newX);
            setY(newY);
        }
    }

    private boolean isCollidingWithWall(int x,int y) {
        ElementManager em = ElementManager.getManager();
        List<ElementOrigin> walls = em.getElementsByKey(GameElement.MAPS);

        Rectangle tankRect = new Rectangle(x, y, this.getW(), this.getH());
        for(ElementOrigin wall : walls) {
            if(tankRect.intersects(wall.getRectangle())) {
                return true;
            }
        }

        return false;
    }


    protected void updateImage(long gameTime) {
        this.setIcon(GameLoad.playerImgMap.get(fx));
    }

    /**
     * @额外问题：1.重写的方法的访问修饰符是否可以修改
     *          2.下面的add方法是否可以自动抛出异常
     * @重写规则：1.重写方法的方法名称和返回值必须和父类的一样
     *          2.重写的方法传入参数类型序列，必须和父类的一样
     *          3.重写的方法访问修饰符，只能比父类的更加宽松
     *          4.重写的方法抛出的异常不能比父类更加宽泛
     *
     * 子弹的添加需要的是发射者的坐标位置，发射者的方向，如果你可以变换子弹，怎么处理
     *
     */

    private long fileTime = 0;

    @Override //添加子弹
    public void add(long gameTime) {
        if(!this.pkType) {
            return ;
        }
        this.pkType = false;//按一次发射一个子弹
//      new PlayFile(); //构造一个类需要比较多的工作 可以使用小工厂 专门将构造对象的多个步骤进行封装称为一个方法，返回值直接是这个对象
//      传递一个固定格式 {x:3,y:5,f:up} json格式
        ElementOrigin element = new PlayFile().createElement(this.toString());//以后的框架学习中会碰到
//      会帮助你返回对象的实体，并初始化数据
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
//        如果要控制子弹速度等等，还需要代码编写
    }

    @Override
    public String toString() {
        int x = getX();
        int y = getY();
        switch (this.fx) {
            case "left":x -= 10;y += 20;break;
            case "right":x += 50;y += 20;break;
            case "up":x += 20;y -= 10;break;
            case "down":x += 20;y += 50;break;
        }
        return "x:" + x + ",y:" + y + ",f:" + this.fx;
    }

    @Override
    public void die() {
        super.die();
        setIcon(EXPLOSIN_ICON);
    }
//    public int getFileX() {
//        int x;
//        switch (this.fx) {
//            case "left":x = this.getX()-10;break;
//            case "right":x = this.getX()+50;break;
//            case "up":x = this.getX()+20;break;
//            case "down":x = this.getX()+20;break;
//        }
//        return 0;
//    }
//
//    public int getFileY() {
//        return 0;
//    }
}
