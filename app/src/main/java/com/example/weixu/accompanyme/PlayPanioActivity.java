package com.example.weixu.accompanyme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.weixu.service.PanioMusic;

public class PlayPanioActivity extends AppCompatActivity {
    private Button button[];// 按钮数组
    private PanioMusic utils;// 工具类
    private View parent;// 父视图
    private int buttonId[];// 按钮id
    private boolean havePlayed[];// 是否已经播放了声音，当手指在同一个按钮内滑动，且已经发声，就为true
    private View keys;// 按钮们所在的视图
    private int pressedkey[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_panio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        parent = (View) findViewById(R.id.llparent);
        parent.setClickable(true);

        parent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int temp;
                int tempIndex;
                int pointercount;
                pointercount = event.getPointerCount();
                for (int count = 0; count < pointercount; count++) {
                    boolean moveflag = false;// 标记是否是在按键上移动
                    temp = isInAnyScale(event.getX(count), event.getY(count),
                            button);
                    if (temp != -1) {// 事件对应的是当前点
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                // // 单独一根手指或最先按下的那个
                                // pressedkey = temp;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                Log.i("--", "count" + count);
                                pressedkey[count] = temp;
                                if (!havePlayed[temp]) {// 在某个按键范围内
                                    button[temp]
                                            .setBackgroundResource(R.drawable.button_pressed);
                                    // 播放音阶
                                    utils.soundPlay(temp);
                                    Log.i("--", "sound" + temp);
                                    havePlayed[temp] = true;
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                temp = pressedkey[count];
                                for (int i = temp + 1; i >= temp - 1; i--) {
                                    // 当在两端的按钮时，会有一边越界
                                    if (i < 0 || i >= button.length) {
                                        continue;
                                    }
                                    if (isInScale(event.getX(count),
                                            event.getY(count), button[i])) {// 在某个按键内
                                        moveflag = true;
                                        if (i != temp) {// 在相邻按键内
                                            boolean laststill = false;
                                            boolean nextstill = false;
                                            // 假设手指已经从上一个位置抬起，但是没有真的抬起，所以不移位
                                            pressedkey[count] = -1;
                                            for (int j = 0; j < pointercount; j++) {
                                                if (pressedkey[j] == temp) {
                                                    laststill = true;
                                                }
                                                if (pressedkey[j] == i) {
                                                    nextstill = true;
                                                }
                                            }

                                            if (!nextstill) {// 移入的按键没有按下
                                                // 设置当前按键
                                                button[i]
                                                        .setBackgroundResource(R.drawable.button_pressed);
                                                // 发音
                                                utils.soundPlay(i);
                                                havePlayed[i] = true;
                                            }

                                            pressedkey[count] = i;

                                            if (!laststill) {// 没有手指按在上面
                                                // 设置上一个按键
                                                button[temp]
                                                        .setBackgroundResource(R.drawable.button);
                                                havePlayed[temp] = false;
                                            }

                                            break;
                                        }
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_POINTER_UP:
                                // 事件与点对应
                                tempIndex = event.getActionIndex();
                                if (tempIndex == count) {
                                    Log.i("--", "index" + tempIndex);
                                    boolean still = false;
                                    // 当前点已抬起
                                    for (int t = count; t < 5; t++) {
                                        if (t != 4) {
                                            if (pressedkey[t + 1] >= 0) {
                                                pressedkey[t] = pressedkey[t + 1];
                                            } else {
                                                pressedkey[t] = -1;
                                            }
                                        } else {
                                            pressedkey[t] = -1;
                                        }

                                    }
                                    for (int i = 0; i < pressedkey.length; i++) {// 是否还有其他点
                                        if (pressedkey[i] == temp) {
                                            still = true;
                                            break;
                                        }
                                    }
                                    if (!still) {// 已经没有手指按在该键上
                                        button[temp]
                                                .setBackgroundResource(R.drawable.button);
                                        havePlayed[temp] = false;
                                        Log.i("--", "button" + temp + "up");
                                    }
                                    break;
                                }
                        }
                    }
                    //
                    if (event.getActionMasked() == MotionEvent.ACTION_MOVE
                            && !moveflag) {
                        if (pressedkey[count] != -1) {
                            button[pressedkey[count]]
                                    .setBackgroundResource(R.drawable.button);
                            havePlayed[pressedkey[count]] = false;
                        }
                    }
                }
                return false;
            }
        });

        keys = (View) findViewById(R.id.llKeys);
    }

    private void init() {
        // 新建工具类
        utils = new PanioMusic(getApplicationContext());

        // 按钮资源Id
        buttonId = new int[7];
        buttonId[0] = R.id.btPanioOne;
        buttonId[1] = R.id.btPanioTwo;
        buttonId[2] = R.id.btPanioThree;
        buttonId[3] = R.id.btPanioFour;
        buttonId[4] = R.id.btPanioFive;
        buttonId[5] = R.id.btPanioSix;
        buttonId[6] = R.id.btPanioSeven;

        button = new Button[7];
        havePlayed = new boolean[7];

        // 获取按钮对象
        for (int i = 0; i < button.length; i++) {
            button[i] = (Button) findViewById(buttonId[i]);
            button[i].setClickable(false);
            havePlayed[i] = false;
        }

        pressedkey = new int[5];
        for (int j = 0; j < pressedkey.length; j++) {
            pressedkey[j] = -1;
        }

    }

    /**
     * 判断某个点是否在某个按钮的范围内
     *
     * @param x      横坐标
     * @param y      纵坐标
     * @param button 按钮对象
     * @return 在：true；不在：false
     */
    private boolean isInScale(float x, float y, Button button) {
        // keys.getTop()是获取按钮所在父视图相对其父视图的右上角纵坐标

        if (x > button.getLeft() && x < button.getRight()
                && y > button.getTop() + keys.getTop()
                && y < button.getBottom() + keys.getTop()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断某个点是否在一个按钮集合中的某个按钮内
     *
     * @param x      横坐标
     * @param y      纵坐标
     * @param button 按钮数组
     * @return
     */
    private int isInAnyScale(float x, float y, Button[] button) {
        // keys.getTop()是获取按钮所在父视图相对其父视图的右上角纵坐标

        for (int i = 0; i < button.length; i++) {
            if (x > button[i].getLeft() && x < button[i].getRight()
                    && y > button[i].getTop() + keys.getTop()
                    && y < button[i].getBottom() + keys.getTop()) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
