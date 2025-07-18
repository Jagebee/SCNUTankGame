package com.tedu.show;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class SplashScreen {
    private JWindow splashWindow;

    public void showSplash() {
        // 使用更可靠的路径获取方式
        String projectRoot = System.getProperty("user.dir");
        String imagePath = projectRoot + File.separator + "image" + File.separator + "icon.png";

        // 打印调试信息
        System.out.println("尝试加载启动图片: " + imagePath);

        // 创建无边框窗口
        splashWindow = new JWindow();
        splashWindow.setSize(800, 600);
        splashWindow.setLocationRelativeTo(null); // 居中显示

        try {
            // 检查图片文件是否存在
            File imageFile = new File(imagePath);
            if (!imageFile.exists() || !imageFile.isFile()) {
                throw new RuntimeException("图片文件不存在: " + imagePath);
            }

            // 使用ImageIO加载图片（更可靠的方式）
            Image originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new RuntimeException("图片加载失败，可能是不支持的格式或损坏文件");
            }

            // 创建自适应面板
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // 缩放图片以适应面板大小
                    g.drawImage(originalImage, 0, 0, getWidth(), getHeight(), this);
                }
            };

            splashWindow.add(panel);
            splashWindow.setVisible(true);

            // 显示3秒
            Thread.sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
            // 图片加载失败时显示错误信息
            JLabel errorLabel = new JLabel("<html><center>启动图片加载失败!<br>" + e.getMessage() + "</center></html>",
                    JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            splashWindow.add(errorLabel);
            splashWindow.setVisible(true);

            try {
                Thread.sleep(3000); // 仍然显示3秒错误信息
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            splashWindow.dispose(); // 确保窗口被关闭
        }
    }
}
