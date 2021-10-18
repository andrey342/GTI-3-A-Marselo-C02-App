package com.example.gti_3a_kuzminandrey_sprint0.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gti_3a_kuzminandrey_sprint0.R;
import com.example.gti_3a_kuzminandrey_sprint0.main.POJO.Medicion;

import java.util.ArrayList;
import java.util.List;

// -----------------------------------------------------------------------------------
// @author: Andrey Kuzmin
// -----------------------------------------------------------------------------------

//adaptador del RecyclerView
public class AdapterRvMediciones extends RecyclerView.Adapter<AdapterRvMediciones.RvMedicionesHolder> {

    //atributos
    private List<Medicion> medicionList=new ArrayList<>(); //lista de mediciones
    private Context context; //contexto


    /**
     * La descripción de AdapterRvMediciones. Constructor del adaptador.
     *
     * @param medicionList Lista de mediciones.
     * @param context contexto de la activity.
     *
     */
    public AdapterRvMediciones(List<Medicion> medicionList, Context context) {
        this.medicionList = medicionList;
        this.context = context;
    }

    /**
     * La descripción de onCreateViewHolder. Funcion por defecto de los adaptadores. Instancia la vista del item medicion
     *
     * @return Clase holder con la vista inicializada. Para que usar los atributos de esa clase.
     */
    @NonNull
    @Override
    public RvMedicionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicion_item , parent,false);
        return new RvMedicionesHolder(v);
    }

    /**
     * La descripción de onBindViewHolder. Funcion por defecto de los adaptadores. Declara la informacion que se le inserta
     * en el medicion_item.
     *
     */
    @Override
    public void onBindViewHolder(@NonNull RvMedicionesHolder holder, int position) {
        Medicion medicion= medicionList.get(position);

        holder.txt_lectura.setText(medicion.getLectura()+"");
        holder.txt_direc.setText("X: "+medicion.getLatX()+" Y: " + medicion.getLatY());
        holder.txt_fecha.setText("");

    }

    /**
     * La descripción de getItemCount. Funcion por defecto de los adaptadores. Funcion que devuelve el tamanyo del recyclerView.
     *
     * @return Devuelve el tamanyo de la lista de mediciones (lista que se mostrara en el recyclerView).
     */
    @Override
    public int getItemCount() {
        return medicionList.size();
    }

    /**
     *
     * Clase statica RvMedicionesHolder tipo Holder , la cual inicializa los atributos del layout medicion_item.
     *
     */
    public static class RvMedicionesHolder extends RecyclerView.ViewHolder{

        //atributos
        protected TextView txt_lectura;
        protected TextView txt_fecha;
        protected TextView txt_direc;

        /**
         * La descripción de RvMedicionesHolder. Constructor del Holder, en el cual inicializamos los atributos.
         *
         * @param itemView Vista del medicion_item.
         *
         * @return Descripcion del valor devuelto.
         */

        public RvMedicionesHolder(@NonNull View itemView) {
            super(itemView);
            //inicializamos los atributos
            txt_lectura=itemView.findViewById(R.id.txt_lectura_medicion_item);
            txt_fecha=itemView.findViewById(R.id.txt_fecha_medicion_item);
            txt_direc=itemView.findViewById(R.id.txt_direc_medicion_item);
        }
    }
}
