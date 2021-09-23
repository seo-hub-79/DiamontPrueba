package com.example.androiduber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class RutasActivity extends AppCompatActivity {
    private GridView gridView;

    private girdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Lugar de Partida: Sauna Piscina Orlando \nLugar de llegada: Cancha Venus \n Coste: 15bs");
        arrayList.add("Lugar de Partida: Cancha Alto Pampahasi \nLugar de llegada: Colegio Don Bosco Pampahasi \n Coste: 15bs\n RESERVA");
        arrayList.add("Lugar de Partida: Stadium Hernando Siles \nLugar de llegada: Terminal \nCoste: 25bs");
        arrayList.add("Lugar de Partida: Correos  \nLugar de llegada:  Aereopuerto \nCoste:  80bs");
        arrayList.add("Lugar de Partida: Iglesia Villa copacabana \nLugar de llegada:  Stadium \nCoste:  25bs");


        gridView = (GridView) findViewById(R.id.am_gv_gridView);
        adapter = new girdAdapter(this,arrayList);
        gridView.setAdapter(adapter);


    }
}