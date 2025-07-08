package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.List;

public class Enemy extends ElementOrigin {
    private Random random = new Random();
    private long moveTime = 0;
    private long fireTime = 0;
    private final long MOVE_INTERVAL = 500;
    private final long FIRE_INTERVAL = 2000;
    private int direction = 0;

    public int getDirection() {
        return direction;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }

    @Override
    public ElementOrigin createElement(String str) {
        ElementManager em = ElementManager.getManager();
        List<ElementOrigin> walls = em.getElementsByKey(GameElement.MAPS);

        boolean validPosition = false;

        while (!validPosition) {
            int x = random.nextInt(800);
            int y = random.nextInt(600);
            int direction = random.nextInt(4);
            this.setX(x);
            this.setY(y);
            this.setW(50);
            this.setH(50);
            this.setDirection(direction);
            this.setIcon(new ImageIcon("image/tank/bot/bot_up.png"));
            validPosition = !isCollidingWithWall(x, y, walls);
        }
        return this;
    }

    @Override
    protected void move() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - moveTime > MOVE_INTERVAL) {
            moveTime = currentTime;
            int newX = this.getX();
            int newY = this.getY();
            int newDirection = random.nextInt(4);
            this.setDirection(newDirection);

            switch (this.direction) {
                case 0: newY -= 5;break;
                case 1: newY += 5;break;
                case 2: newX -= 5;break;
                case 3: newX += 5;break;
            }

            ElementManager em = ElementManager.getManager();
            List<ElementOrigin> walls = em.getElementsByKey(GameElement.MAPS);
            if(!isCollidingWithWall(newX,newY,walls)) {
                this.setX(newX);
                this.setY(newY);
            }
        }
    }

    @Override
    protected void add(long gameTime) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - fireTime > FIRE_INTERVAL) {
            fireTime = currentTime;
            ElementOrigin element = new PlayFile().createElement(this.toString());
            ElementManager.getManager().addElement(element,GameElement.PLAYFILE);
        }
    }

    @Override
    public String toString() {
        int x = this.getX();
        int y = this.getY();
        String fx = "";

        int direction = this.getDirection();
        switch (direction) {
            case 0:fx = "up";x += 20;y -= 10;break;
            case 1:fx = "down";x += 20;y += 50;break;
            case 2:fx = "left";x -= 10;y += 20;break;
            case 3:fx = "right";x += 50;y += 20;break;
        }
        return "x:" + x + " y:" + y + " fx:" + fx;
    }

    private boolean isCollidingWithWall(int x, int y, List<ElementOrigin> walls) {
        Rectangle enemyRect = new Rectangle(x,y,this.getW(),this.getH());
        for(ElementOrigin wall : walls) {
            if(enemyRect.intersects(wall.getRectangle())) {
                return true;
            }
        }
        return false;
    }
}
