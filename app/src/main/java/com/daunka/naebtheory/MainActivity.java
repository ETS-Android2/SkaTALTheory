package com.daunka.naebtheory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    boolean volumepress = false;
    String[] currentBlock;
    String timefile;
    int count = 0;
    String[] newtimecodes;
    Locale locale = new Locale("ru");
    private TextToSpeech textToSpeechSystem;
    MediaPlayer mediaPlayer = new MediaPlayer();
    int globj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BroadcastReceiver vReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                volumepress = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        volumepress = false;
                    }
                }, 2000);
            }
        };
        registerReceiver(vReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
        textToSpeechSystem = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechSystem.setLanguage(locale);
                }
            }
        });

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    volumepress = true;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            volumepress = false;
                        }
                    }, 2000);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getApplicationContext(), selectedfile);
                mediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] shit = new String[1];
            currentBlock = timefile.split("#");
            shit[0] = currentBlock[0];

            fuckMyLifeAgain fml = new fuckMyLifeAgain();

            fml.execute(shit);
        }
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(selectedfile);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            timefile = total.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
                openaudiochooser();
        }
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();


            String entireFile = "";
            try {
                InputStream in = getContentResolver().openInputStream(selectedfile);


                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                entireFile = total.toString();
                String[] shit = new String[1];
                currentBlock = entireFile.split("#");
                shit[0] = currentBlock[0];

                fuckMyLife FuckMyLife = new fuckMyLife();

                FuckMyLife.execute(shit);
            } catch (Exception e) {

            }

        }
    }

    public class fuckMyLife extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... i) {
            debugMessage(i[0]);
            textToSpeechSystem.speak(i[0], TextToSpeech.QUEUE_ADD, null);

        }

        @Override
        protected void onPostExecute(String j) {
            debugMessage(j);
            textToSpeechSystem.speak(j, TextToSpeech.QUEUE_ADD, null);
            count++;

            if (count < currentBlock.length) {
                String[] shit = new String[1];
                shit[0] = currentBlock[count];
                fuckMyLife FuckMyLife = new fuckMyLife();
                FuckMyLife.execute(shit);
            } else {
                count = 0;
            }
        }


        @Override
        protected String doInBackground(String... shit) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            while (textToSpeechSystem.isSpeaking() == true) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            String[] markers;
            String[] newmarkers;
            String[] currentTheory;
            String currentBlock;
            currentBlock = shit[0];
            int j = 0;

            currentTheory = currentBlock.split("\t");

            markers = currentBlock.split(System.getProperty("line.separator"));
            for (int k = 0; k < markers.length; k++) {
                if (!markers[k].startsWith("\t")) {
                    markers[k] = "";
                }
            }

            StringBuilder finalStringBuilder = new StringBuilder("");
            for (String s : markers) {
                if (!s.equals("")) {
                    finalStringBuilder.append(s);
                }
            }
            String finalString = finalStringBuilder.toString();
            newmarkers = finalString.split("\t");

            while (volumepress == false) {
                for (j = 1; j < newmarkers.length; j++) {
                    String[] shit1 = new String[1];
                    shit1[0] = newmarkers[j];
                    publishProgress(newmarkers[j]);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                    while (textToSpeechSystem.isSpeaking() == true) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                    if (volumepress == true) {
                        break;
                    }
                }
            }

            return currentTheory[j];

        }
    }


    public class fuckMyLifeAgain extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... i) {
            debugMessage(i[0]);
            textToSpeechSystem.speak(i[0], TextToSpeech.QUEUE_ADD, null);

        }

        @Override
        protected void onPostExecute(String j) {
            debugMessage(j);

            mediaPlayer.start();
            mediaPlayer.seekTo(1000*Integer.parseInt(j.trim()));

            count++;

            if (count < currentBlock.length) {
                String[] shit = new String[1];
                shit[0] = currentBlock[count];
                fuckMyLifeAgain FuckMyLifeAgain = new fuckMyLifeAgain();
                FuckMyLifeAgain.execute(shit);
            } else {
                count = 0;
            }
        }


        @Override
        protected String doInBackground(String... shit) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            while (textToSpeechSystem.isSpeaking() == true) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            while (mediaPlayer.isPlaying()) {
                if (mediaPlayer.getCurrentPosition()>=1000*Integer.parseInt(newtimecodes[globj])) {
                    mediaPlayer.pause();
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }

            String[] markers;
            String[] newmarkers;
            String[] timecodes;

            String currentBlock;
            currentBlock = shit[0];
            int j = 0;

            markers = currentBlock.split(System.getProperty("line.separator"));
            for (int k = 0; k < markers.length; k++) {
                if (!markers[k].startsWith("\t")) {
                    markers[k] = "";
                }
            }

            StringBuilder finalStringBuilder = new StringBuilder("");
            for (String s : markers) {
                if (!s.equals("")) {
                    finalStringBuilder.append(s);
                }
            }
            String finalString = finalStringBuilder.toString();
            newmarkers = finalString.split("\t");
            timecodes = currentBlock.split(System.getProperty("line.separator"));
            for (int k = 0; k < timecodes.length; k++) {
                if (timecodes[k].startsWith("\t")) {
                    timecodes[k] = "";
                }
            }
            StringBuilder newStringBuilder = new StringBuilder("");
            for (String s : timecodes) {
                if (!s.equals("")) {
                    newStringBuilder.append(s).append(System.getProperty("line.separator"));
                }
            }
            finalString = newStringBuilder.toString();
            newtimecodes = finalString.split(System.getProperty("line.separator"));
            while (volumepress == false) {
                for (j = 1; j < newmarkers.length; j++) {
                    String[] shit1 = new String[1];
                    shit1[0] = newmarkers[j];
                    publishProgress(newmarkers[j]);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                    while (textToSpeechSystem.isSpeaking() == true) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                    if (volumepress == true) {
                        break;
                    }
                }
            }

            globj=j;
            return newtimecodes[j-1];

        }
    }

    public void debugMessage(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public void openfilechooser(View view) {

        Intent intent = new Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    public void opentimecodechooser(View view) {

        Intent intent = new Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 1234);
    }

    public void openaudiochooser() {

        Intent intent = new Intent()
                .setType("audio/aac")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 12345);
    }
}

