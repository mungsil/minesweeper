package com.mungsil.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("AppCompatCustomView")
public class BlockButton extends Button {
    private int x;
    private int y;
    private boolean mine;
    private boolean flag;
    int neighborMines;
    static int flags=10;
    static int blocks=0;

    public BlockButton(Context context, int x, int y) {
        super(context);
        mine = false;
        flag = false;
        neighborMines = 0;
        this.x = x;
        this.y = y;

        //총 블록 수 증가
        blocks = blocks + 1;

        TableRow.LayoutParams layoutParams =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);

        super.setLayoutParams(layoutParams);
    }

    public void toggleFlag() {

           if (flag == false) {
            if (flags > 0) {
                flag = true;
                flags -= 1;
                blocks--;
                setText("+");

            }

        }else {
            flag = false;
            flags += 1;
            blocks++;
            setText("");

        }
    }

    public boolean breakBlock() {
        this.setEnabled(false);
        blocks--;
        if (mine == true) {
            setText("*");
            return true;
        }else {
            setText(neighborMines+"");
            return false;
        }
    }

    //setter
    public void setMine(boolean mine) {
        this.mine = mine;
    }
    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }

    //getter
    public int getBlock_X() {
        return this.x;
    }
    public int getBlock_Y() {
        return this.y;
    }
    public boolean isFlag() {
        return flag;
    }
    public int getNeighborMines() {
        return neighborMines;
    }
}
