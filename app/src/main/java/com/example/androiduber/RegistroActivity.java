package com.example.androiduber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiduber.ui.home.HomeFragment;

public class RegistroActivity extends AppCompatActivity {

    TextView txtPartida;
    Spinner spinnerPartida;
    Button btnSeleccon, btnAceptar, btnReserva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtPartida = findViewById(R.id.txtPartida);
        btnSeleccon = findViewById(R.id.btnSeleccionar);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnReserva = findViewById(R.id.btnReserva);
        spinnerPartida= findViewById(R.id.spinnerPartida);


        btnSeleccon.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, DriverHomeActivity.class);
            startActivity(intent);
        });

        btnAceptar.setOnClickListener(v -> {
            if (txtPartida.getText().toString().isEmpty() && !txtPartida.getText().toString().contains(" ") )
                Toast.makeText(RegistroActivity.this, "Por favor seleccione una ruta", Toast.LENGTH_SHORT).show();
                else

            Toast.makeText(RegistroActivity.this, "Solicitud exitosa!!", Toast.LENGTH_SHORT).show();
        });

        btnReserva.setOnClickListener(v -> {
            if (txtPartida.getText().toString().isEmpty() && !txtPartida.getText().toString().contains(" ") )
                Toast.makeText(RegistroActivity.this, "Por favor seleccione una ruta", Toast.LENGTH_SHORT).show();
            else

                Toast.makeText(RegistroActivity.this, "Reserva exitosa!!", Toast.LENGTH_SHORT).show();
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lugaresCercanos, android.R.layout.simple_spinner_item);
        spinnerPartida.setAdapter(adapter);

        spinnerPartida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txtPartida.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}