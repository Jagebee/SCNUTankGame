package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Enemy extends ElementOrigin{
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
        Random ran = new Random();
        int x,y;
        this.setW(50);
        this.setH(50);

        do {
            x = ran.nextInt(800);
            y = ran.nextInt(600);

            this.setX(x);
            this.setY(y);
        } while(this.isCollidingWithWall() || this.isOutOfBounds());

        this.setIcon(new ImageIcon("image/tank/bot/bot_up.png"));
        return this;

    }
}

