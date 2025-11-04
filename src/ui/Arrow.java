package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Arrow {
    private int x;
    private int y;
    private final int speed;
    private final Rectangle bounds;

    private static final int WIDTH = 20;
    private static final int HEIGHT = 5;

    public Arrow(int startX, int startY, int speed) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void move() {
        x += speed;
        bounds.setLocation(x, y);
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }
}