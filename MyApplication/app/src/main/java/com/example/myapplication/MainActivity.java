package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private boolean isOn;
    private Button buttonOnOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOnOff = findViewById(R.id.buttonOnOff);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.sair);
        textView = findViewById(R.id.logado);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();


        }

        else {
            textView.setText(user.getEmail());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();

            }
        });
        // Obtém a referência do banco de dados do Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("acionamento/");

// Adiciona um ouvinte para o valor booleano
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Atualiza o estado do botão com base no valor booleano no Firebase
                isOn = snapshot.getValue(Boolean.class);
                updateButtonState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Trate os erros de leitura do Firebase aqui, se necessário
            }
        });

        buttonOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inverte o valor booleano
                isOn = !isOn;

                // Atualiza o valor booleano no Firebase
                databaseReference.setValue(isOn);
            }
        });



    }

    private void updateButtonState() {
        if (isOn) {
            buttonOnOff.setText("Trancar");
            buttonOnOff.setBackgroundColor(0xFF00FF00);

        } else {
            buttonOnOff.setText("Destrancar");
            buttonOnOff.setBackgroundColor(0xFFFF0000);

        }
    }}

