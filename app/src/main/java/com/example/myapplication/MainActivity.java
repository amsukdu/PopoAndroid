package com.example.myapplication;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private GeaApi api = null;
    private Vibrator haptic = null;
    private AzureSpeech azureSpeech = new AzureSpeech();

    public String getFridgeStatus() {
        return fridgeStatus;
    }

    public void setFridgeStatus(String fridgeStatus) {
        if (fridgeStatus.equals("01")) {
            findViewById(R.id.fridge_big_up_left).setVisibility(View.GONE);
            findViewById(R.id.fridge_big_up_right).setVisibility(View.VISIBLE);
            findViewById(R.id.fridge_warning).setVisibility(View.VISIBLE);
        } else if (fridgeStatus.equals("10")) {
            findViewById(R.id.fridge_big_up_left).setVisibility(View.VISIBLE);
            findViewById(R.id.fridge_big_up_right).setVisibility(View.GONE);
            findViewById(R.id.fridge_warning).setVisibility(View.VISIBLE);
        } else if (fridgeStatus.equals("11")) {
            findViewById(R.id.fridge_big_up_left).setVisibility(View.VISIBLE);
            findViewById(R.id.fridge_big_up_right).setVisibility(View.VISIBLE);
            findViewById(R.id.fridge_warning).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.fridge_big_up_left).setVisibility(View.GONE);
            findViewById(R.id.fridge_big_up_right).setVisibility(View.GONE);
            findViewById(R.id.fridge_warning).setVisibility(View.GONE);
        }
        this.fridgeStatus = fridgeStatus;
    }

    private String fridgeStatus = "00";

    public String getOvenMinute() {
        return ovenMinute;
    }

    public void setWasherMinute(String ovenMinute) {
        char[] charArray = ovenMinute.toCharArray();
        ImageView first = findViewById(R.id.right_view_washer_view_time_first);
        ImageView second = findViewById(R.id.right_view_washer_view_time_second);
        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
        switch (charArray[0]) {
            case '0':
                first.setImageResource(R.drawable.number0);
                break;
            case '1':
                first.setImageResource(R.drawable.number1);
                break;
            case '2':
                first.setImageResource(R.drawable.number2);
                break;
            case '3':
                first.setImageResource(R.drawable.number3);
                break;
            case '4':
                first.setImageResource(R.drawable.number4);
                break;
            case '5':
                first.setImageResource(R.drawable.number5);
                break;
            case '6':
                first.setImageResource(R.drawable.number6);
                break;
            case '7':
                first.setImageResource(R.drawable.number7);
                break;
            case '8':
                first.setImageResource(R.drawable.number8);
                break;
            case '9':
                first.setImageResource(R.drawable.number9);
                break;
        }

        switch (charArray[1]) {
            case '0':
                second.setImageResource(R.drawable.number0);
                break;
            case '1':
                second.setImageResource(R.drawable.number1);
                break;
            case '2':
                second.setImageResource(R.drawable.number2);
                break;
            case '3':
                second.setImageResource(R.drawable.number3);
                break;
            case '4':
                second.setImageResource(R.drawable.number4);
                break;
            case '5':
                second.setImageResource(R.drawable.number5);
                break;
            case '6':
                second.setImageResource(R.drawable.number6);
                break;
            case '7':
                second.setImageResource(R.drawable.number7);
                break;
            case '8':
                second.setImageResource(R.drawable.number8);
                break;
            case '9':
                second.setImageResource(R.drawable.number9);
                break;
        }
        this.ovenMinute = ovenMinute;
    }

    private String ovenMinute = "00";

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        haptic = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        api = new GeaApi(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).hide();
        api.connect((b2) -> {
        });
