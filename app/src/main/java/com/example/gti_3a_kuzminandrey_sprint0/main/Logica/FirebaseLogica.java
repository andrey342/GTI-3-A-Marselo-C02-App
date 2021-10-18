package com.example.gti_3a_kuzminandrey_sprint0.main.Logica;

import com.example.gti_3a_kuzminandrey_sprint0.main.POJO.Medicion;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// -----------------------------------------------------------------------------------
// @author: Andrey Kuzmin
// -----------------------------------------------------------------------------------

//clase que contiene toda la logica de la aplicacion.
public class FirebaseLogica {

    //atributos
    private final FirebaseFirestore db;


    /**
     * La descripción de FirebaseLogica. Constructor de la clase, al llamarlo se instancia la base de datos
     * al hacer esto , se puede hacer peticiones.
     *
     */
    public FirebaseLogica() {
        db= FirebaseFirestore.getInstance();
    }

    /**
     * La descripción de guardarMedicion. Funcion que guarda la medicion en Cloud Firestore.
     * Guarda los datos que le pasamos en el objeto Medicion y se lo insertamos en firebase.
     *
     * @param collection nombre de la collecion a la que queremos anyadir la medicion.
     * @param id id de la medicion.
     * @param lectura lectura de la medicion.
     * @param user_id id del usario al que pertenece la medicion.
     * @param latX latitud.
     * @param latY longitud.
     *
     */
    public void guardarMedicion(String collection, int id, int lectura, int user_id, double latX, double latY){
        if(collection!=null){ //si tenemos nombre de la coleccion.
            //transformamos los datos en el objeto.
            Medicion medicion=new Medicion(id,new Timestamp(new Date()), lectura ,user_id, latX , latY );

            // collecion(NOMBRE) -> documento(ID automatico) medicion.class como campos de ese documento -> guardar
            db.collection(collection).add(medicion);
        }
    }

    /**
     * La descripción de obtenerUltimasMedicionesCallBack. Callback que almacena los valores de las lecturas.
     * Como las consultas de GET de Cloud Firestore son asincronas se usa este metodo para poder recuperar
     * el valor de la manera mas eficaz posible.
     *
     */
    public interface obtenerUltimasMedicionesCallBack{
        /**
         * La descripción de onCompletedC02. funcion que recibe constantemente el valor de la ultima lectura.
         *
         * @param C02 lectura de CO2
         *
         */
        void onCompletedC02(int C02);
        /**
         * La descripción de onCompletedTemperatura. funcion que recibe constantemente el valor de la ultima lectura.
         *
         * @param temperatura lectura de temperatura.
         *
         */
        void onCompletedTemperatura(int temperatura);
    }

    /**
     * La descripción de obtenerUltimasMediciones. Funcion que obtiene la medicion mas reciente de Cloud Firestore.
     *
     * @param obtenerUltimasMedicionesCallBack Callback para poder almacenar el valor.
     *
     */
    public void obtenerUltimasMediciones(obtenerUltimasMedicionesCallBack obtenerUltimasMedicionesCallBack){

        // collection("Mediciones CO2") -> Get all ordenado por fecha y con limite 1
        db.collection("Mediciones CO2")
                .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        //declaramos varible
                        int C02=0;
                        //for que recorre todos los documentos recuperados por la consulta
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("lectura") != null) { // si no es null
                                //transformamos el documento en objeto Medicion
                                Medicion medicion=doc.toObject(Medicion.class);
                                C02 =medicion.getLectura();
                            }
                        }
                        //le pasamos al callback el valor
                        obtenerUltimasMedicionesCallBack.onCompletedC02(C02);
                    }
                });

        //lo mismo que arriba pero para la coleccion temperatura.

        db.collection("Mediciones temperatura")
                .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }
                        int temperatura=0;
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("lectura") != null) {
                                Medicion medicion=doc.toObject(Medicion.class);
                                temperatura =medicion.getLectura();
                            }
                        }
                        obtenerUltimasMedicionesCallBack.onCompletedTemperatura(temperatura);
                    }
                });

    }

    /**
     * La descripción de obtenerTodasLasMedicionesCallBack. Callback que almacena la lista de mediciones.
     * Como las consultas de GET de Cloud Firestore son asincronas se usa este metodo para poder recuperar
     * el valor de la manera mas eficaz posible.
     *
     */
    public interface obtenerTodasLasMedicionesCallBack{
        /**
         * La descripción de onCompleted. funcion que recibe constantemente la lista de mediciones.
         *
         * @param mediciones lista de mediciones.
         *
         */
        void onCompleted(List<Medicion> mediciones);
    }

    /**
     * La descripción de obtenerTodasLasMediciones. Funcion que obtiene las utlimas 30 mediciones y las almacena
     * en una lista
     *
     * @param obtenerTodasLasMedicionesCallBack Callback para poder almacenar el valor.
     *
     */
    public void obtenerTodasLasMediciones(obtenerTodasLasMedicionesCallBack obtenerTodasLasMedicionesCallBack){

        // collection("Mediciones CO2") -> get los ultimos 30 documentos
        db.collection("Mediciones CO2")
                .orderBy("fecha", Query.Direction.DESCENDING).limit(30)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        //lista de mediciones
                        List<Medicion> mediciones= new ArrayList<Medicion>();
                        //for que recorre todos los docuemntos recuperados por la consulta
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("lectura") != null) { //si no es null la lectura
                                //transformamos el documento en objeto Medicion
                                Medicion medicion=doc.toObject(Medicion.class);
                                //lo anyadimos a la lista
                                mediciones.add(medicion);
                            }
                        }
                        //le pasamos la lista al callback
                        obtenerTodasLasMedicionesCallBack.onCompleted(mediciones);
                    }
                });
    }
    
}
