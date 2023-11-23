package com.mungsil.minesweeper;

import static com.google.android.material.internal.ViewUtils.getContentView;
import static com.mungsil.minesweeper.BlockButton.blocks;
import static com.mungsil.minesweeper.R.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    static int mine = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //지뢰 랜덤 배치

        int[][] map = new int[9][9];
        int[] nx = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] ny = {0, 1, 1, 1, 0, -1, -1, -1};

        while (mine < 10) {
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);
            if (map[y][x] == -1) {
                continue;
            } else {
                map[y][x] = -1; //지뢰 표시

                for (int i = 0; i < 8; i++) {
                    int neighborX = x + nx[i];
                    int neighborY = y + ny[i];

                    if ( neighborX>= 0 && neighborX < 9 && neighborY >= 0 && neighborY < 9 && map[neighborY][neighborX] != -1) {
                        map[neighborY][neighborX] += 1;
                    }
                }
            }
            mine++;
        }

        TextView mineText = findViewById(id.mineNumText);
        mineText.setText("10");

        // 동적으로 버튼 생성
        TableLayout tableLayout = (TableLayout) findViewById(id.tableLayout);
        TableRow tableRow[]=new TableRow[9];
        Button[][] buttons = new BlockButton[9][9];

        for (int i = 0; i < 9; i++) {
            tableRow[i] = new TableRow(this);
                        tableLayout.addView(tableRow[i]);
                        ToggleButton toggleBtn = findViewById(id.toggleButton);

                        for (int j = 0; j < 9; j++) {
                            buttons[i][j] = new BlockButton(this, i, j);
                            tableRow[i].addView(buttons[i][j]);
                            if (map[i][j] == -1) {
                                ((BlockButton)buttons[i][j]).setMine(true);
                            } else {
                                ((BlockButton)buttons[i][j]).setNeighborMines(map[i][j]);
                            }
                            buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (toggleBtn.isChecked()) {
                                        ((BlockButton)view).toggleFlag();
                                    }

                                    // 블록 열기
                                    else if(!toggleBtn.isChecked()) {

                                        if (!((BlockButton) view).isFlag()) {
                                            boolean isMine = ((BlockButton) view).breakBlock();

                                            if (isMine) {
                                                for (int i = 0; i < 9; i++) {
                                                    for (int j = 0; j < 9; j++) {
                                                        ((BlockButton)buttons[i][j]).breakBlock();
                                                        buttons[i][j].setEnabled(false);
                                                    }
                                                }
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                                dialog.setTitle("게임종료");
                                                dialog.setMessage("지뢰를 발견하였습니다");
                                                dialog.show();

                                            } else {

                                                int neighborMines = ((BlockButton) view).getNeighborMines();

                                                // 주변 지뢰 수가 =0인 블록이 있을 경우 해당 블록의 주변 블록 열기
                                                if (neighborMines == 0) {

                                                    int x = ((BlockButton) view).getBlock_X();
                                                    int y = ((BlockButton) view).getBlock_Y();

                                                    recursionBreak(x,y);
                                                }
                                            }
                                        }
                                    }
                                    //남은 블록이 없으면 WIN
                                    if (blocks == 0) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                        dialog.setTitle("우승");
                                        dialog.setMessage("게임 이겼어요");
                                        dialog.show();
                                    }

                            }


                                public void recursionBreak(int x, int y) {
                                        for (int i = 0; i < 8; i++) {
                                            int neighborX = x - nx[i];
                                            int neighborY = y - ny[i];

                                            if (neighborX >= 0 && neighborX < 9 && neighborY >= 0 && neighborY < 9) {
                                                if (((BlockButton) buttons[neighborX][neighborY]).isEnabled() == true && ((BlockButton) buttons[neighborX][neighborY]).isFlag() == false) {
                                                    ((BlockButton) buttons[neighborX][neighborY]).breakBlock();
                                                    int mines = ((BlockButton) buttons[neighborX][neighborY]).getNeighborMines();
                                                    if (mines == 0) {
                                                        recursionBreak(neighborX, neighborY);
                                                    }
                                                }
                                            }
                                    }
                                }
                            });

            }
        }
    }
}