package edu.stilgar.project01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.function.UnaryOperator;

public class PantallaJoc extends AppCompatActivity {

    static int size = 12;
    static int len = size * size;
    static int sizeWords = 5;

    ArrayList<String> a1 = new ArrayList<>();
    ArrayList<Integer> cont = new ArrayList<>();
    ArrayList<Integer> redpos = new ArrayList<>();
    String[] numbers = new String[len];
    ArrayList<String> contactosAL;
    TextView actual;
    ListView contact;
    Context context = this;
    String[] contactosFinales = new String[sizeWords];
    GridView gvS;
    String[] keyWords;
    int lastPos = 0;
    Chronometer cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources res = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_joc);
        keyWords = res.getStringArray(R.array.keywords);
        contact = (ListView) findViewById(R.id.NameList);
        actual = (TextView) findViewById(R.id.palabraActual);
        actual.setText("");
        getPhoneNumbers();
        vGeneraJuego();
        vStartChrono();
    }

    public void vGeneraJuego(){




        gvS = (GridView) findViewById(R.id.sopaFrame);
        gvS.setColumnWidth(size);
        gvS.setAdapter(new Addapter(context, contactosFinales, size));


        contactosAL = new ArrayList<String>(Arrays.asList(contactosFinales));
        contactosAL.replaceAll( new MyOperator() );
        vUpdateListView();


                gvS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int color = Color.TRANSPARENT;
                        Drawable background = view.getBackground();
                        if (background instanceof ColorDrawable)
                            color = ((ColorDrawable) background).getColor();

                            if (color == Color.RED){
                                if (!(redpos.contains(position))){
                                    redpos.add(position);
                                }
                            }

                            if (color == Color.BLUE) {
                                int lastA = a1.size()-1;
                                if (cont.get(lastA).equals(position)) {
                                    if (redpos.contains(position)){
                                        view.setBackgroundColor(Color.RED);
                                    }else {
                                        view.setBackgroundColor(Color.TRANSPARENT);
                                    }
                                    a1.remove(lastA);
                                    cont.remove((Integer) position);
                                    int lastPosTo = cont.size()-1;
                                    if (cont.size() != 0)
                                        lastPos = cont.get(lastPosTo);
                                }else{
                                    Toast toast = Toast.makeText(context, "Esta accion no se puede realizar", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } else {
                                if (isCorrect(lastPos, position) || cont.size() == 0){
                                view.setBackgroundColor(Color.BLUE);
                                a1.add(Addapter.getLetter(position));
                                cont.add(position);
                                    int lastPosTo = cont.size()-1;
                                    if (cont.size() != 0)
                                        lastPos = cont.get(lastPosTo);

                            }else{
                                    Toast toast = Toast.makeText(context,"Esta accion no se puede realizar" , Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        actual.setText(alToString(a1));
                        if (contactosAL.contains(alToString(a1))){
                            for (int i = 0; i < cont.size(); i++) {
                                gvS.getChildAt(cont.get(i)).setBackgroundColor(Color.RED);
                                Toast toast = Toast.makeText(context, "Palabra Encontrada!", Toast.LENGTH_SHORT);
                                toast.show();
                             }
                            lastPos=0;
                            cont.clear();
                            contactosAL.remove(alToString(a1));
                            vUpdateListView();
                            //TODO ADD THIS TO DATABASE

                            a1.clear();
                            actual.setText(alToString(a1));
                            if (contactosAL.isEmpty()){

                                Context contexto = getApplicationContext();
                                Toast toast = Toast.makeText(context, "ENHORABUENA! HAS FINALIZADO ESTE JUEGO", Toast.LENGTH_SHORT);
                                toast.show();
                               // SQLiteDatabase baseDades = null;
                                String currentCR = String.valueOf(cr.getText());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                               /* try{

                                final String TAULA = "SopaLetras";
                                    baseDades = context.openOrCreateDatabase("Sopa", MODE_PRIVATE, null);

                               baseDades.execSQL("INSERT INTO "
                                        + TAULA
                                        + " (fecha, tiempo)"
                                        + " VALUES ('" + currentDateandTime + "', '" + currentCR + "');");

                               /*ContentValues cv=new ContentValues();
                                    cv.put("fecha", currentDateandTime);
                                    cv.put("tiempo", currentCR);
                                    baseDades.insert(TAULA, null, cv);

                                } finally {
                                    if (baseDades != null) {
                                        baseDades.close();
                                    }
                                } */
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("time", currentCR);
                                intent.putExtra("date", currentDateandTime);
                                startActivity(intent);
                            }
                        }

                    }
                });

    }
    class MyOperator implements UnaryOperator<String>
    {
        @Override
        public String apply(String t) {
            return t.toUpperCase();
        }
    }

    public void vStartChrono(){
        cr = (Chronometer) findViewById(R.id.chrono);
        cr.start();
    }
    public static String alToString(ArrayList<String> al) {
        String result = "";
        for (int i = 0; i < al.size(); i++) {
            result += al.get(i).toUpperCase();
        }
        return result;
    }

    public boolean isCorrect(int Last, int position){
        int Correct = Last - position;

        switch (Correct){
            case 1:
            case -1:
            case 11:
            case 12:
            case 13:
            case -11:
            case -12:
            case -13:
                return true;
            default:
                return false;
        }

    }
    private void getPhoneNumbers() {
        try {

            ArrayList<String> contactos = new ArrayList<>();
            Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            int nameFieldColumnIndex = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            while(contacts.moveToNext()) {

                String contactName = contacts.getString(nameFieldColumnIndex);
                if (!(contactos.contains(contactName)))
                    contactos.add(contactName);


            }
            contacts.close();

            Random r1 = new Random();
            int nextRandom = r1.nextInt(contactos.size() + 1);

            ArrayList<Integer> repeat = new ArrayList<>();
            for (int counter = 0; counter < sizeWords; counter++){
                    while (repeat.contains(nextRandom)) {
                        nextRandom = r1.nextInt(contactos.size() + 1);
                    }
                    if (contactos.get(nextRandom).length() > 8){
                        counter--;
                    }else{
                        contactosFinales[counter] = contactos.get(nextRandom);
                    }

                repeat.add(nextRandom);
            }


        }catch (Exception e){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);

            getPhoneNumbers();
        }

    }

    public void vUpdateListView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                contactosAL);

        contact.setAdapter(arrayAdapter);
    }
}
