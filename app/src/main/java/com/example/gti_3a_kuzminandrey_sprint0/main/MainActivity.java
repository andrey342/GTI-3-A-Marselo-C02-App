package com.example.gti_3a_kuzminandrey_sprint0.main;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gti_3a_kuzminandrey_sprint0.main.Logica.FirebaseLogica;
import com.example.gti_3a_kuzminandrey_sprint0.main.POJO.Medicion;
import com.example.gti_3a_kuzminandrey_sprint0.main.Service.ServicioEscuharBeacons;
import com.example.gti_3a_kuzminandrey_sprint0.R;
import com.example.gti_3a_kuzminandrey_sprint0.main.adapters.AdapterRvMediciones;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private Intent elIntentDelServicio = null;

    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo = null;

    //layout palette
    private MaterialButton btn_play;
    private MaterialButton btn_stop;
    private TextView txt_temp;
    private TextView txt_co2;
    private RecyclerView rv_mediciones;
    private List<Medicion> medicionList=new ArrayList<>();
    private AdapterRvMediciones adapterRvMediciones;
    FirebaseLogica firebaseLogica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findByid
        btn_play =findViewById(R.id.btn_play_service);
        btn_stop =findViewById(R.id.btn_stop_service);
        txt_temp=findViewById(R.id.txt_temp);
        txt_co2=findViewById(R.id.txt_co2);
        rv_mediciones=findViewById(R.id.rv_mediciones);

        firebaseLogica=new FirebaseLogica();
        firebaseLogica.obtenerUltimasMediciones(new FirebaseLogica.obtenerUltimasMedicionesCallBack() {
            @Override
            public void onCompletedC02(int C02) {
                txt_co2.setText(String.valueOf(C02));
            }

            @Override
            public void onCompletedTemperatura(int temperatura) {
                txt_temp.setText(String.valueOf(temperatura));
            }
        });

        //rv mediciones
        //config rv
        initRvMediciones();
        getItemMediciones();



        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )
                {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                            CODIGO_PETICION_PERMISOS);
                }
                else {
                    Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

                    if ( elIntentDelServicio != null ) {
                        // ya estaba arrancado
                        return;
                    }

                    Log.d(ETIQUETA_LOG, " MainActivity.constructor : voy a arrancar el servicio");

                    elIntentDelServicio = new Intent(getApplicationContext(), ServicioEscuharBeacons.class);

                    elIntentDelServicio.putExtra("tiempoDeEspera", (long) 5000);
                    startService( elIntentDelServicio );

                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonDetenerServicioPulsado(v);
            }
        });

    } // onCreate()


    private void initRvMediciones(){
        //defino que el rv no tenga fixed size
        rv_mediciones.setHasFixedSize(false);
        rv_mediciones.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    private void getItemMediciones(){
        //clear array
        medicionList.clear();

        firebaseLogica.obtenerTodasLasMediciones(new FirebaseLogica.obtenerTodasLasMedicionesCallBack() {
            @Override
            public void onCompleted(List<Medicion> mediciones) {
                medicionList=mediciones;
                adapterRvMediciones= new AdapterRvMediciones(medicionList, getApplicationContext());
                rv_mediciones.setAdapter(adapterRvMediciones);


            }
        });
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()


    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    private void botonDetenerServicioPulsado( View v ) {

        if ( this.elIntentDelServicio == null ) {
            // no estaba arrancado
            return;
        }

        stopService( this.elIntentDelServicio );

        this.elIntentDelServicio = null;

        Log.d(ETIQUETA_LOG, " boton detener servicio Pulsado" );


    } // ()
}