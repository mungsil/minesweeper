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

                int[] nx = {-1, -1, 0, 1, 1, 1, 0, -1};
                int[] ny = {0, 1, 1, 1, 0, -1, -1, -1};
                for (int i = 0; i < 8; i++) {
                    int newX = x + nx[i];
                    int newY = y + ny[i];

                    if (newX >= 0 && newX < width && newY >= 0 && newY < height && map[newY][newX] != -1) {
                        map[newY][newX] += 1;
                    }
                }
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
                                            // AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
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



                                        }
                                    }
                                }
                                //남은 블록이 없으면 WIN
                                    if (BlockButton.blocks == 0) {
                                        // AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                                        Toast.makeText(MainActivity.this, "니가이김", Toast.LENGTH_SHORT).show();
                                    }

                            }
                });

            }
        }
    }
}