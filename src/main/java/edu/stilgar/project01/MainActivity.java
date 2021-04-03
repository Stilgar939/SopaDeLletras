package edu.stilgar.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton b1, b2;
    private ListView t1;
    public String fechaa,tiempoo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Tabla de puntuaciones
        // TODO Resoluciones en el xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent getData = getIntent();
        tiempoo =  getData.getStringExtra("time");
        fechaa = getData.getStringExtra("date");
        b1=(ImageButton) findViewById(R.id.botonJuega);
        b2=(ImageButton) findViewById(R.id.buttonHelp);
        t1= findViewById(R.id.puntuacion);
        t1.setBackgroundColor(Color.WHITE);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        vTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PantallaHelp.class);
            startActivity(intent);
        }else if (id == R.id.action_exit){
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View arg0) {
        Intent intent;
        switch (arg0.getId()){
            case R.id.botonJuega:
                intent = new Intent(this, PantallaJoc.class);
                startActivity(intent);
                break;
            case R.id.buttonHelp:
                intent = new Intent(this, PantallaHelp.class);
                startActivity(intent);
                break;
        }
    }

    public void vTable(){
        String TAULA = "SopaLetras";

        ArrayList<String> resultats = new ArrayList<String>();

        Context context = this;

        SQLiteDatabase baseDades = null;
        try {

            baseDades = getApplicationContext().openOrCreateDatabase("SopaaDeLetras", MODE_PRIVATE, null);


            baseDades.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TAULA
                    + " (fecha VARCHAR, tiempo VARCHAR);");

            if (fechaa != null) {

                baseDades.execSQL("INSERT INTO "
                        + TAULA
                        + " (fecha, tiempo)"
                        + " VALUES ('" + fechaa + "', '" + tiempoo + "');");
            }
            Cursor c = baseDades.rawQuery("SELECT fecha, tiempo FROM " + TAULA + " ORDER BY tiempo",null);

            int columnaFecha = c.getColumnIndex("fecha");
            int columnaTiempo = c.getColumnIndex("tiempo");

            Toast toast = Toast.makeText(context, String.valueOf(columnaTiempo), Toast.LENGTH_SHORT);
            toast.show();


            if (c != null && c.getCount() != 0) {

                if (c.isBeforeFirst()) {
                        c.moveToFirst();
                        int i = 0;
                        do {
                            i++;

                            String fecha = c.getString(columnaFecha);
                            String tiempo = c.getString(columnaTiempo);

                            resultats.add("" + i + ": " + fecha
                                    + " ( " + tiempo + " )");
                        } while (c.moveToNext());
                    }
                }
        } finally {
            if (baseDades != null) {
                baseDades.close();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resultats);

        t1.setAdapter(arrayAdapter);

    }
}
