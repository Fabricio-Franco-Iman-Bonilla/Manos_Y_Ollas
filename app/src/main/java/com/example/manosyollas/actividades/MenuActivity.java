package com.example.manosyollas.actividades;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.Menu;
import com.example.manosyollas.fragmentos.DonacionesFragment;
import com.example.manosyollas.fragmentos.ForosFragment;
import com.example.manosyollas.fragmentos.InicioFragment;
import com.example.manosyollas.fragmentos.MenuLocalFragment;
import com.example.manosyollas.fragmentos.PerfilFragment;
import com.example.manosyollas.fragmentos.SuministroFragment;
import com.example.manosyollas.fragmentos.TransferenciaFragment;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements Menu {
    Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragments = new Fragment[7];
        fragments[0] = new PerfilFragment();
        fragments[1] = new MenuLocalFragment();
        fragments[2] = new InicioFragment();
        fragments[3] = new ForosFragment();
        fragments[4] = new DonacionesFragment();
        fragments[5] = new SuministroFragment();
        fragments[6] = new TransferenciaFragment();

        int id = getIntent().getIntExtra("id", -1);
        onClickMenu(id);
    }

    @Override
    public void onClickMenu(int id) {
        FragmentManager fr = getSupportFragmentManager();
        FragmentTransaction ft = fr.beginTransaction();
        ft.replace(R.id.menRelArea, fragments[id]);
        ft.setMaxLifecycle(fragments[id], Lifecycle.State.RESUMED);
        List<Fragment> fragments = fr.getFragments();
        Log.d("FragmentCount", "Active fragments: " + fragments.size());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // Retroceder al fragmento anterior
        } else {
            super.onBackPressed(); // Finalizar la actividad si no hay más fragmentos
        }
    }

}