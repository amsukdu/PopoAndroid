package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private GeaApi api = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        api = new GeaApi(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).hide();
        api.connect((b) -> {});
        setContentView(R.layout.activity_main);
        findViewById(R.id.neck_down_img).setOnTouchListener(this);
        findViewById(R.id.camera_down_img).setOnTouchListener(this);
        findViewById(R.id.camera_left_img).setOnTouchListener(this);
        findViewById(R.id.camera_right_img).setOnTouchListener(this);
        findViewById(R.id.camera_up_img).setOnTouchListener(this);
        findViewById(R.id.neck_up_img).setOnTouchListener(this);
        findViewById(R.id.back_fast_img).setOnTouchListener(this);
        findViewById(R.id.back_slow_img).setOnTouchListener(this);
        findViewById(R.id.turn_left_img).setOnTouchListener(this);
        findViewById(R.id.turn_right_img).setOnTouchListener(this);
        findViewById(R.id.go_slow_img).setOnTouchListener(this);
        findViewById(R.id.go_fast_img).setOnTouchListener(this);
        findViewById(R.id.right_handle_img).setOnTouchListener(this);

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                api.currentAppStatus((str) -> {
                    Log.d("amsukdu", String.format("%s", str));
                });
                ha.postDelayed(this, 1000);
            }
        }, 1000);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.google.com");
        myWebView.loadUrl("http://192.168.1.80:8090/stream");
    }

    public boolean isShowingRightDrawer() {
        return findViewById(R.id.right_view).isShown();
    }

    public void setShowingRightDrawer(boolean show) {
        ImageView img = findViewById(R.id.right_handle_img);
        img.setImageResource(show ? R.drawable.righthandleclose : R.drawable.righthandleopen);
        findViewById(R.id.right_view).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.onTouchEvent(motionEvent);
        if (view == findViewById(R.id.neck_down_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.neck_down_img);
                image.setImageResource(R.drawable.neckdown);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.neck_down_img);
                image.setImageResource(R.drawable.neckdownsel);
                api.popoAction(PopoActions.LOWER_NECK, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.camera_down_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.camera_down_img);
                image.setImageResource(R.drawable.cameradown);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.camera_down_img);
                image.setImageResource(R.drawable.cameradownsel);
                api.popoAction(PopoActions.CAM_DOWN, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.camera_left_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.camera_left_img);
                image.setImageResource(R.drawable.cameraleft);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.camera_left_img);
                image.setImageResource(R.drawable.cameraleftsel);
                api.popoAction(PopoActions.CAM_LEFT, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.camera_right_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.camera_right_img);
                image.setImageResource(R.drawable.cameraright);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.camera_right_img);
                image.setImageResource(R.drawable.camerarightsel);
                api.popoAction(PopoActions.CAM_RIGHT, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.camera_up_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.camera_up_img);
                image.setImageResource(R.drawable.cameraup);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.camera_up_img);
                image.setImageResource(R.drawable.cameraupsel);
                api.popoAction(PopoActions.CAM_UP, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.neck_up_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.neck_up_img);
                image.setImageResource(R.drawable.neckup);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.neck_up_img);
                image.setImageResource(R.drawable.neckupsel);
                api.popoAction(PopoActions.RAISE_NECK, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.back_fast_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.back_fast_img);
                image.setImageResource(R.drawable.backfast);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.back_fast_img);
                image.setImageResource(R.drawable.backfastsel);
                api.popoAction(PopoActions.BACKWARD_FAST, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.back_slow_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.back_slow_img);
                image.setImageResource(R.drawable.backslow);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.back_slow_img);
                image.setImageResource(R.drawable.backslowsel);
                api.popoAction(PopoActions.BACKWARD_SLOW, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.turn_left_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.turn_left_img);
                image.setImageResource(R.drawable.turnleft);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.turn_left_img);
                image.setImageResource(R.drawable.turnleftsel);
                api.popoAction(PopoActions.TURN_LEFT, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.turn_right_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.turn_right_img);
                image.setImageResource(R.drawable.turnright);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.turn_right_img);
                image.setImageResource(R.drawable.turnrightsel);
                api.popoAction(PopoActions.TURN_RIGHT, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.go_slow_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.go_slow_img);
                image.setImageResource(R.drawable.goslow);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.go_slow_img);
                image.setImageResource(R.drawable.goslowsel);
                api.popoAction(PopoActions.FORWARD_SLOW, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.go_fast_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ImageView image = findViewById(R.id.go_fast_img);
                image.setImageResource(R.drawable.gofast);
                api.popoAction(PopoActions.STOP_ALL, (b) -> {
                });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView image = findViewById(R.id.go_fast_img);
                image.setImageResource(R.drawable.gofastsel);
                api.popoAction(PopoActions.FORWARD_FAST, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.right_handle_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setShowingRightDrawer(!isShowingRightDrawer());
            }
        }
        return true;
    }
}