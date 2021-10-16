package com.example.gti_3a_kuzminandrey_sprint0.Main.Logica;

import com.example.gti_3a_kuzminandrey_sprint0.Main.POJO.Medicion;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseLogica {

    private final FirebaseFirestore db;


    //constructor , siempre llamar antes de hacer las operaciones
    public FirebaseLogica() {
        db= FirebaseFirestore.getInstance();
    }

    //add new documento to a collection
    public void AddMedicion(Medicion medicion){
        if(medicion != null){ //si todos los datos existen
            db.collection("Medicion").add(medicion);
        }
    }


}
