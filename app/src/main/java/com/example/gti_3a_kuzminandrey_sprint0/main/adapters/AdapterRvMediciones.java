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

public class AdapterRvMediciones extends RecyclerView.Adapter<AdapterRvMediciones.RvMedicionesHolder> {

    private List<Medicion> medicionList=new ArrayList<>();
    private Context context;

    public AdapterRvMediciones(List<Medicion> medicionList, Context context) {
        this.medicionList = medicionList;
        this.context = context;
    }

    @NonNull
    @Override
    public RvMedicionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicion_item , parent,false);
        return new RvMedicionesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvMedicionesHolder holder, int position) {
        Medicion medicion= medicionList.get(position);

        holder.txt_lectura.setText(medicion.getLectura()+"%");
        holder.txt_direc.setText("X: "+medicion.getLatX()+" Y: " + medicion.getLatY());
        holder.txt_fecha.setText(medicion.getFecha().toDate().getDate()+"");

    }

    @Override
    public int getItemCount() {
        return medicionList.size();
    }

    public static class RvMedicionesHolder extends RecyclerView.ViewHolder{

        protected TextView txt_lectura;
        protected TextView txt_fecha;
        protected TextView txt_direc;

        public RvMedicionesHolder(@NonNull View itemView) {
            super(itemView);

            txt_lectura=itemView.findViewById(R.id.txt_lectura_medicion_item);
            txt_fecha=itemView.findViewById(R.id.txt_fecha_medicion_item);
            txt_direc=itemView.findViewById(R.id.txt_direc_medicion_item);
        }
    }
}
