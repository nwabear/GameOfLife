package com.nwabear.cgol;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CGOLSurface extends JPanel {
    private CGOLFrame frame;
    private Cell[][] cells;
    private boolean drawGrid = false;

    public CGOLSurface(CGOLFrame frame) {
        this.frame = frame;

        cells = new Cell[AppContext.COLS][AppContext.ROWS];

        for(int x = 0; x < AppContext.COLS; x++) {
            for(int y = 0; y < AppContext.ROWS; y++) {
                cells[x][y] = new Cell(x, y, this);
            }
        }

        try {
            this.readFile();
        } catch(Exception e) {
            System.out.println("[ERROR] Failed to Load File");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D) (graphics);
        g2d.setFont(new Font("font", Font.PLAIN, 64));

        if(AppContext.WIDTH < AppContext.COLS) {
            BufferedImage bi = new BufferedImage(AppContext.COLS, AppContext.ROWS, BufferedImage.TYPE_3BYTE_BGR);

            Arrays.stream(this.cells)
                    .forEach(cells1 -> Arrays.stream(cells1)
                        .filter(Cell::isAlive)
                        .forEach(cell -> bi.setRGB(
                                cell.getPosX(),
                                cell.getPosY(),
                                Color.WHITE.getRGB())));

            Image temp = bi.getScaledInstance(AppContext.WIDTH, AppContext.HEIGHT, Image.SCALE_AREA_AVERAGING);
            g2d.drawImage(temp, 0, 0, null);
        } else {
            int rectWidth = AppContext.WIDTH / AppContext.COLS;
            int rectHeight = AppContext.HEIGHT / AppContext.ROWS;
            for (int x = 0; x < AppContext.COLS; x++) {
                for (int y = 0; y < AppContext.ROWS; y++) {
                    if(this.cells[x][y].isAlive()) {
                        g2d.setColor(Color.WHITE);
                    } else {
                        g2d.setColor(Color.BLACK);
                    }
                    g2d.fillRect(x * rectWidth, y * rectHeight, rectWidth, rectHeight);
                }
            }
        }

        if(this.drawGrid) {
            int rectWidth = AppContext.WIDTH / AppContext.COLS;
            int rectHeight = AppContext.HEIGHT / AppContext.ROWS;
            g2d.setColor(Color.DARK_GRAY);
            for(int x = 0; x < AppContext.COLS; x++) {
                for(int y = 0; y < AppContext.ROWS; y++) {
                    g2d.drawRect(x * rectWidth, y * rectHeight, rectWidth, rectHeight);
                }
            }
        }
    }

    private void readFile() throws Exception {
        File toRead = new File("start.txt");
        if(toRead.exists()) {
            Scanner scan = new Scanner(toRead);
            int w = Integer.parseInt(scan.nextLine());
            int h = Integer.parseInt(scan.nextLine());

            int startX = 10;

            int x = startX;
            int y = (AppContext.ROWS / 2) - (h / 2);

            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                for(char c : line.toCharArray()) {
                    if(c != '.') {
                        this.cells[x][y].setAlive(true);
                    }
                    x++;
                }
                x = startX;
                y++;
            }
        }
        repaint();
    }

    private void calcNextTick() {
        Arrays.stream(this.cells)
                .forEach(cells -> Arrays.stream(cells)
                .forEach(Cell::nextGen));

        Arrays.stream(this.cells)
                .forEach(cells -> Arrays.stream(cells)
                .forEach(cell -> cell.setAlive(cell.isAliveNext())));
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public void randomize() {
        Random rand = new Random();

        for(int x = 0; x < AppContext.COLS; x++) {
            for(int y = 0; y < AppContext.ROWS; y++) {
                this.cells[x][y].setAlive(false);
                if(rand.nextDouble() < AppContext.RANDOM_CHANCE) {
                    this.cells[x][y].setAlive(true);
                }
            }
        }
        this.repaint();
    }

    public void clear() {
        for(int x = 0; x < AppContext.COLS; x++) {
            for(int y = 0; y < AppContext.ROWS; y++) {
                this.cells[x][y].setAlive(false);
            }
        }
        this.repaint();
    }

    public void tick() {
        if(!this.frame.wasLeftClick()) {
            for(int i = 0; i < this.frame.getDraws(); i++) {
                this.calcNextTick();
            }
        } else {
            Point p = this.frame.getPoint();
            this.cells[this.getLocX(p.x)][this.getLocY(p.y)].setAlive(!this.cells[this.getLocX(p.x)][this.getLocY(p.y)].isAlive());
        }
        this.repaint();
    }

    private int getLocX(int x) {
        return  x / (AppContext.WIDTH / AppContext.COLS);
    }

    private int getLocY(int y) {
        return y / (AppContext.HEIGHT / AppContext.ROWS);
    }

    public void toggleGrid() {
        this.drawGrid = !this.drawGrid;
    }
}
