package com.mungsil.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

@SuppressLint("AppCompatCustomView")
public class BlockButton extends Button {


    private int x;
    private int y;
    //지뢰니? true or false로 대답하렴.
    private boolean mine;
    //flag가 꽃혔니? true or false로 대답하렴.
    private boolean flag;
    int neighborMines;
    static int flags=10;
    static int blocks=81;
    

    //레이아웃 생성자 안으로 이동
    public BlockButton(Context context, int x, int y) {
        //매개변수, 부모의 생성자 호출, 필드 초기화
        super(context);
        mine = false;
        flag = false;
        neighborMines = 0;
        this.x = x;
        this.y = y;

        //남은 블록 수 증가
        blocks = blocks + 1;

        //LayoutParameter 설정
        TableRow.LayoutParams layoutParams =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);

        super.setLayoutParams(layoutParams);
    }

    // 깃발 꽃기 , 해제 메소드
    //버튼안에 글자넣기: setText
    public void toggleFlag() {
        if (flag == false) {
            if (flags > 0) {
                flag = true;
                flags -= 1;
                setText("+");
            }

        }else {
            flag = false;
            flags += 1;
            setText("");
        }
    }

    // 블록을 여는 메소드
    public boolean breakBlock() {
        this.setEnabled(false);
        System.out.println("블록 열어!");
        blocks--;
        if (mine == true) {
            setText("*");
            return true;
        }else {
            setText(neighborMines+"");
            //열린 블록으로 표시하라는데? 근데 그게 머임;
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
    public boolean getMine() {
        return this.mine;
    }
    public boolean isFlag() {
        return flag;
    }
    public int getNeighborMines() {
        return neighborMines;
    }
}
