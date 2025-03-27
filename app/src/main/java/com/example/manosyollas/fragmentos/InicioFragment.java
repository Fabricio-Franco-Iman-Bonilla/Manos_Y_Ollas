package com.example.manosyollas.fragmentos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manosyollas.R;
import com.example.manosyollas.actividades.ImageAdapter;
import com.example.manosyollas.actividades.ImageViewActivity;

import java.util.ArrayList;

public class InicioFragment extends Fragment {

    // Variables para los parámetros opcionales
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    // Método de fábrica para crear una nueva instancia del fragmento
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar el RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        ArrayList<Integer> arrayList = new ArrayList<>();

        // Agregar imágenes locales desde res/drawable
        arrayList.add(R.drawable.caritaslima);
        arrayList.add(R.drawable.donacion);
        arrayList.add(R.drawable.ollascomunes1);
        arrayList.add(R.drawable.caritaslima);
        arrayList.add(R.drawable.donacion);
        arrayList.add(R.drawable.ollascomunes1);

        // Configurar el adaptador
        ImageAdapter adapter = new ImageAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);

        // Manejar el clic en las imágenes del RecyclerView
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, int resourceId) {
                // Crear el Intent para iniciar ImageViewActivity
                Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                intent.putExtra("image", resourceId);

                // Configurar la transición
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), // Contexto de la actividad actual
                        imageView,    // Vista que será usada para la transición
                        "image"       // Nombre de la transición compartida
                );
                startActivity(intent, options.toBundle()); // Iniciar la nueva actividad con la transición
            }
        });

        return view;
    }
}
