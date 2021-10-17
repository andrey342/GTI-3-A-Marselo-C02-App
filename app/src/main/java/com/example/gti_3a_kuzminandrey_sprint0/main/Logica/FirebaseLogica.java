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

public class FirebaseLogica {

    private final FirebaseFirestore db;


    //constructor , siempre llamar antes de hacer las operaciones
    public FirebaseLogica() {
        db= FirebaseFirestore.getInstance();
    }

    //add new documento to a collection
    public void guardarMedicion(String collection, int id, int lectura, int user_id, double latX, double latY){
        if(collection!=null){
            Medicion medicion=new Medicion(id,new Timestamp(new Date()), lectura ,user_id, latX , latY );
            //si todos los datos existen
            db.collection(collection).add(medicion);
        }
    }

    //callback
    public interface obtenerUltimasMedicionesCallBack{
        void onCompletedC02(int C02);
        void onCompletedTemperatura(int temperatura);
    }
    public void obtenerUltimasMediciones(obtenerUltimasMedicionesCallBack obtenerUltimasMedicionesCallBack){

        db.collection("Mediciones CO2")
                .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        int C02=0;
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("lectura") != null) {
                                Medicion medicion=doc.toObject(Medicion.class);
                                C02 =medicion.getLectura();
                            }
                        }
                        obtenerUltimasMedicionesCallBack.onCompletedC02(C02);
                    }
                });

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

    //callback
    public interface obtenerTodasLasMedicionesCallBack{
        void onCompleted(List<Medicion> mediciones);
    }
    public void obtenerTodasLasMediciones(obtenerTodasLasMedicionesCallBack obtenerTodasLasMedicionesCallBack){
        db.collection("Mediciones CO2")
                .orderBy("fecha", Query.Direction.DESCENDING).limit(30)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<Medicion> mediciones= new ArrayList<Medicion>();
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("lectura") != null) {
                                Medicion medicion=doc.toObject(Medicion.class);
                                mediciones.add(medicion);
                            }
                        }
                        obtenerTodasLasMedicionesCallBack.onCompleted(mediciones);
                    }
                });
    }




}
