package com.example.gti_3a_kuzminandrey_sprint0.Main.Logica;

import com.example.gti_3a_kuzminandrey_sprint0.Main.POJO.Medicion;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class FirebaseLogica {

    private final FirebaseFirestore db;


    //constructor , siempre llamar antes de hacer las operaciones
    public FirebaseLogica() {
        db= FirebaseFirestore.getInstance();
    }

    //add new documento to a collection
    public void guardarMedicion(int id, int lectura, int user_id, double latX, double latY){

        Medicion medicion=new Medicion(id,new Timestamp(new Date()), lectura ,user_id, latX , latY );
        //si todos los datos existen
        db.collection("Mediciones").add(medicion);
    }


}
