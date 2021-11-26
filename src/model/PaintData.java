package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Delta paint data update
 */

public class PaintData implements Serializable {
    int col;
    int row;
    int color;
    Date updatedAt;

    public PaintData(int col, int row, int color) {
        this.col = col;
        this.row = row;
        this.color = color;
        updatedAt = new Date();
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getColor() {
        return color;
    }
}
