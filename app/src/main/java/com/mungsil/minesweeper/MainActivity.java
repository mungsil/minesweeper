package com.mungsil.minesweeper;

import static com.mungsil.minesweeper.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        TableLayout tableLayout = (TableLayout) findViewById(id.tableLayout);
        TextView mineText = findViewById(id.mineNumText);
        Button[][] buttons = new BlockButton[9][9];

        //

        int mine = 0;
        int width = 9;
        int height = 9;
        int[][] map = new int[width][height];

        while (mine < 10) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            if (map[y][x] == -1) { //지뢰 중복 시 재시도
                continue;
            } else {
                map[y][x] = -1; //지뢰 생성

                /**
                 * 지뢰 힌트 생성 시작
                 * */
                if ((x >= 0 && x <= width - 2) && (y >= 0 && y <= height)) {
                    if (map[y][x + 1] != -1) {
                        map[y][x + 1] += 1;  //center right
                    }
                }
                if ((x >= 1 && x <= width - 1) && (y >= 0 && y <= height - 1)) {
                    if (map[y][x - 1] != -1) {
                        map[y][x - 1] += 1;  // center left
                    }
                }
                if ((x >= 1 && x <= width - 1) && (y >= 1 && y <= height - 1)) {
                    if (map[y - 1][x - 1] != -1) {
                        map[y - 1][x - 1] += 1;  // top left
                    }
                }
                if ((x >= 0 && x <= width - 2) && (y >= 1 && y <= height - 1)) {
                    if (map[y - 1][x + 1] != -1) {
                        map[y - 1][x + 1] += 1;  // top right
                    }
                }
                if ((x >= 0 && x <= width - 1) && (y >= 1 && y <= height - 1)) {
                    if (map[y - 1][x] != -1) {
                        map[y - 1][x] += 1;  // top center
                    }
                }
                if ((x >= 0 && x <= width - 2) && (y >= 0 && y <= height - 2)) {
                    if (map[y + 1][x + 1] != -1) {
                        map[y + 1][x + 1] += 1; // bottom right
                    }
                }
                if ((x >= 1 && x <= width - 1) && (y >= 0 && y <= height - 2)) {
                    if (map[y + 1][x - 1] != -1) {
                        map[y + 1][x - 1] += 1; // bottom left
                    }
                }
                if ((x >= 0 && x <= width - 1) && (y >= 0 && y <= height - 2)) {
                    if (map[y + 1][x] != -1) {
                        map[y + 1][x] += 1; // bottom center
                    }
                }
                /**
                 * 지뢰 힌트 생성 끝
                 * */
            }
            mine++;
        }
        //
        mineText.setText("10");
        TableRow tableRow[]=new TableRow[9];
        for (int i = 0; i < 9; i++) {
            tableRow[i] = new TableRow(this);
                        tableLayout.addView(tableRow[i]);
                        ToggleButton toggleBtn = findViewById(id.toggleButton);

                        for (int j = 0; j < 9; j++) {
                            buttons[i][j] = new BlockButton(this, i, j);

                            tableRow[i].addView(buttons[i][j]);
                            if (map[i][j] == -1) {
                                ((BlockButton)buttons[i][j]).setMine(true);
                                System.out.println("buttons["+i+"]["+j+"] = "+ ((BlockButton)buttons[i][j]).getMine());
                            } else {
                                ((BlockButton)buttons[i][j]).setNeighborMines(map[i][j]);
                            }
                            buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 깃발 꽃기
                                if (toggleBtn.isChecked()) {
                                    ((BlockButton)view).toggleFlag();

                                }
                                // 블록 열기
                                else {
                                    if (!((BlockButton) view).isFlag()) {
                                        boolean isMine = ((BlockButton) view).breakBlock();

                                        if (isMine) {
                                            for (int i = 0; i < 9; i++) {
                                                for (int j = 0; j < 9; j++) {
                                                    ((BlockButton)buttons[i][j]).breakBlock();
                                                }
                                            }
                                            Toast.makeText(MainActivity.this, "게임 종료", Toast.LENGTH_SHORT).show();
                                        } else {
                                            int neighborMines = ((BlockButton) view).getNeighborMines();
                                            // 주변 지뢰 수가 >0 인 경우
                                            // 머 해줄게 있나? 그냥...

                                            // 주변 지뢰 수가 =0 인 경우 재귀 호출로 주변의 모든 블록 열기
                                            if (neighborMines == 0) {
                                                int[] nx = {-1, -1, 0, 1, 1, 1, 0, -1};
                                                int[] ny = {0, 1, 1, 1, 0, -1, -1, -1};
                                                int x = ((BlockButton) view).getBlock_X();
                                                int y = ((BlockButton) view).getBlock_Y();

                                                for (int i = 0; i < 8; i++) {
                                                    if((x-nx[i])>-1 && (x-nx[i])<9 && (y-ny[i])>-1 && (y-ny[i])<9){
                                                    ((BlockButton) buttons[x-nx[i]][y-ny[i]]).breakBlock();
                                                }}

                                            }

                                            //남은 블록이 없으면 WIN
                                            if (BlockButton.blocks == 0) {
                                                Toast.makeText(MainActivity.this, "니가이김", Toast.LENGTH_SHORT).show();
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