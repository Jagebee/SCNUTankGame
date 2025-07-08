package com.tedu.element;

import javax.swing.*;
import java.awt.*;

/**
 * @说明 玩家子弹类，本类的实体对象是由玩家对象调用和创建
 * @author mooken
 * @子类的开发步骤：
 * 1.继承元素基类；重写show方法
 * 2.按照需求选择性重写其他方法，例如：move等
 * 3.思考并定义子类特有的属性
 */
public class PlayFile extends ElementOrigin{
    private int attack = 1;//攻击力
    private int moveNum = 10;//移动速度
    private String fx;
//    剩下的大家扩展

    public PlayFile() {}

//  对创建这个对象的过程进行封装，外界只需要传输必要的约定参数，返回值就是对象实体
    @Override
    public  ElementOrigin createElement(String str) {//定义字符串的规则
        String[] split = str.split(",");
        for(String str1 : split){
            String[] split2 = str1.split(":");
            switch (split2[0]) {
                case "x":this.setX(Integer.parseInt(split2[1]));break;
                case "y":this.setY(Integer.parseInt(split2[1]));break;
                case "f":this.fx = split2[1];break;
            }
        }
        this.setW(10);
        this.setH(10);
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.setColor(Color.red);//常用色可使用
//        switch(this.fx) {
//            case "up": x += 20;y -= 10;break;
//            case "down": x += 20;y += 50;break;
//            case "left": x -= 10;y += 20;break;
//            case "right": x += 50;y += 20;break;
//        }
        g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
    }

    @Override
    protected void move() {
        if(this.getX() < 0 || this.getX() > 800 || this.getY() < 0 || this.getY() > 600){
            this.setLive(false);
            return ;
        }
        switch (this.fx) {
            case "up":this.setY(this.getY()-this.moveNum);break;
            case "down":this.setY(this.getY()+this.moveNum);break;
            case "left":this.setX(this.getX()-this.moveNum);break;
            case "right":this.setX(this.getX()+this.moveNum);break;
        }
    }
    /**
     * 对于子弹来说：1.出边界 2.碰撞 3.玩家放保险
     * 处理方式就是，当达到死亡的条件时，值进行修改死亡状态的操作
     */

    /**子弹变装*/
//    private long time = 0;
//    protected void updateImage(long gameTime) {
//        if(gameTime - time > 5) {
//            time = gameTime;
//            this.setW(this.getW()+5);
//            this.setH(this.getH()+5);
//        }
//    }
}
