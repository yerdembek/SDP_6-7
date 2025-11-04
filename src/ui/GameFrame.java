package ui;

import service.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameFrame extends JFrame implements GameViewListener {

    private final GameController controller;
    private final GamePanel gamePanel;

    private BufferedImage stayImage, moveImage, attackImage, currentImage;

    private int playerX = 100;
    private int playerY = 300;
    private final int playerSpeed = 10;
    private final List<Arrow> arrows = new CopyOnWriteArrayList<>();

    private static final int PLAYER_WIDTH = 80;
    private static final int PLAYER_HEIGHT = 80;

    private boolean isMoving = false;
    private Timer attackAnimationTimer;
    private Timer shockwaveTimer;

    private int shockwaveDistance = 0;
    private final int stripeWidth = 15;

    public GameFrame(GameController controller) {
        this.controller = controller;
        loadResources();
        this.currentImage = stayImage;
        this.gamePanel = new GamePanel();
        setupFrame();
        setupTimers();
        setupListeners();
        startAnimation();
    }

    @Override
    public void onShowShockwave() {
        shockwaveDistance = 0;
        shockwaveTimer.restart();
    }

    @Override
    public void onVillainAttackReceived(int damage) {
        int attackZoneStart = getWidth() / 2;
        if (playerX + PLAYER_WIDTH > attackZoneStart) {
            controller.getHero().takeDamage(damage);
            System.out.println("You are in attack zone. Taken " + damage + " damage. HP left: " + controller.getHero().getHealth());
        }
        controller.reportCurrentHealth();
    }

    private BufferedImage loadImage(String path) throws IOException {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            throw new IOException("Resource not found: " + path);
        }
        return ImageIO.read(imageUrl);
    }

    private void loadResources() {
        try {
            stayImage = loadImage("/resourses/archer_stay.png");
            moveImage = loadImage("/resourses/archer_move.png");
            attackImage = loadImage("/resourses/archer_attack.png");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Не удалось загрузить скины персонажа: " + e.getMessage(),
                    "Ошибка ресурсов",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void setupFrame() {
        setTitle("Archer Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        add(gamePanel);
        setVisible(true);
    }

    private void setupTimers() {
        attackAnimationTimer = new Timer(400, e -> updatePlayerImage());
        attackAnimationTimer.setRepeats(false);

        shockwaveTimer = new Timer(16, e -> {
            shockwaveDistance += 25;
            if (shockwaveDistance > getWidth() / 2 + 100) {
                shockwaveTimer.stop();
            }
        });
    }

    private void setupListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { handleKeyPress(e.getKeyCode()); }
            @Override
            public void keyReleased(KeyEvent e) { handleKeyRelease(e.getKeyCode()); }
        });
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
                isMoving = true;
                playerX -= playerSpeed;
                break;
            case KeyEvent.VK_D:
                isMoving = true;
                playerX += playerSpeed;
                break;
            case KeyEvent.VK_SPACE:
                fireArrow();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
        updatePlayerImage();
    }

    private void handleKeyRelease(int keyCode) {
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
            isMoving = false;
            updatePlayerImage();
        }
    }

    private void updatePlayerImage() {
        if (attackAnimationTimer.isRunning()) {
            currentImage = attackImage;
        } else if (isMoving) {
            currentImage = moveImage;
        } else {
            currentImage = stayImage;
        }
    }

    private void fireArrow() {
        int attackZoneStart = getWidth() / 2;
        if (playerX + PLAYER_WIDTH > attackZoneStart) {
            if (!attackAnimationTimer.isRunning()) {
                controller.onAttack();
                int arrowStartX = playerX + PLAYER_WIDTH;
                int arrowStartY = playerY + PLAYER_HEIGHT / 2;
                arrows.add(new Arrow(arrowStartX, arrowStartY, 15));
                attackAnimationTimer.start();
                updatePlayerImage();
            }
        }
    }

    private void startAnimation() {
        Timer gameLoop = new Timer(16, e -> {
            updateArrows();
            gamePanel.repaint();
        });
        gameLoop.start();
    }

    private void updateArrows() {
        for (Arrow arrow : arrows) {
            arrow.move();
            if (arrow.getX() > getWidth()) {
                arrows.remove(arrow);
            }
        }
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int midPoint = getWidth() / 2;
            g2d.setColor(new Color(135, 206, 235));
            g2d.fillRect(0, 0, midPoint, getHeight());
            g2d.setColor(new Color(255, 105, 97));
            g2d.fillRect(midPoint, 0, getWidth() - midPoint, getHeight());
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(midPoint, 0, midPoint, getHeight());

            if (currentImage != null) {
                g2d.drawImage(currentImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, null);
            }

            for (Arrow arrow : arrows) {
                arrow.draw(g2d);
            }

            if (shockwaveTimer.isRunning()) {
                drawShockwave(g2d);
            }
        }

        private void drawShockwave(Graphics2D g2d) {
            g2d.setColor(new Color(220, 20, 60));

            // Центр атаки - середина правой (красной) зоны
            int originX = getWidth()+100;

            int leftStripeX = originX - shockwaveDistance;

            g2d.fillRect(leftStripeX - stripeWidth / 2, 0, stripeWidth, getHeight());}
    }
}