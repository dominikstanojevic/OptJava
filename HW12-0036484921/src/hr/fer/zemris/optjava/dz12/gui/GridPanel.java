package hr.fer.zemris.optjava.dz12.gui;

import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.models.nodes.Action;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Dominik on 14.2.2017..
 */
public class GridPanel extends JPanel {
    private static final long serialVersionUID = -4489366051578664933L;

    private Grid template;
    private Grid grid;

    private BufferedImage ant;

    public GridPanel(Grid template, int moves) {
        this.template = template;
        grid = Grid.copy(template);
        grid.reset(moves);

        try {
            ant = ImageIO.read(this.getClass().getClassLoader().getResource("ant.png"));
        } catch (IOException e) {
            System.err.println("Ant image is missing.");
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        boolean[][] food = grid.getFood();

        int cellWidth = width / food[0].length;
        int cellHeight = height / food.length;

        int xOffset = (width - (food[0].length * cellWidth)) / 2;
        int yOffset = (height - (food.length * cellHeight)) / 2;

        for (int y = 0; y < food.length; y++) {
            for (int x = 0; x < food[0].length; x++) {
                Rectangle cell =
                        new Rectangle(xOffset + (x * cellWidth), yOffset + (y * cellHeight), cellWidth, cellHeight);

                if (grid.getPosition().x == x && grid.getPosition().y == y) {
                    AffineTransform tx = AffineTransform
                            .getRotateInstance(getRotation(grid.getPosition().direction), ant.getWidth() / 2,
                                    ant.getHeight() / 2);
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                    g2d.drawImage(op.filter(ant, null), xOffset + (x * cellWidth), yOffset + (y * cellHeight),
                            xOffset + (x * cellWidth) + cellWidth, yOffset + (y * cellHeight) + cellHeight, 0, 0,
                            ant.getWidth(), ant.getHeight(), null);
                }

                if (food[y][x]) {
                    g2d.setColor(Color.BLUE);
                    g2d.fill(cell);
                }

                g2d.setColor(Color.GRAY);
                g2d.draw(cell);
            }
        }

        g2d.dispose();
    }

    public void reset(int moves) {
        Grid.copy(template, grid);
        grid.reset(moves);
        revalidate();
        repaint();
    }

    public void next(Action action) {
        grid.executeAction(action);
        revalidate();
        repaint();
    }

    private double getRotation(Grid.Position.Direction direction) {
        switch (direction) {
            case RIGHT:
                return Math.toRadians(90);
            case DOWN:
                return Math.toRadians(180);
            case LEFT:
                return Math.toRadians(270);
            case UP:
                return 0;
            default:
                throw new RuntimeException();
        }
    }
}
