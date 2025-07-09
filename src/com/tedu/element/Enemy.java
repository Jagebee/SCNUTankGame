package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Enemy extends ElementOrigin{
    private static final int moveSpeed = 1;
    private int startX, startY;
    private int endX, endY;
    private int currentX, currentY;
    private boolean movingToEnd;

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
        //解析配置字符串 每关单独给出 格式为“startX,startY,endX,endY”
        String[] parts = str.split(",");
        if(parts.length == 4) {
            try {
                startX = Integer.parseInt(parts[0]);
                startY = Integer.parseInt(parts[1]);
                endX = Integer.parseInt(parts[2]);
                endY = Integer.parseInt(parts[3]);

                this.setX(startX);
                this.setY(startY);
                this.setW(50);
                this.setH(50);
                this.setIcon(new ImageIcon("image/tank/bot/bot_up.png"));

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
            if(currentX < endX) currentX += moveSpeed;
            else if(currentX > endX) currentX -= moveSpeed;

            if(currentY < endY) currentY += moveSpeed;
            else if(currentY > endY) currentY -= moveSpeed;

            if(currentX == endX && currentY == endY) {
                movingToEnd = false;
            }
        } else {
            if(currentX < startX) currentX += moveSpeed;
            else if(currentX > startX) currentX -= moveSpeed;

            if(currentY < startY) currentY += moveSpeed;
            else if(currentY > startY) currentY -= moveSpeed;

            if(currentX == startX && currentY == startY) {
                movingToEnd = true;
            }
        }

        this.setX(currentX);
        this.setY(currentY);

    }
}

