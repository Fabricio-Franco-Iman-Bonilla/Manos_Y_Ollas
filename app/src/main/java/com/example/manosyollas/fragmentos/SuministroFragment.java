package com.example.manosyollas.fragmentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manosyollas.R;
import com.example.manosyollas.actividades.ConfirmarActivity;
import com.example.manosyollas.actividades.MenuActivity;
import com.example.manosyollas.clases.Menu;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuministroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuministroFragment extends Fragment implements Menu {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuministroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuministroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuministroFragment newInstance(String param1, String param2) {
        SuministroFragment fragment = new SuministroFragment();
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


    private ArrayList<String> itemsSeleccionados;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "SeleccionPrefs";
    private static final String KEY_ITEMS = "itemsSeleccionados";
    private boolean goingToConfirmarActivity = false;

    View vi;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vi =  inflater.inflate(R.layout.fragment_suministro, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(vi.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        itemsSeleccionados = new ArrayList<>(preferences.getStringSet(KEY_ITEMS, new HashSet<>()));

        ImageView btnVolver = vi.findViewById(R.id.ic_flotante);
        btnVolver.setOnClickListener(v1 -> {
            Intent principal = new Intent(vi.getContext(), MenuActivity.class);
            principal.putExtra("id", 4);
            startActivity(principal);
        });

        configurarProductos(vi);

        restaurarSeleccion(vi);

        Button btnAceptar = vi.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(v -> {
            if (itemsSeleccionados.isEmpty()) {
                Toast.makeText(requireContext(), "No se ha seleccionado ningún ítem", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(KEY_ITEMS, new HashSet<>(itemsSeleccionados));
            editor.apply();

            goingToConfirmarActivity = true;

            Intent intent = new Intent(requireContext(), ConfirmarActivity.class);
            intent.putStringArrayListExtra("itemsSeleccionados", itemsSeleccionados);
            startActivity(intent);
            requireActivity().finish();
        });

        return vi;
    }

    private void restaurarSeleccion(View v) {
        if (itemsSeleccionados.contains("Arroz - 5kg")) {
            ImageView icArroz = v.findViewById(R.id.ic_arroz);
            icArroz.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Aceite - 1L")) {
            ImageView icAceite = v.findViewById(R.id.ic_aceite);
            icAceite.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Harina - 3kg")) {
            ImageView icHarina = v.findViewById(R.id.ic_harina);
            icHarina.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Sal - 10kg")) {
            ImageView icSal = v.findViewById(R.id.ic_sal);
            icSal.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Azúcar - 2kg")) {
            ImageView icAzucar = v.findViewById(R.id.ic_azucar);
            icAzucar.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Leche - 1L")) {
            ImageView icLeche = v.findViewById(R.id.ic_leche);
            icLeche.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Fideos - 500g")) {
            ImageView icFideos = v.findViewById(R.id.ic_fideos);
            icFideos.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Lentejas - 1kg")) {
            ImageView icLentejas = v.findViewById(R.id.ic_lentejas);
            icLentejas.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Conservas - 400g")) {
            ImageView icConservas = v.findViewById(R.id.ic_conservas);
            icConservas.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Huevos - docena")) {
            ImageView icHuevos = v.findViewById(R.id.ic_huevos);
            icHuevos.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Atún - lata")) {
            ImageView icAtun = v.findViewById(R.id.ic_atun);
            icAtun.setAlpha(0.5f);
        }
        if (itemsSeleccionados.contains("Cereales - 500g")) {
            ImageView icCereales = v.findViewById(R.id.ic_cereales);
            icCereales.setAlpha(0.5f);
        }
    }
    private void configurarProductos(View v) {
        ImageView icArroz = v.findViewById(R.id.ic_arroz);
        ImageView icAceite = v.findViewById(R.id.ic_aceite);
        ImageView icHarina = v.findViewById(R.id.ic_harina);
        ImageView icSal = v.findViewById(R.id.ic_sal);
        ImageView icAzucar = v.findViewById(R.id.ic_azucar);
        ImageView icLeche = v.findViewById(R.id.ic_leche);
        ImageView icFideos = v.findViewById(R.id.ic_fideos);
        ImageView icLentejas = v.findViewById(R.id.ic_lentejas);
        ImageView icConservas = v.findViewById(R.id.ic_conservas);
        ImageView icHuevos = v.findViewById(R.id.ic_huevos);
        ImageView icAtun = v.findViewById(R.id.ic_atun);
        ImageView icCereales = v.findViewById(R.id.ic_cereales);

        icArroz.setOnClickListener(view -> toggleSelection("Arroz - 5kg", icArroz));
        icAceite.setOnClickListener(view -> toggleSelection("Aceite - 1L", icAceite));
        icHarina.setOnClickListener(view -> toggleSelection("Harina - 3kg", icHarina));
        icSal.setOnClickListener(view -> toggleSelection("Sal - 10kg", icSal));
        icAzucar.setOnClickListener(view -> toggleSelection("Azúcar - 2kg", icAzucar));
        icLeche.setOnClickListener(view -> toggleSelection("Leche - 1L", icLeche));
        icFideos.setOnClickListener(view -> toggleSelection("Fideos - 500g", icFideos));
        icLentejas.setOnClickListener(view -> toggleSelection("Lentejas - 1kg", icLentejas));
        icConservas.setOnClickListener(view -> toggleSelection("Conservas - 400g", icConservas));
        icHuevos.setOnClickListener(view -> toggleSelection("Huevos - docena", icHuevos));
        icAtun.setOnClickListener(view -> toggleSelection("Atún - lata", icAtun));
        icCereales.setOnClickListener(view -> toggleSelection("Cereales - 500g", icCereales));
    }

    private void toggleSelection(String item, ImageView imageView) {
        if (itemsSeleccionados.contains(item)) {
            itemsSeleccionados.remove(item);
            imageView.setAlpha(1.0f);
            Toast.makeText(vi.getContext(), item + " deseleccionado", Toast.LENGTH_SHORT).show();
        } else {
            itemsSeleccionados.add(item);
            imageView.setAlpha(0.5f);
            Toast.makeText(vi.getContext(), item + " seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!goingToConfirmarActivity) {
            preferences.edit().clear().apply();
        }
    }


    @Override
    public void onClickMenu(int id) {
        Intent menu = new Intent(vi.getContext(), MenuActivity.class);
        menu.putExtra("id", id);
        startActivity(menu);
    }
}