package com.example.manosyollas.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.manosyollas.R;
import com.example.manosyollas.Util.ManosyOllasSQLite;
import com.example.manosyollas.clases.AppDatabase;
import com.example.manosyollas.clases.ForumItem;
import com.example.manosyollas.clases.OllaItem;
import com.example.manosyollas.controladores.ForumAdapter;
import com.example.manosyollas.controladores.OllaAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaFragment extends Fragment implements View.OnClickListener {
    private final static String urlMostrarOlla = "http://manosyollas.atwebpages.com/services/MostrarOlla.php";
    LinearLayout espacio, espacio1;

    private OllaAdapter ollaAdapter;
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaFragment newInstance(String param1, String param2) {
        ListaFragment fragment = new ListaFragment();
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
        View vista = inflater.inflate(R.layout.fragment_lista, container, false);
        espacio = vista.findViewById(R.id.ContenedorListaOllas);

        recyclerView = vista.findViewById(R.id.recyclerViewOllas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarOllasComunes();
        espacio.setOnClickListener(this);

        return vista;

    }

    private void cargarOllasComunes() {

        AppDatabase db = AppDatabase.getInstance(getContext());

        AsyncHttpClient ahcMostrarOllas = new AsyncHttpClient();

        ahcMostrarOllas.get(urlMostrarOlla, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        if (!isAdded()) {
                            return; // Salir si el fragmento ya no está activo
                        }
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(requireActivity().getApplicationContext());
                        dbHelper.deleteAllOllas(); // Elimina las Ollas antiguas

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Crear un nuevo objeto OllaItem
                            int ollaId = jsonObject.getInt("olla_id"); // Cambia a getInt
                            String nombre = jsonObject.getString("olla_nombre");
                            String description = jsonObject.getString("olla_descripcion");
                            String fecCreacion = jsonObject.getString("olla_fc");
                            String distrito = jsonObject.getString("nombre_distrito");
                            String zona = jsonObject.getString("zona_nombre");
                            String latitud = jsonObject.getString("latitud");
                            String longitud = jsonObject.getString("longitud");
                            String direccion = jsonObject.getString("direccion");

                            OllaItem ollaItem = new OllaItem(nombre,zona,direccion,latitud,longitud, R.drawable.ollita);
                            ollaItem.setDescription(description);
                            ollaItem.setNombreDistrito(distrito);
                            ollaItem.setFecCreacion(fecCreacion);
                            ollaItem.setOllaId(ollaId);

                            // Verificamos si la olla ya existe y la actualizamos
                            int rowsUpdated = dbHelper.updateOlla(ollaItem);

                            // Si no se actualizó ninguna fila (no existía la olla), la insertamos
                            if (rowsUpdated == 0) {
                                dbHelper.insertOlla(ollaItem);
                            }
                        }

                        cargarOllasLocalmente();

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

    private void cargarOllasLocalmente() {
        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(getActivity().getApplicationContext());
        List<OllaItem> ollaList = dbHelper.getAllOllas();

        ollaAdapter = new OllaAdapter(ollaList, new OllaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OllaItem olla) {
                abrirMapita(olla);
            }
        });

        recyclerView.setAdapter(ollaAdapter);
    }

    private void abrirMapita(OllaItem olla) {
        // Crea una instancia del MapsFragment
        MapsFragment mapsFragment = new MapsFragment();

        // Crea un Bundle para pasar las coordenadas
        Bundle bundle = new Bundle();
        bundle.putString("titulo", olla.getNombre());
        bundle.putDouble("latitud", Double.parseDouble(olla.getLatitud()));
        bundle.putDouble("longitud", Double.parseDouble(olla.getLongitud()));


        // Asigna el Bundle al fragmento
        mapsFragment.setArguments(bundle);

        // Reemplaza el fragmento actual por el MapsFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frgContainer, mapsFragment);
        transaction.addToBackStack(null); // Para poder volver al fragmento anterior
        transaction.commit();

    }




    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.espacioolla)
            enviarMapa();
    }
    private void enviarMapa() {
        // Crea una instancia del fragmento que deseas mostrar
        MapsFragment mapsFragment = new MapsFragment();
        // Utiliza el FragmentTransaction para reemplazar el fragmento actual con el nuevo fragmento
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        // Reemplaza el fragmento actual con MapaFragment
        //transaction.add(R.id.listaolla, mapaFragment);
        transaction.replace(R.id.frgContainer, mapsFragment);
        // Añade la transacción a la pila de retroceso (opcional)
        transaction.addToBackStack(null);
        // Ejecuta la transacción
        transaction.commit();
    }

}