package com.example.manosyollas.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.manosyollas.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuLocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuLocalFragment extends Fragment {
    private final static int BOTONES []= {R.id.btnmenuLista, R.id.btnmenuMapa};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuLocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuLocalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuLocalFragment newInstance(String param1, String param2) {
        MenuLocalFragment fragment = new MenuLocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_menu_local, container, false);

        Button btnLista = vista.findViewById(R.id.btnmenuLista);
        Button btnMapa = vista.findViewById(R.id.btnmenuMapa);

        loadFragment(new ListaFragment());

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ListaFragment());  // Cambiar a MenuListaFragment
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MapsFragment());  // Cambiar a MenuMapaFragment
            }
        });

        /*Button Boton;
        for (int i=0; i< BOTONES.length; i++){
            Boton = vista.findViewById(BOTONES[i]);
            final  int ID = i;
            Boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = getActivity();
                    ((MenuLocal)activity).onClickMenuLocal(ID);
                }
            });
        }*/
        return vista;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frgContainer, fragment);  // Cambia el FrameLayout por el nuevo fragmento
        transaction.addToBackStack(null);  // Opción para agregar a la pila de retroceso
        transaction.commit();
    }
}