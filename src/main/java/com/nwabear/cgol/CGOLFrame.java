package com.nwabear.cgol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CGOLFrame extends JFrame {
    private CGOLSurface surface;
    private GameClock gc;
    private Point point;
    private boolean wasLeftClick = false;
    private int curFPS;
    private int draws;

    public CGOLFrame() {
        this.initUI();
    }

    private void initUI() {
        this.surface = new CGOLSurface(this);

        this.add(this.surface);

        this.curFPS = AppContext.STARTING_FPS;
        this.draws = 1;
        this.gc = new GameClock(this.surface, this);
        Thread clock = new Thread(this.gc);
        clock.start();

        this.setTitle("Conway's Game of Life");

        int width = AppContext.WIDTH;
        int height = AppContext.HEIGHT;
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CGOLFrame.this.point = e.getPoint();
                if(e.getButton() == 1) {
                    CGOLFrame.this.wasLeftClick = true;
//                    System.out.println("[ACTION] Toggling Cell State at X: " + (e.getPoint().x / (AppContext.WIDTH / AppContext.COLS)) + ", Y: " + (e.getPoint().y / (AppContext.HEIGHT / AppContext.ROWS)));
                }
                CGOLFrame.this.surface.tick();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch(e.getExtendedKeyCode()) {
                    case 32: // space
                        CGOLFrame.this.gc.toggleRunning();
                        System.out.println("[ACTION] Toggling Game Clock");
                        break;

                    case 78: // n
                        CGOLFrame.this.surface.tick();
                        System.out.println("[ACTION] Advancing Board Manually");
                        break;

                    case 82: // r
                        CGOLFrame.this.surface.randomize();
                        System.out.println("[ACTION] Randomizing Cell States");
                        break;

                    case 67: // c
                        CGOLFrame.this.surface.clear();
                        System.out.println("[ACTION] Clearing Screen");
                        break;

                    case 61: // +
                        if(CGOLFrame.this.curFPS < 100) {
                            CGOLFrame.this.curFPS++;
                            System.out.println("[ACTION] Increasing Clock Speed to " + CGOLFrame.this.curFPS + " TPS");
                        } else {
                            System.out.println("[WARNING] Trying to Increase Clock Speed Above 100, Ignoring Action");
                        }
                        break;

                    case 45: // -
                        if(CGOLFrame.this.curFPS > 1) {
                            CGOLFrame.this.curFPS--;
                            System.out.println("[ACTION] Decreasing Clock Speed to " + CGOLFrame.this.curFPS + " TPS");
                        } else {
                            System.out.println("[WARNING] Trying to Decrease Clock Speed to Zero, Ignoring Action");
                        }
                        break;

                    case 47: // /
                        CGOLFrame.this.surface.toggleGrid();
                        CGOLFrame.this.surface.repaint();
                        System.out.println("[ACTION] Toggling Grid");
                        break;

                    case 91: // [
                        CGOLFrame.this.draws--;
                        System.out.println("[ACTION] Reducing Generations per Frame to " + CGOLFrame.this.draws);
                        break;

                    case 93: // ]
                        CGOLFrame.this.draws++;
                        System.out.println("[ACTION] Increasing Generations per Frame to " + CGOLFrame.this.draws);
                        break;

//                    default:
//                        System.out.println(e.getExtendedKeyCode());
//                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public int getCurFPS() {
        return this.curFPS;
    }

    public Point getPoint() {
        return this.point;
    }

    public int getDraws() {
        return this.draws;
    }

    public boolean wasLeftClick() {
        boolean was = this.wasLeftClick;
        this.wasLeftClick = false;
        return was;
    }

    public static void main(String[] args) {
        CGOLFrame cf = new CGOLFrame();
        EventQueue.invokeLater( () -> {
            cf.setVisible(true);
        } );
    }
}
