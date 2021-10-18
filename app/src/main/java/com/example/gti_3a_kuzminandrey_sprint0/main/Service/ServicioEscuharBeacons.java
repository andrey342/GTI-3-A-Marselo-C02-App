package com.example.gti_3a_kuzminandrey_sprint0.main.Service;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.util.Log;

import com.example.gti_3a_kuzminandrey_sprint0.main.Logica.FirebaseLogica;
import com.example.gti_3a_kuzminandrey_sprint0.main.TramaIBeacon;
import com.example.gti_3a_kuzminandrey_sprint0.main.Utilidades;

import java.util.ArrayList;
import java.util.List;

// -----------------------------------------------------------------------------------
// @author: Andrey Kuzmin
// -----------------------------------------------------------------------------------

//servicio que detecta beacons y llama a la logica para almacenar la medicion.
public class ServicioEscuharBeacons extends IntentService {

    //atributos
    private static final String ETIQUETA_LOG = ">>>>";
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;
    private long tiempoDeEspera = 10000;
    private boolean seguir = true;

    /**
     * La descripción de ServicioEscuharBeacons. Constructor.
     *
     */
    public ServicioEscuharBeacons(  ) {
        super("HelloIntentService");

        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.constructor: termina");

    }

    /**
     *
     * La descripción de onDestroy. Funcion por defecto de android studio. Detiene el servicio.
     *
     */
    public void onDestroy() {

        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.parar() " );

        if ( this.seguir == false ) {
            return;
        }
        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;
        this.seguir = false;
        this.stopSelf();
        Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.parar() : acaba " );
    }


    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
    /**
     * La descripción de onHandleIntent. Funcion por defecto, se llama al llamar el servicio.
     *
     * @param intent intent data que le pasamos al inicializar el servicio.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        //init bluetooth
        inicializarBlueTooth();
        this.tiempoDeEspera = intent.getLongExtra("tiempoDeEspera", /* default */ 50000);
        this.seguir = true;

        // esto lo ejecuta un WORKER THREAD !
        long contador = 1;

        //Init busqueda de beacons
        buscarTodosLosDispositivosBTLE();

        try {
            while ( this.seguir ) { //mientras esta permitido seguir
                Thread.sleep(tiempoDeEspera);
                Log.d(ETIQUETA_LOG, " ServicioEscucharBeacons.onHandleIntent: tras la espera:  " + contador );
                contador++;
            }

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    /**
     *
     * La descripción de inicializarBlueTooth. Funcion que inicia el bluetooth
     *
     */
    private void inicializarBlueTooth() {

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();

        if ( this.elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");
        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");
    } // ()

    /**
     * La descripción de buscarTodosLosDispositivosBTLE. Funcion que busca dispositivos beacons.
     *
     */
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                //mostramos info en el log y guardamos la medicion
                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        //filtro por nombre de beacon
        //los beacons que se escanean son pasados por este filtro para SOLO recuperar los beacons con un nombre especifico
        ScanFilter sf = new ScanFilter.Builder().setDeviceName( "Andrey-Kuzmin-G3A" ).build();
        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings.Builder scan = new ScanSettings.Builder();
        filters.add(sf);
        //le pasamos el filtro por nombre al scaneo
        this.elEscanner.startScan(filters, scan.build(), callbackDelEscaneo);

    } // ()

    /**
     * La descripción de mostrarInformacionDispositivoBTLE. Muestra informacion del beacon en el logcat
     * y llama a la logica para almacenar la medicion.
     *
     * @param resultado Descripcion de param1.
     */
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes); //11 c02 12 temperatura

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( DENGUEEEEEEE -> "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

        //llamamos al metodo que llama a la logica y le pasamos todos los datos necesarios de la medicion
        nuevaMedicion(1, Utilidades.bytesToInt(tib.getMajor()) ,  Utilidades.bytesToInt(tib.getMinor()) ,1, 1000.4 , 2323.6);

    } //


    /**
     * La descripción de nuevaMedicion. Funcion que recibe los datos de la medicion
     * y llama a la logica para almacenar la medicion en Cloud Firestore.
     *
     * @param id id de la medicion.
     * @param tipo tipo de medicion (11 es CO2 y 12 es temperatura)
     * @param lectura lectura de la medicion.
     * @param user_id id del usario de la medicion.
     * @param latX lat.
     * @param latY long.
     *
     */
    private void nuevaMedicion(int id, int tipo , int lectura, int user_id, double latX, double latY){

        //instanciamos la logica
        FirebaseLogica firebaseLogica= new FirebaseLogica();
        //string con el nombre de la coleccion donde vamos a guardar la medicion.
        String collection = "";

        //si el tipo de medicion es 11 o 12
        if(tipo == 11){ //C02
            //cambiamos nombre coleccion
            collection="Mediciones CO2";
        }else if (tipo == 12){ //TEMPERATURA
            //cambiamos nombre coleccion
            collection="Mediciones temperatura";
        }

        if(!collection.equals("")){
            //llamamos al metodo que guarda la medicion y le pasamos los datos
            firebaseLogica.guardarMedicion(collection, id, lectura ,user_id, latX , latY);
        }
    }
} // class
// -------------------------------------------------------------------------------------------------