//        api.disconnect((b) -> {
//            api.connect((b2) -> {
//            });
//        });

        azureSpeech.initialize(this);
        // Initialize SpeechSDK and request required permissions.
        try {
            // a unique number within the application to allow
            // correlating permission request responses with the request.
            int permissionRequestId = 5;

            // Request permissions needed for speech recognition
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, INTERNET, READ_EXTERNAL_STORAGE}, permissionRequestId);
        }
        catch(Exception ex) {
            Log.e("SpeechSDK", "could not init sdk, " + ex.toString());
//            recognizedTextView.setText("Could not initialize: " + ex.toString());
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
//            checkPermission();
//        }
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("amsukdu", "onReady");
            }

            @Override
            public void onBeginningOfSpeech() {
//                Log.d("amsukdu", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
//                Log.d("amsukdu", "onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
//                Log.d("amsukdu", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
//                Log.d("amsukdu", "onEndOfSpeech");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String str = data.toString();
                doSomething(str);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.mic_img).setOnTouchListener(this);
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
        findViewById(R.id.oven_big_img).setOnTouchListener(this);
        findViewById(R.id.washer_big_img).setOnTouchListener(this);
        findViewById(R.id.fridge_big_img).setOnTouchListener(this);
        findViewById(R.id.right_view_oven_view_left_arrow_view).setOnTouchListener(this);
        findViewById(R.id.right_view_fridge_view_left_arrow_view).setOnTouchListener(this);
        findViewById(R.id.right_view_washer_view_left_arrow_view).setOnTouchListener(this);
        findViewById(R.id.refresh_img).setOnTouchListener(this);
        findViewById(R.id.oven_power_off).setOnTouchListener(this);
        findViewById(R.id.oven_power_on).setOnTouchListener(this);
        findViewById(R.id.washer_power_off).setOnTouchListener(this);
        findViewById(R.id.washer_power_on).setOnTouchListener(this);

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                api.currentAppStatus((str) -> {
                    Log.d("amsukdu", String.format("%s", str));

                    if (str == null) {
                        return;
                    }
                    try {
                        JSONObject json = new JSONObject(str);
                        String status = json.getString("status");
                        String type = json.getString("kind").split("#")[1];
                        if (type.equals("fridgestatus")) {
                            setFridgeStatus(status);
                        } else if (type.equals("washerstatus")) {
                            setWasherMinute(status);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                ha.postDelayed(this, 500);
            }
        }, 500);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://192.168.1.80:8090/stream/video.mjpeg");
    }

    private void doSomething(String str) {
        Log.d("amsukdu", str);
//                if(str.equals("watch on") || str.equals("wash on") || str.equals("washer on")) {
//                    api.appAction(AppActions.WASHER_START, (b) -> {});
//                } else if(str.equals("watch off") || str.equals("wash off") || str.equals("washer off")) {
//                    api.appAction(AppActions.WASHER_STOP, (b) -> {});
//                } if(str.equals(""))
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            haptic.vibrate(50);
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
            } else {

            }
        } else if (view == findViewById(R.id.oven_big_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_oven_view).setVisibility(View.VISIBLE);
            }
        } else if (view == findViewById(R.id.fridge_big_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_fridge_view).setVisibility(View.VISIBLE);
            }
        } else if (view == findViewById(R.id.washer_big_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_washer_view).setVisibility(View.VISIBLE);
            }
        } else if (view == findViewById(R.id.right_view_oven_view_left_arrow_view)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_oven_view).setVisibility(View.GONE);
            }
        } else if (view == findViewById(R.id.right_view_fridge_view_left_arrow_view)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_fridge_view).setVisibility(View.GONE);
            }
        } else if (view == findViewById(R.id.right_view_washer_view_left_arrow_view)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.right_view_washer_view).setVisibility(View.GONE);
            }
        } else if (view == findViewById(R.id.refresh_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.reload();
            }
        } else if (view == findViewById(R.id.oven_power_off)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                api.appAction(AppActions.OVEN_STOP, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.oven_power_on)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                api.appAction(AppActions.OVEN_START, (b) -> {
                });
            }
        } else if (view == findViewById(R.id.washer_power_off)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                api.appAction(AppActions.WASHER_STOP, (b) -> {
                    findViewById(R.id.right_view_washer_view_time_first).setVisibility(View.GONE);
                    findViewById(R.id.right_view_washer_view_time_second).setVisibility(View.GONE);
                });
            }
        } else if (view == findViewById(R.id.washer_power_on)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                api.appAction(AppActions.WASHER_START, (b) -> {
                });
            }
//        } else if(view == findViewById(R.id.mic_img)) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                speechRecognizer.stopListening();
//            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
////                micButton.setImageResource(R.drawable.ic_mic_black_24dp);
//                speechRecognizer.startListening(speechRecognizerIntent);
//            }
//        }
        } else if(view == findViewById(R.id.mic_img)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                view.setEnabled(false);
        azureSpeech.recognize(new AzureSpeech.OnRecognitionCompletedListener() {
            @Override
            public void onCompleted(String s, Exception ex) {
                view.setEnabled(true);
                if (s != null) {
                    doSomething(s);
                } else {

                }
            }
        });
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                micButton.setImageResource(R.drawable.ic_mic_black_24dp);
//                speechRecognizer.startListening(speechRecognizerIntent);
            }
        }

        return true;
    }
}
