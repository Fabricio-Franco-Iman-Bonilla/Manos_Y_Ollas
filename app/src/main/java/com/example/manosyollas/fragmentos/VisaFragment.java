package com.example.manosyollas.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.Menu;

public class VisaFragment extends Fragment implements Menu {

    Fragment[] fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visa, container, false);

        fragments = new Fragment[2];
        fragments[0] = new SuministroFragment();
        fragments[1] = new TransferenciaFragment();

        ImageView icVolver = view.findViewById(R.id.ic_flotante);
        icVolver.setOnClickListener(v -> {
            volverTransferencias();
        });

        return view;
    }

    private void volverTransferencias() {
        int id = 1;
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
