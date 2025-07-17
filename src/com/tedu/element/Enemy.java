package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Enemy extends ElementOrigin{
    private static final int moveSpeed = 1; //移动速度
    private int startX, startY; //开始位置
    private int endX, endY; //结束位置
    private int currentX, currentY; //当前位置
    private boolean movingToEnd;//是否正在向结束位置移动

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    private String fx;

    private long lastFireTime;//记录上次发射的时间
    private long fireInterval = 1000;//发射子弹的时间间隔
    private ElementManager em;

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
    }

//    @Override
//    public ElementOrigin createElement(String str) {
//        Random ran=new Random();
//        int x=ran.nextInt(800);
//        int y=ran.nextInt(500);
//        this.setX(x);
//        this.setY(y);
//        this.setW(50);
//        this.setH(50);
//        this.setIcon(new ImageIcon("image/tank/bot/bot_up.png"));
//        return this;
//    }

    @Override
    public ElementOrigin createElement(String str) {
        //解析配置字符串 每关单独给出 格式为“startX,startY,endX,endY,fx”
        String[] parts = str.split(",");
        if(parts.length == 5) {
            try {
                startX = Integer.parseInt(parts[0]);
                startY = Integer.parseInt(parts[1]);
                endX = Integer.parseInt(parts[2]);
                endY = Integer.parseInt(parts[3]);
                fx = parts[4];

                this.setX(startX);
                this.setY(startY);
                this.setW(50);
                this.setH(50);
                this.setIcon(GameLoad.enemyImgMap.get(fx));

                currentX = startX;
                currentY = startY;
                movingToEnd = true;

                return this;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        this.setX(100);
        this.setY(100);
        this.setW(50);
        this.setH(50);
        this.setIcon(new ImageIcon("image/tank/bot/bot_up.png"));

        return this;
    }

    @Override
    protected void move() {
        if(movingToEnd) {
            if(currentX < endX) {currentX += moveSpeed;fx = "right";}
            else if(currentX > endX) {currentX -= moveSpeed;fx = "left";}

            if(currentY < endY) {currentY += moveSpeed;fx = "down";}
            else if(currentY > endY) {currentY -= moveSpeed;fx = "up";}

            if(currentX == endX && currentY == endY) {
                movingToEnd = false;
            }
        } else {
            if(currentX < startX) {currentX += moveSpeed;fx = "right";}
            else if(currentX > startX) {currentX -= moveSpeed;fx = "left";}

            if(currentY < startY) {currentY += moveSpeed;fx = "down";}
            else if(currentY > startY) {currentY -= moveSpeed;fx = "up";}

            if(currentX == startX && currentY == startY) {
                movingToEnd = true;
            }
        }

        this.setX(currentX);
        this.setY(currentY);

        updateImage(System.currentTimeMillis());
    }

    protected void updateImage(long gameTime) {
        this.setIcon(GameLoad.enemyImgMap.get(fx));
    }

    @Override
    protected void add(long gameTime) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastFireTime >= fireInterval) {
            ElementOrigin element = new PlayFile().createElement(getBulletCreationString());
            if(element instanceof PlayFile) {
                ((PlayFile)element).setAttack(20);
            }
            ElementManager.getManager().addElement(element,GameElement.PLAYFILE);
            lastFireTime = currentTime;
        }
    }

    private String getBulletCreationString() {
        int x = this.getX();
        int y = this.getY();
        switch (this.fx) {
            case "left": x -= 10;y += 20;break;
            case "right": x += 50;y += 20;break;
            case "up": x += 20;y -= 10;break;
            case "down": x += 20;y += 50;break;
        }

        return "x:" + x + ",y:" + y + ",f:" + this.fx;
    }

}

