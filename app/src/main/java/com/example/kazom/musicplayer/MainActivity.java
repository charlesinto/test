package com.example.kazom.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.media.session.PlaybackState;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener audioFocusListener;
    private int focus;

    MediaPlayer.OnCompletionListener mOnComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMedia();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = (Button) findViewById(R.id.Button1);
        Button pauseButton = (Button) findViewById(R.id.Button2);
        Button stopButton = (Button) findViewById(R.id.Button3);
        Button upButton = (Button) findViewById(R.id.Button4);
        Button downutton = (Button) findViewById(R.id.Button5);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

         audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch(focusChange){
                    case AudioManager.AUDIOFOCUS_GAIN:
                        play();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        stop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        pause();
                        break;
                }
            }
        };

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus = audioManager.requestAudioFocus(audioFocusListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                play();

            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();

            }
        });
//        upButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                INIT_VOLUME = INIT_VOLUME + 1.2 * INIT_VOLUME;
//
//            }
//        });
    }
    public void play() {
        try {
            if (mediaPlayer == null && focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.just_as_i_am);
                mediaPlayer.start();
            } else {
                mediaPlayer.start();
            }

        } catch (IllegalStateException e) {
            Log.v("MediaPlayer.java", "In the wrong state");
            Log.v("MediaPlayer.java", "Media: " + mediaPlayer);
        }
        mediaPlayer.setOnCompletionListener(mOnComplete);
    }

    public void stop() {
        releaseMedia();
        audioManager.abandonAudioFocus(audioFocusListener);
    }

    public void pause() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }

        } catch (IllegalStateException e) {
            Log.v("MediaPlayer.java", "In the wrong state");
        }
    }
    public void releaseMedia (){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
