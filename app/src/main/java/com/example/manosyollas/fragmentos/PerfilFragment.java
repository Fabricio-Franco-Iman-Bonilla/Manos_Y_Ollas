package com.example.manosyollas.fragmentos;

import static android.app.Activity.RESULT_OK;
import static cz.msebera.android.httpclient.util.EncodingUtils.getBytes;
import static retrofit2.converter.gson.GsonConverterFactory.*;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manosyollas.R;
import com.example.manosyollas.Util.ManosyOllasSQLite;
import com.example.manosyollas.actividades.AjustesActivity;
import com.example.manosyollas.actividades.EditarPerfilActivity;
import com.example.manosyollas.actividades.InicioActivity;
import com.example.manosyollas.clases.ForumItem;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener{
    private final static int BOTONES []= {R.id.btnPerDona, R.id.btnPerNoti};
    private static final int REQUEST_CODE_SELECT_IMAGE = 1001;
    private Button btnCerrar;
    ImageButton btnAjustes,btnDonaciones;
    Button btnEditarPerfil;
    private ImageView imagenEditarPerfil;
    private static final String URL_MOSTRAR_USUARIO = "http://manosyollas.atwebpages.com/services/MostrarUsuarioxid.php";
    private SharedPreferences sharedPreferences;
    private ImageView imagenPerfil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int userId = 123;
    private String userUrl = null;
    private Uri uriImg;
    private String stringBase64;


    TextView perNombre, perCorreo, perApellido;
    Integer idUsuario;
    ImageView vperImagen;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button btnDona = vista.findViewById(R.id.btnPerDona);
        Button btnNoti = vista.findViewById(R.id.btnPerNoti);
        // Referencia al ImageView
        imagenEditarPerfil = vista.findViewById(R.id.vperImgEditar);
        imagenPerfil = vista.findViewById(R.id.vperImagen);
        sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("idUsuario", -1);

        mostrarImagen(userId);



        perCorreo = vista.findViewById(R.id.perCorreo);
        perNombre = vista.findViewById(R.id.perNombre);
        perApellido = vista.findViewById(R.id.perApellido);

        sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);

        if (idUsuario != -1) {
            mostrarUsuario(idUsuario);
        }

        btnCerrar = vista.findViewById(R.id.vperbtnCerrarSesion);
        btnEditarPerfil = vista.findViewById(R.id.vperbtnEditarPerfil);
        btnAjustes = vista.findViewById(R.id.vperimgajustes);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cerrarSesion();

            }
        });

        imagenEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarPermisos();
                abrirGaleria();

            }
        });

        btnEditarPerfil.setOnClickListener(this);
        btnAjustes.setOnClickListener(this);

        loadFragment(new HistDonacionFragment());

        btnDona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HistDonacionFragment());  // Cambiar a MenuListaFragment
            }
        });

        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ForosFragment());  // Cambiar a MenuMapaFragment
            }
        });
        return vista;
    }


    private void mostrarUsuario(int idUsuario) {
        String url = URL_MOSTRAR_USUARIO + "?idUsuario=" + idUsuario;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //vperImagen.setI
                    perNombre.setText(response.getString("nombre"));
                    perApellido.setText(response.getString("apellidos"));
                    perCorreo.setText(response.getString("correo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity().getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void cerrarSesion() {
        // 1. Eliminar los datos de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // Elimina todos los valores guardados (esto puede ajustarse según lo que quieras eliminar)
        editor.apply(); // Guarda los cambios

        // 2. Redirigir al usuario a la pantalla de inicio
        Intent intent = new Intent(getActivity(), InicioActivity.class);
        FirebaseAuth.getInstance().signOut();
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia la pila de actividades
        startActivity(intent);
        getActivity().finish(); // Cierra la actividad actual para prevenir el regreso con el botón de atrás
    }

    private void mostrarImagen(int idUsuario) {
        String url = URL_MOSTRAR_USUARIO + "?idUsuario=" + idUsuario;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("HttpRequest", "Status code: " + statusCode);  // Verifica el código de estado

                if (statusCode == 200) {
                    try {
                        if (!isAdded()) {
                            return; // Salir si el fragmento ya no está activo
                        }

                        userUrl = response.getString("avatar");


                        String intermedio= decodeBase64ToUtf8(userUrl);
                        Uri uri= convertBase64ToUri(intermedio);
                        imagenPerfil.setImageURI(uri);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity().getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e("Base64Error", "No se pudo decodificar la imagen: " + e.getMessage());
            return null; // Devuelve null si no puede decodificar
        }
    }

    public String convertStringToBase64(String input) {
        String holita="s";
        // Convierte el string a bytes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             holita=java.util.Base64.getEncoder().encodeToString(input.getBytes());
        }
        return holita;


    }



    private void verificarPermisos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    200); // Código de solicitud
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagenPerfil.setImageURI(selectedImageUri);
            //String imagePath = getRealPathFromURI(selectedImageUri);

            String uriString=convertUriToBase64(getContext(),selectedImageUri);

            subirImagen(userId, uriString); // Cambia el ID de usuario dinámicamente


        }
    }



    private String convertImageToBase64(Uri imageUri) throws IOException {
        // Obtener InputStream de la imagen
        InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            throw new IOException("Unable to open input stream for URI: " + imageUri);
        }

        // Leer el InputStream en un array de bytes
        byte[] imageBytes = getBytes(inputStream);
        // Convertir el array de bytes a Base64
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }




    // Método para actualizar la UI con la URL de la imagen (como ejemplo, en un ImageView)
    private void actualizarImagenPerfil(String imageUrl) {

        Glide.with(getContext())
                .load(imageUrl)
                .into(imagenPerfil);
    }


    // Método para obtener el camino real del archivo desde la URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }




    private void loadFragment(Fragment Fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frgContainerCambio, Fragment);  // Cambia el FrameLayout por el nuevo fragmento
        transaction.addToBackStack(null);  // Opción para agregar a la pila de retroceso
        transaction.commit();
    }
    public void onClick(View view) {
        if (view.getId() == R.id.vperimgajustes)
            ingresarAjustes();
        if (view.getId()==R.id.vperbtnEditarPerfil)
            editarPerfil();
    }
    private void editarPerfil() {
        Intent editarP = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(editarP);
    }
    private void ingresarAjustes() {
        Intent ajustes = new Intent(getActivity(), AjustesActivity.class);
        startActivity(ajustes);
    }
    private void subirImagen(int idUsuario, String uriImg) {
        String url = "http://manosyollas.atwebpages.com/services/subirImagen.php"; // Cambia a tu URL de subida

        // Crear cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();

        // Crear entidad con los datos del formulario
        RequestParams params = new RequestParams();
        params.put("idUsuario", idUsuario); // ID del usuario

        // Agregar la imagen al request
        params.put("image", uriImg);
        //params.put("image", base64);

        // Realizar la petición POST
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // Procesar la respuesta del servidor
                    String response = new String(responseBody);
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("message")) {
                        Toast.makeText(getContext(), "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                    } else if (jsonResponse.has("error")) {
                        Toast.makeText(getContext(), "Error: " + jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error procesando la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error al subir la imagen: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String convertUriToBase64(Context context, Uri uri) {
        try {
            // Obtener el ContentResolver
            ContentResolver contentResolver = context.getContentResolver();

            // Obtener un InputStream del archivo
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Convertir el InputStream a un arreglo de bytes
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }

                // Cerrar el InputStream
                inputStream.close();

                // Obtener los bytes del archivo
                byte[] fileBytes = byteArrayOutputStream.toByteArray();

                // Codificar los bytes en Base64
                return Base64.encodeToString(fileBytes, Base64.DEFAULT);
            } else {
                return null; // No se pudo abrir el archivo
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null; // En caso de error
        }
    }

    public static Uri convertBase64ToUri(String base64String) {
        try {
            // Decodificar la cadena Base64 a bytes
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

            // Crear un archivo temporal en el almacenamiento externo
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!directory.exists()) {
                directory.mkdirs(); // Crear el directorio si no existe
            }

            File file = new File(directory, "decoded_image.jpg");

            // Escribir los bytes en el archivo
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedBytes);
            fos.close();

            // Devolver el Uri del archivo
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // En caso de error
        }
    }
    public static String decodeBase64ToUtf8(String base64String) {
        // Decodificar la cadena Base64 a bytes
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

        // Convertir los bytes a una cadena UTF-8
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


}
