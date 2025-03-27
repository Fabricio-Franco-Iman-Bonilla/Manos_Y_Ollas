package com.example.manosyollas.controladores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manosyollas.R;
import com.example.manosyollas.clases.Donacion;
import java.util.List;

public class DonacionAdaptador extends RecyclerView.Adapter<DonacionAdaptador.ViewHolder> {

    private List<Donacion> donaciones;

    public DonacionAdaptador(List<Donacion> donaciones) {
        this.donaciones = donaciones;
    }

    @NonNull
    @Override
    public DonacionAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout del item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionAdaptador.ViewHolder holder, int position) {
        // Obtiene la donación en la posición correspondiente
        Donacion donacion = donaciones.get(position);
        holder.productoTextView.setText(donacion.getProducto_nombre());
        holder.cantidadTextView.setText(donacion.getProducto_cantidad());
        holder.ollaNombreTextView.setText(donacion.getOlla_nombre());
    }

    @Override
    public int getItemCount() {
        return donaciones.size(); // Devuelve la cantidad de donaciones en la lista
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productoTextView;
        public TextView cantidadTextView;
        public TextView ollaNombreTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Asocia los TextViews con los elementos del layout
            productoTextView = itemView.findViewById(R.id.producto_nombre);
            cantidadTextView = itemView.findViewById(R.id.producto_cantidad);
            ollaNombreTextView = itemView.findViewById(R.id.olla_nombre);
        }
    }
}
