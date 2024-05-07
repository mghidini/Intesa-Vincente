package com.example.intesavincente.ui.partita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.intesavincente.ui.home.MainActivity;
import com.example.intesavincente.utils.Launch_Screen;
import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.repository.partita.PartitaResponse;
import com.example.intesavincente.repository.traduzione.TraduzioneRepository;
import com.example.intesavincente.repository.traduzione.TraduzioneResponse;
import com.example.intesavincente.repository.words.IWordsRepository;
import com.example.intesavincente.repository.words.WordsRepository;
import com.example.intesavincente.utils.Constants;
import com.example.intesavincente.utils.ResponseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Indovinatore extends AppCompatActivity implements PartitaResponse, ResponseCallback, TraduzioneResponse {
    private static final String TAG ="Indovinatore" ;

    //private final Application mApplication;
    private String parola;
    private IWordsRepository mIWordsRepository;
    private PartitaRepository mPartitaRepository;
    private TraduzioneRepository mTraduzioneRepository;

    private MediaPlayer soundClickBuzzer;
    private MediaPlayer soundClickPasso;
    private MediaPlayer soundCambiaParola;
    private MediaPlayer soundGongTimer;
    private MediaPlayer soundParoleIndovinate2;
    private MediaPlayer soundParoleIndovinate6;
    private MediaPlayer soundErroreEIndietro;
    private MediaPlayer soundFinitiPasso;
    private MediaPlayer soundMenoMalePasso;
    private MediaPlayer soundOk4Parole;
    private MediaPlayer soundDoppiaCifra;

    private TextView timer;
    private TextView numeroPasso;
    private TextView paroleIndovinate;
    private Button buzz;
    private Button passo;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 600000;

    private SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    private SharedPreferences.Editor editor = pref.edit();

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public Indovinatore() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indovinatore);
        // float count=100000*.01f;
        soundClickBuzzer = MediaPlayer.create(this, R.raw.click_buzzer);
        soundClickPasso = MediaPlayer.create(this, R.raw.passo);
        soundGongTimer = MediaPlayer.create(this, R.raw.gong_timer);
        soundCambiaParola = MediaPlayer.create(this, R.raw.cambio_parola);
        soundParoleIndovinate2 = MediaPlayer.create(this, R.raw.e_le_parole_diventano_2);
        soundParoleIndovinate6 = MediaPlayer.create(this, R.raw.e_le_parole_diventano_6);
        soundErroreEIndietro = MediaPlayer.create(this, R.raw.errore_e_tornate_indietro);
        soundFinitiPasso = MediaPlayer.create(this, R.raw.finiti_i_passo_ragazzi_attenzione);
        soundMenoMalePasso = MediaPlayer.create(this, R.raw.meno_male_che_ha_detto_passo);
        soundOk4Parole = MediaPlayer.create(this, R.raw.ok_4_parole);
        soundDoppiaCifra = MediaPlayer.create(this, R.raw.perfetto_doppia_cifra);

        mIWordsRepository = new WordsRepository((Application) MyApplication.getAppContext(), this);
        mTraduzioneRepository = new TraduzioneRepository((Application) MyApplication.getAppContext(), this);

        mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
        mPartitaRepository.trovaPartita();
    }

    public void startTimer(String idPartita) {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftMillis = l;
                updateCountDowText(idPartita);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
            }
        }.start();
        timerRunning = true;
    }

    public void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void updateCountDowText(String idPartita) {
        int seconds = (int) (timeLeftMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        timer.setText(timeLeftFormatted);

        if(timeLeftFormatted.equals("00")){
            soundGongTimer.start();
            pauseTimer();
            Log.d(TAG, "If timer 00");
            finePartita(idPartita);
            Log.d(TAG, "PARTITA FINITA");
        }
    }

    @Override
    public void onDataFound(Partita partita) {

        Log.d(TAG, "par89"+partita);
        Log.d(TAG, "par891"+partita.getIdPartita());

        paroleIndovinate= findViewById(R.id.parole);
        buzz = findViewById(R.id.buzz);
        timer = findViewById(R.id.timer);
        numeroPasso = findViewById(R.id.passo);

        buzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning) {
                    soundClickBuzzer.start();
                    pauseTimer();
                    //mIWordsRepository.fetchWords();
                    String parola=pref.getString("name", "prova");
                    Log.d(TAG, "parolaEstratta"+parola);
                    Intent i = new Intent(Indovinatore.this, InserisciParola.class);
                    i.putExtra("parola",getParola());
                    i.putExtra("partita",partita.getIdPartita());
                    startActivity(i);
                    aggiornaParola(partita.getIdPartita());
                } else {
                    soundCambiaParola.start();
                    Log.d(TAG, "PreTimer " + partita.getIdPartita());
                    startTimer(partita.getIdPartita());
                    Log.d(TAG, "AfterTimer " + partita.getIdPartita());
                    mIWordsRepository.fetchWords();
                    String parola=pref.getString("name", "prova");
                    String parola1=pref.getString("tradotta", "prova");
                    //mTraduzioneRepository.fetchTraduzione(parola);
                    setParola(parola1);
                    Log.d(TAG, "parolaEstratta"+parola);
                    Log.d(TAG, "parolaTradotta"+parola1);
                    caricaParola(getParola(),partita.getIdPartita());
                }
            }
        });

        passo = findViewById(R.id.button_passo);
        passo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning) {
                    soundClickPasso.start();
                    pauseTimer();
                    aggiornaPasso(partita.getIdPartita());
                }
            }
        });
    }

    @Override
    public void onResponse(String parola) {
        //SharedPreferences.Editor editor = pref.edit();
        System.out.println("dentroResponse"+parola);
        mTraduzioneRepository.fetchTraduzione(parola);
        editor.putString("name", parola);
        editor.apply();
    }

    @Override
    public void onTradotto(String parola) {
        System.out.println("dentrotradotto"+parola);
        editor.putString("tradotta", parola);
        editor.apply();
    }

    @Override
    public void onErrore(String err) {
        System.out.println("errore");
    }

    @Override
    public void onFailure(String errorMessage) {

    }

    public void caricaParola(String parola, String idPartita){
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("idPartita").getValue().equals(idPartita))
                        db.child(keyNode.getKey()).child("parola").setValue(parola);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void finePartita(String idPartita){
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("idPartita").getValue().equals(idPartita)){
                        db.child(keyNode.getKey()).child("attiva").setValue(false);
                        Intent io = new Intent(Indovinatore.this, MainActivity.class);
                        startActivity(io);
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void aggiornaParola(String idPartita){
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("idPartita").getValue().equals(idPartita)){
                        int pIndovinate=keyNode.child("parole_indovinate").getValue(Integer.class);

                        if(Integer.parseInt(paroleIndovinate.getText().toString()) < pIndovinate)
                            switch (pIndovinate){
                                case 2: soundParoleIndovinate2.start(); break;
                                case 4: soundOk4Parole.start();         break;
                                case 6: soundParoleIndovinate6.start(); break;
                                case 10:soundDoppiaCifra.start();       break;
                            }
                        System.out.println("parole_indovinate"+pIndovinate);
                        paroleIndovinate.setText(String.valueOf(pIndovinate));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void aggiornaPasso(String idPartita){
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("idPartita").getValue().equals(idPartita)){
                        int nPasso=keyNode.child("passo").getValue(Integer.class);
                        System.out.println("numero passo "+nPasso);
                        if(nPasso != 0){
                            nPasso--;
                            db.child(keyNode.getKey()).child("passo").setValue(nPasso);
                            soundClickPasso.start();
                            if((Integer.parseInt(numeroPasso.getText().toString()) != nPasso) && (nPasso == 1))
                                soundMenoMalePasso.start();
                            if((Integer.parseInt(numeroPasso.getText().toString()) != nPasso) && (nPasso == 0))
                                soundFinitiPasso.start();
                            numeroPasso.setText(String.valueOf(nPasso));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
