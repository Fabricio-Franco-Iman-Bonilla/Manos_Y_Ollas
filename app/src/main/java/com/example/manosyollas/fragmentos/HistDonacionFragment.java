package com.example.manosyollas.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manosyollas.R;
import com.example.manosyollas.controladores.DonacionAdaptador;
import com.example.manosyollas.clases.Donacion;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.List;

public class HistDonacionFragment extends Fragment {

    private RecyclerView recyclerDonaciones;
    private DonacionAdaptador donacionAdaptador;
    private List<Donacion> donacionesList;
    private Donacion donacion;
    private SharedPreferences sharedPreferences;
    Integer idUsuario;
    // URL para obtener las donaciones
    final static String urlMostrarDonaciones = "http://manosyollas.atwebpages.com/services/mostrardonaprobadaxid.php";

    public HistDonacionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_hist_donacion, container, false);



        // Configuración del RecyclerView
        recyclerDonaciones = vista.findViewById(R.id.recyclerDonaciones);
        recyclerDonaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        donacionesList = new ArrayList<>();
        donacionAdaptador = new DonacionAdaptador(donacionesList);
        recyclerDonaciones.setAdapter(donacionAdaptador);


        obtenerDonaciones();

        // Botón de volver
        ImageView btnVolver = vista.findViewById(R.id.ic_flotante);
        btnVolver.setOnClickListener(v -> selectFragment(new PerfilChatFragment()));

        return vista;
    }

    private void obtenerDonaciones() {
        sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);
        AsyncHttpClient ahcDonaciones = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("usuarioID", String.valueOf(idUsuario));

        // Hacer la petición HTTP para obtener las donaciones
        ahcDonaciones.post(urlMostrarDonaciones, params, new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {

                        JSONObject jsonResponse = new JSONObject(rawJsonResponse);


                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

                            JSONArray jsonArray = jsonResponse.getJSONArray("donaciones");

                            if (jsonArray.length() > 0) {
                                donacionesList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject donacionObj = jsonArray.getJSONObject(i);
                                    String producto = donacionObj.getString("producto_nombre");
                                    String cantidad = donacionObj.getString("producto_cantidad");
                                    String ollaNombre = donacionObj.getString("olla_nombre");

                                    Donacion donacion = new Donacion(producto, cantidad, ollaNombre);
                                    donacionesList.add(donacion);
                                }


                                donacionAdaptador.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "No se encontraron donaciones aprobadas", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error: No se pudo obtener las donaciones", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void selectFragment(Fragment fragment) {
        // Cambiar de fragmento
        FragmentTransaction t = getParentFragmentManager().beginTransaction();
        t.replace(R.id.menRelArea, fragment).commit();
    }
}
