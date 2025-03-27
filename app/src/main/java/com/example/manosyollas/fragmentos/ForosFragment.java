package com.example.manosyollas.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manosyollas.R;
import com.example.manosyollas.Util.ManosyOllasSQLite;
import com.example.manosyollas.clases.AppDatabase;
import com.example.manosyollas.clases.ForumItem;
import com.example.manosyollas.controladores.ForumAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForosFragment extends Fragment {
    private final static String URL_MOSTRAR_FOROS ="http://manosyollas.atwebpages.com/services/MostrarForosPorUsuario.php";
    private RecyclerView recyclerView;
    private ForumAdapter forumAdapter;
    private SharedPreferences sharedPreferences;
    Integer idUsuario;
    private List<ForumItem> forumItemList = new ArrayList<>();
    private View bottomNavBar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForosFragment newInstance(String param1, String param2) {
        ForosFragment fragment = new ForosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_foros, container, false);

        //super.onCreate(savedInstanceState);
        bottomNavBar= vista.findViewById(R.id.ContenedorLista);
        sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);

        recyclerView = vista.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear y agregar ítems a la lista
        muestranavegacion();
        cargarForos(idUsuario);
        // Añadir más ítems según sea necesario



        return  vista;
    }

    public void ocultanavegacion() {
        //bottomNavBar.setVisibility(View.GONE);
    }
    public void muestranavegacion() {
        //bottomNavBar.setVisibility(View.VISIBLE);
    }

    private void abrirChat(ForumItem foro) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChatFragment chatFragment = new ChatFragment();

        // Aquí puedes pasar datos del foro seleccionado
        Bundle args = new Bundle();
        args.putString("foroId", String.valueOf(foro.getForoId()));



        args.putString("foroTitle", foro.getTitle());
        chatFragment.setArguments(args);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ForoPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("foroId", String.valueOf(foro.getForoId()));
        editor.apply();



        //fragmentTransaction.add(chatFragment,"s");
        fragmentTransaction.replace(R.id.menRelArea, chatFragment);
        //fragmentTransaction.(R.id.menRelArea, chatFragment);
        //fragmentTransaction.add(R.id.menRelArea, chatFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void cargarForos(int idUsuario) {
        String url = URL_MOSTRAR_FOROS + "?idUsuario=" + idUsuario;
        AppDatabase db = AppDatabase.getInstance(getContext());

        AsyncHttpClient ahcMostrarForos = new AsyncHttpClient();

        ahcMostrarForos.get(url, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        if (!isAdded()) {
                            return; // Salir si el fragmento ya no está activo
                        }
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(requireActivity().getApplicationContext());
                        dbHelper.deleteAllForos(); // Elimina los foros antiguos

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Crear un nuevo objeto ForumItem
                            int foroId = jsonObject.getInt("idForo"); // Cambia a getInt
                            String title = jsonObject.getString("titulo");
                            String description = jsonObject.getString("descripcion");
                            String fecCreacion = jsonObject.getString("fecha_creacion");
                            String rol = jsonObject.getString("rol");

                            ForumItem forumItem = new ForumItem(title, description, R.drawable.ollita);
                            forumItem.setForoId(foroId);
                            forumItem.setRol(rol);
                            forumItem.setFecCracion(fecCreacion);

                            // Verificamos si el foro ya existe y lo actualizamos
                            int rowsUpdated = dbHelper.updateForum(forumItem);

                            // Si no se actualizó ninguna fila (no existía el foro), lo insertamos
                            if (rowsUpdated == 0) {
                                dbHelper.insertForum(forumItem);
                            }
                        }

                        cargarForosLocalmente();

                    } catch (JSONException e) {
                        e.printStackTrace(); // Cambiado para imprimir el stack trace
                        Toast.makeText(getContext(), "Error en los datos recibidos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar foros: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }


        });
    }


    private void cargarForosLocalmente() {
        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(getActivity().getApplicationContext());
        List<ForumItem> forumList = dbHelper.getAllForos();

        forumAdapter = new ForumAdapter(forumList, new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ForumItem foro) {
                ocultanavegacion();
                abrirChat(foro);
            }
        });

        recyclerView.setAdapter(forumAdapter);
    }
}