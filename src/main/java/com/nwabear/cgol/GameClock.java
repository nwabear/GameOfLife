package com.nwabear.cgol;

public class GameClock implements Runnable {
    private CGOLSurface surface;
    private CGOLFrame frame;
    private boolean isRunning = false;

    public GameClock(CGOLSurface surface, CGOLFrame frame) {
        this.surface = surface;
        this.frame = frame;
    }

    @Override
    public void run() {
        while(true) {
            while(this.isRunning) {
                this.surface.tick();
                try {
                    Thread.sleep(1000 / this.frame.getCurFPS());
                } catch (Exception e) {
                    System.out.println("[ERROR] Error Occured in Clock Active Loop: " + e);
                }
            }
            try {
                Thread.sleep(1000 / this.frame.getCurFPS());
            } catch(Exception e) {
                System.out.println("[ERROR] Error Occurred in Clock Inactive Loop: " + e);
            }
        }
    }

    public void toggleRunning() {
        this.isRunning = !this.isRunning;
    }
}
