package com.example.manosyollas.fragmentos;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manosyollas.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilChatFragment extends Fragment {
    TextView txtdonacion;
    ImageView btnVolver;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilChatFragment newInstance(String param1, String param2) {
        PerfilChatFragment fragment = new PerfilChatFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_perfil_chat, container, false);
        txtdonacion = vista.findViewById(R.id.chatPerfDonacionTex);
        txtdonacion.setOnClickListener(v-> {
            selectFragment(new HistDonUsuFragment());
        });
        btnVolver = vista.findViewById(R.id.ic_flotante);
        btnVolver.setOnClickListener(v1-> {
            selectFragment(new ForosFragment());
        });
        return vista;
    }

    private void selectFragment(ForosFragment ff) {
        FragmentTransaction t = getParentFragmentManager().beginTransaction();
        t.replace(R.id.menRelArea,ff).commit();
    }

    private void selectFragment(HistDonUsuFragment hdf) {
        FragmentTransaction t = getParentFragmentManager().beginTransaction();
        t.replace(R.id.menRelArea,hdf).commit();

    }

}