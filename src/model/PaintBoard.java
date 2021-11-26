package model;

import java.io.Serializable;
import java.util.Date;

public class PaintBoard implements Serializable {
    int[][] data;
    Date updatedAt;

    public PaintBoard() {
        data = new int[50][50];
        updatedAt = new Date();
    }

    public void setPixel(int col, int row, int color) {
        data[col][row] = color;
        updatedAt = new Date();
    }

    public void setData(int[][] data) {
        this.data = data;
    }

    public int[][] getData() {
        return data;
    }

    public void clear() {
        data = new int[50][50];
    }
}
