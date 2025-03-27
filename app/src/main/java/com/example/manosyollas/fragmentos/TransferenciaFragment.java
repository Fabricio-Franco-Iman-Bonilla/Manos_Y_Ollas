package com.example.manosyollas.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.Menu;

public class TransferenciaFragment extends Fragment implements Menu {

    Fragment[] fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transferencia, container, false);

        ImageView btnBackToDonaciones = view.findViewById(R.id.ic_flotante);
        ImageButton btnYape = view.findViewById(R.id.btn_yape);
        ImageButton btnPlin = view.findViewById(R.id.btn_plin);
        ImageButton btnVisa = view.findViewById(R.id.btn_visa);

        btnYape.setOnClickListener(v -> navigateToFragment(new YapeFragment()));
        btnPlin.setOnClickListener(v -> navigateToFragment(new PlinFragment()));
        btnVisa.setOnClickListener(v -> navigateToFragment(new VisaFragment()));

        fragments = new Fragment[2];
        fragments[0] = new SuministroFragment();
        fragments[1] = new DonacionesFragment();

        btnBackToDonaciones.setOnClickListener(v -> {
            volverDonaciones();
        });

        return view;
    }

    private void volverDonaciones() {
        int id = 1;
        onClickMenu(id);
    }

    private void navigateToFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.menRelArea, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickMenu(int id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menRelArea, fragments[id]);
        ft.commit();
    }
}

