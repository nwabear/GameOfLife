package com.nwabear.cgol;

public class Cell {
    private boolean alive;
    private boolean aliveNext;
    private CGOLSurface surface;
    private int posX;
    private int posY;

    public Cell(int x, int y, CGOLSurface surface) {
        this.alive = false;
        this.aliveNext = false;

        this.posX = x;
        this.posY = y;

        this.surface = surface;
    }

    public int getNeighbors() {
        int neighbors = 0;
        Cell[][] cells = this.surface.getCells();
        for(int x = posX - 1; x <= posX + 1; x++) {
            for(int y = posY - 1; y <= posY + 1; y++) {
                if(x != posX || y != posY) {
                    try {
                        if(cells[x][y].isAlive()) {
                            neighbors++;
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {
                        // cell is on edge, assume the outside is always dead
                    }
                }
            }
        }
        return neighbors;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void nextGen() {
        switch(this.getNeighbors()) {
            case 2:
                this.aliveNext = this.alive;
                break;

            case 3:
                this.aliveNext = true;
                break;

            default:
                this.aliveNext = false;
                break;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isAliveNext() {
        return aliveNext;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }
}
