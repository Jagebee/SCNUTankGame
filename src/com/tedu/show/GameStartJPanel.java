package com.tedu.show;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameStartJPanel extends JPanel {
    private GameJFrame gameJFrame;

    public GameStartJPanel(GameJFrame gameJFrame) {
        this.gameJFrame = gameJFrame;
        init();
    }

    private void init() {
        setBackground(Color.BLACK);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击任意位置开始游戏
                startGame();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String startText = "点击开始游戏";
        int x = (getWidth() - g.getFontMetrics().stringWidth(startText)) / 2;
        int y = getHeight() / 2;
        g.drawString(startText, x, y);
    }

    private void startGame() {
        // 移除起始页面面板
        gameJFrame.getContentPane().remove(this);
        // 创建游戏主面板
        GameMainJPanel gameMainJPanel = new GameMainJPanel();
        gameJFrame.setjPanel(gameMainJPanel);
        gameJFrame.start();
    }
}
