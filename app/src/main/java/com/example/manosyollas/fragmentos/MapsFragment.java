package com.example.manosyollas.fragmentos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manosyollas.R;
import com.example.manosyollas.Util.ManosyOllasSQLite;
import com.example.manosyollas.clases.AppDatabase;
import com.example.manosyollas.clases.ForumItem;
import com.example.manosyollas.controladores.ForumAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapsFragment extends Fragment {
    private final static String urlMostrarDirecciones = "http://manosyollas.atwebpages.com/services/MostrarOlla.php ";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 10);
            }
            else{
                googleMap.setMyLocationEnabled(true);
                }

            // Bandera para verificar si el Bundle procesó un marcador válido
            boolean isBundleProcessed = false;

            // Obtén las coordenadas del Bundle
            Bundle bundle = getArguments();
            if (bundle != null) {
                String titulo= bundle.getString("titulo","");
                double latitud = bundle.getDouble("latitud", 0.0);
                double longitud = bundle.getDouble("longitud", 0.0);
                // Centra el mapa en las coordenadas recibidas
                LatLng posicion = new LatLng(latitud, longitud);
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(posicion)
                        .title("Ubicación Seleccionada: "+titulo)); // Título del marcador



                // Muestra la ventana de información automáticamente
                if (marker != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15)); // Zoom nivel 15
                    marker.showInfoWindow();
                    isBundleProcessed = true; // Marca que el Bundle fue procesado
                }
            }

            AsyncHttpClient ahcMostrarUbi = new AsyncHttpClient();

            boolean finalIsBundleProcessed = isBundleProcessed;
            ahcMostrarUbi.get(urlMostrarDirecciones, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if(statusCode == 200){
                        LatLng lugar;
                        Marker marker;
                        String titulo;
                        double latitud, longitud;
                        int olla_id;


                        try {
                            LatLng primerLugar = null;
                            JSONArray jsonArray = new JSONArray(rawJsonResponse);
                            for(int i=0; i<jsonArray.length();i++){
                                latitud = jsonArray.getJSONObject(i).getDouble("latitud");
                                longitud = jsonArray.getJSONObject(i).getDouble("longitud");
                                lugar =new LatLng(latitud,longitud);
                                titulo = jsonArray.getJSONObject(i).getString("olla_nombre");
                                marker = googleMap.addMarker(new MarkerOptions().position(lugar).title(titulo));
                                olla_id = jsonArray.getJSONObject(i).getInt("olla_id");
                                switch (olla_id){
                                    case 1: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                    case 2: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                    case 3: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                    case 4: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                    case 5: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                    case 6: marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.olla2));break;
                                }
                                if(i==0)
                                    primerLugar=lugar;
                            }
                            // Si el Bundle no procesó una posición, centra en el primer marcador
                            if (!finalIsBundleProcessed && primerLugar != null) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(primerLugar, 13)); // Ajusta el zoom según sea necesario
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    Toast.makeText(getContext(),"ERROR: "+statusCode,Toast.LENGTH_LONG).show();
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });




        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}