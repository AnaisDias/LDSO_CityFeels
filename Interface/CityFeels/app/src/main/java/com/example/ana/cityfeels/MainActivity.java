package com.example.ana.cityfeels;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ana.cityfeels.sia.Etiqueta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, LocationEventListener {

    private final static int TEXT_TO_SPEECH_CHECK_CODE = 0;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DEPOIS POR EM FUNÇAO-----------------------------------------
        final Button btn1 = (Button) findViewById(R.id.button1);
        final Button btn2 = (Button) findViewById(R.id.button2);
        final Button btn3 = (Button) findViewById(R.id.button3);
        final Button btn4 = (Button) findViewById(R.id.button4);
        btn1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn2.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(false);
                return true;
            }
        });
        btn2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn1.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(false);
                return true;
            }
        });
        btn3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn2.setPressed(false);
                btn1.setPressed(false);
                btn4.setPressed(false);
                return true;
            }
        });
        btn4.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn2.setPressed(false);
                btn3.setPressed(false);
                btn1.setPressed(false);
                return true;
            }
        });
        //----DEPOIS POR EM FUNÇAO-----------------------------------------

        Button generateLocationButton = (Button) findViewById(R.id.generate_location_button);
        generateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                float latitude = random.nextInt(2) + 1;
                LocationEventDispatcher.fireNewLocation(new Location(latitude, 3f));
            }
        });

        LocationEventDispatcher.registerOnNewLocation(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, TEXT_TO_SPEECH_CHECK_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonprincipal, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewLocation(Location location) {
        Toast.makeText(this, location.latitude + ", " + location.longitude, Toast.LENGTH_LONG).show();

        Etiqueta etiqueta = Database.getEtiquetaAtLocation(location);
        this.textToSpeech.speak(etiqueta.informacao, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            this.textToSpeech.setLanguage(new Locale("pt", "BR"));

            Locale[] locales = Locale.getAvailableLocales();
            List<Locale> localeList = new ArrayList<>();
            for (Locale locale : locales) {
                int res = this.textToSpeech.isLanguageAvailable(locale);
                if (res == TextToSpeech.LANG_AVAILABLE) {
                    localeList.add(locale);
                }
            }

        }
        else if (status == TextToSpeech.ERROR)
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TEXT_TO_SPEECH_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                this.textToSpeech = new TextToSpeech(this, this);
            }
            else {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
}
