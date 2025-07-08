package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public class Maps extends ElementOrigin{

    private int hp;
    private String name;//墙的type 也可以使用枚举

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(),this.getW(),this.getH(), null);
    }

    @Override
    public ElementOrigin createElement(String str) {
        String []arr = str.split(",");
        //先写一个假图片
        ImageIcon icon = null;
        switch (arr[0]) {//设置图片信息 图片还未加载到内存中
            case "GRASS":icon = new ImageIcon("image/wall/grass.png"); break;
            case "BRICK":icon = new ImageIcon("image/wall/brick.png"); break;
            case "RIVER":icon = new ImageIcon("image/wall/river.png"); break;
            case "IRON":icon = new ImageIcon("image/wall/iron.png");this.hp = 4; name = "IRON";break;
        }
        int x = Integer.parseInt(arr[1]);
        int y = Integer.parseInt(arr[2]);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        this.setH(h);
        this.setW(w);
        this.setX(x);
        this.setY(y);
        this.setIcon(icon);

        return this;
    }

    @Override//说明 这个设置扣血的方法需要自己思考 重新编写
    public void setLive(boolean live) {
        if("IRON".equals(name)) {//钢墙需要四下
            this.hp--;
            if(this.hp > 0) {
                return;
            }
        }
        super.setLive(live);
    }
}
