package com.example.manosyollas.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.manosyollas.R;
import com.example.manosyollas.actividades.PrincipalActivity;
import com.example.manosyollas.actividades.RegistrateActivity;
import com.example.manosyollas.actividades.SobreNosotrosActivity;
import com.example.manosyollas.actividades.SuministroActivity;
import com.example.manosyollas.actividades.TransferenciaActivity;
import com.example.manosyollas.clases.Menu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonacionesFragment extends Fragment implements View.OnClickListener, Menu {
    Button btnDonar, btnTransferencia;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DonacionesFragment() {

    }

    public static DonacionesFragment newInstance(String param1, String param2) {
        DonacionesFragment fragment = new DonacionesFragment();
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

    Fragment[] fragments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_donaciones, container, false);

        btnDonar = vista.findViewById(R.id.btnDonarSuministro);
        btnTransferencia = vista.findViewById(R.id.btnTransferencia);

        fragments = new Fragment[2];
        fragments[0] = new SuministroFragment();
        fragments[1] = new TransferenciaFragment();

        btnDonar.setOnClickListener(this);
        btnTransferencia.setOnClickListener(this);
        return vista;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDonarSuministro)
            DonarSum();
        if (view.getId() == R.id.btnTransferencia)
            Tranferencia();
    }

    private void Tranferencia() {
        int id = 1;
        onClickMenu(id);
    }

    private void DonarSum() {
        int id = 0;
        onClickMenu(id);
    }

    @Override
    public void onClickMenu(int id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menRelArea, fragments[id]);
        ft.commit();
    }

}