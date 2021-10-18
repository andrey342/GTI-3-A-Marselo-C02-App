package com.example.gti_3a_kuzminandrey_sprint0.main;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

// -----------------------------------------------------------------------------------
// @author: Andrey Kuzmin
// -----------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private Intent elIntentDelServicio = null;
    private static final int CODIGO_PETICION_PERMISOS = 11223344;
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    //layout palette
    private MaterialButton btn_play;
    private MaterialButton btn_stop;
    private TextView txt_temp;
    private TextView txt_co2;
    private RecyclerView rv_mediciones;
    private List<Medicion> medicionList=new ArrayList<>();
    private AdapterRvMediciones adapterRvMediciones;
    FirebaseLogica firebaseLogica;


    /**
     * La descripción de onCreate. Funcion responsable de crear el activity. En esta funcion inicializamos nuestras variables,
     * llamamos a los metodos de la logica para sacar datos a tiempo real , inicializamos el recyclerView y
     * le damos funcionalidad a los botones con el onClick.
     *
     * @param savedInstanceState Descripcion de param1.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findByid -> inicializar variables
        btn_play =findViewById(R.id.btn_play_service);
        btn_stop =findViewById(R.id.btn_stop_service);
        txt_temp=findViewById(R.id.txt_temp);
        txt_co2=findViewById(R.id.txt_co2);
        rv_mediciones=findViewById(R.id.rv_mediciones);

        //inicializamos la logica
        firebaseLogica=new FirebaseLogica();

        //llamamos a la logica para sacar las ultimas mediciones
        firebaseLogica.obtenerUltimasMediciones(new FirebaseLogica.obtenerUltimasMedicionesCallBack() {
            @Override
            public void onCompletedC02(int C02) {
                //mostramos valor en pantalla
                txt_co2.setText(String.valueOf(C02));
            }

            @Override
            public void onCompletedTemperatura(int temperatura) {
                //mostramos valor en pantalla
                txt_temp.setText(String.valueOf(temperatura));
            }
        });

        //RecyclerView mediciones
        //inicializamos rv
        initRvMediciones();
        //obtenemos los datos y los insertamos en el rv
        getItemMediciones();


        //boton iniciar servicio de busqueda de beacons
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comprobación de permisos
                if (
                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )
                { //sino tiene permisos
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                            CODIGO_PETICION_PERMISOS);
                }
                else { // si tiene permisos

                    if ( elIntentDelServicio != null ) {
                        // ya estaba arrancado
                        return;
                    }
                    Log.d(ETIQUETA_LOG, " MainActivity.constructor : voy a arrancar el servicio");

                    //inicializar servicio
                    elIntentDelServicio = new Intent(getApplicationContext(), ServicioEscuharBeacons.class);
                    elIntentDelServicio.putExtra("tiempoDeEspera", (long) 5000);
                    startService( elIntentDelServicio );
                    //mensaje que confirmación en pantalla
                    setSnackbar("Detención de beacons iniciada.");

                }
            }
        });

        //boton detener servicio
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metodo que para el servicio de detención de beacons
                botonDetenerServicioPulsado(v);
                //mensaje en pantalla
                setSnackbar("Detención de beacons detenida.");
            }
        });

    } // onCreate()

    /**
     * La descripción de initRvMediciones. Funcion que inicializa el RecyclerView, declara el RV como que tiene tamanyo
     * ampliable y le declara el Layout de sus items , si es vertical o horizontal.
     *
     */
    private void initRvMediciones(){
        //defino que el rv no tenga fixed size
        rv_mediciones.setHasFixedSize(false);
        rv_mediciones.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    } // ()

    /**
     * La descripción de getItemMediciones. Funcion que rellena el RecyclerView. Primero se limpia la lista para que
     * no se duplique y luego se consulta a la logica, que devuelve una lista de las mediciones ordenadas por fecha.
     * Por ultimo, le pasamos al constructor del adaptador del RecyclerView la lista y declaramos el adaptador.
     *
     */
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
    } // ()

    /**
     * La descripción de onRequestPermissionsResult. Funcion por defecto de android studio. Al pedir permisos se llama a
     * este metodo para pedir permisos
     *
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                }  else {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()


    /**
     * La descripción de botonDetenerServicioPulsado. Funcion que para el servicio de busqueda de beacons.
     *
     * @param v View del activity. Se le pasa desde el onClick de los botones en el onCreate
     *
     */
    private void botonDetenerServicioPulsado( View v ) {

        if ( this.elIntentDelServicio == null ) {
            // no estaba arrancado
            return;
        }

        //parar servicio
        stopService( this.elIntentDelServicio );
        //declaramos null
        this.elIntentDelServicio = null;

        Log.d(ETIQUETA_LOG, "SERVICIO DETENIDO" );

    } // ()

    /**
     * La descripción de setSnackbar. Funcion que muestra en pantalla un mensaje que acción.
     *
     * @param snackBarText String con el mensaje que queremos mostrar en pantalla.
     *
     */
    public void setSnackbar(String snackBarText){
        //creamos snackbar
        Snackbar snackBar = Snackbar.make( findViewById(R.id.layout_inicio), snackBarText,Snackbar.LENGTH_LONG);
        //color boton snackbar
        snackBar.setActionTextColor(Color.CYAN);
        //boton cerrar snackbar
        snackBar.setAction("Cerrar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        });
        //mostrar
        snackBar.show();
    } // ()
}