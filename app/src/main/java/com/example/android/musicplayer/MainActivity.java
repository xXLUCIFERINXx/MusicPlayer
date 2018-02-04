package com.example.android.musicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    SeekBar seeker;
    MediaPlayer wolves;
    Button play;
    Button stop;
    TextView timeStamp,t1;
    TextView totalTime,t2;
    int position;
    private final Handler handler = new Handler();

    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        seeker = findViewById(R.id.seeker);
        wolves = MediaPlayer.create(this, R.raw.wolves);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);

        wolves.setLooping(true);
        seeker.setMax(wolves.getDuration());

        play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (wolves.isPlaying()) {
                    wolves.pause();
                    play.setText("resume");
                    handler.removeCallbacks(updatePositionRunnable);
                } else {
                    wolves.start();
                    play.setText("Pause");
                    updatePosition();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                wolves.stop();
                try {
                    wolves.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                play.setText("Play");
            }
        });

        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    wolves.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);
        seeker.setProgress(wolves.getCurrentPosition());
        timeStamp = findViewById(R.id.timeStamp);
        totalTime = findViewById(R.id.totalTime);

        int num1 = wolves.getCurrentPosition() / 1000;
        int num2 = num1 / 60;
        int num3 = num1 % 60;

        if (num3 < 10) {
            timeStamp.setText(num2 + ":0" + num3);
        } else {
            timeStamp.setText(num2 + ":" + num3);
        }

        int num4 = wolves.getDuration() / 1000;
        int num5 = num4 / 60;
        int num6 = num4 % 60;

        if (num6 < 10) {
            totalTime.setText(num5 + ":0" + num6);
        } else {
            totalTime.setText(num5 + ":" + num6);
        }
        handler.postDelayed(updatePositionRunnable, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        t1=timeStamp;
        t2=totalTime;
        position = wolves.getCurrentPosition();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeStamp=t1;
        totalTime=t2;
        wolves.seekTo(position);
    }
}