package com.example.manosyollas.actividades;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.ApiService;
import com.example.manosyollas.clases.ResponseData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetectionActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String currentPhotoPath;
    private TextView textViewResultado;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.13:5000/") // Reemplaza con la IP de tu PC
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestPermissions();
        textViewResultado = findViewById(R.id.textViewResultado);
        Button botoncito=findViewById(R.id.btnCapture);

        botoncito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openCamera();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 100);
            }

        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri; // Para almacenar la ruta de la imagen

    public void openCamera() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.manosyollas.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Crear un nombre único para el archivo
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Directorio para almacenar la imagen
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // Nombre del archivo
                ".jpg",         // Extensión
                storageDir      // Directorio
        );
        imageUri = FileProvider.getUriForFile(this,
                getPackageName() + ".fileprovider", image);

        // Guardar la ruta absoluta
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // La captura fue exitosa
            Toast.makeText(this, "Imagen capturada con éxito", Toast.LENGTH_SHORT).show();

            // Usa la URI de la imagen para mostrarla o procesarla
            if (imageUri != null) {
                // Mostrar la imagen capturada
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(imageUri);
                enviarImagenRetrofit();
                //uploadImage(imageUri);
            }
        } else {
            // Si el resultado no es OK, muestra un mensaje de error
            Toast.makeText(this, "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
/*
    private void uploadImage(Uri imagenUri) {
        try {
            // Convertir la imagen a Base64
            String imagenBase64 = convertirImagenABase64(imagenUri);

            // Agregar el prefijo requerido por el servidor
            String prefijoBase64 = "data:image/jpeg;base64,";
            JsonObject body = new JsonObject();
            //body.addProperty("imagen", prefijoBase64 + imagenBase64);


            // Si el JSON fue creado exitosamente

                Call<JsonObject> call = apiService.uploadImage(body);
            // Hacer la solicitud
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        // Manejar respuesta exitosa
                        Log.d("API_RESPONSE", "Datos recibidos: " + response.body());
                    } else {
                        // Manejar errores en la respuesta
                        Log.e("API_ERROR", "Error en la solicitud: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // Manejar errores de conexión
                    Log.e("API_FAILURE", "Error en la conexión: ", t);
                }
            });
        } catch (FileNotFoundException e) {
            // Manejar error si no se puede acceder a la URI de la imagen
            Log.e("ERROR", "Archivo no encontrado: ", e);
        }
    }*/

    public String convertirImagenABase64(Uri imagenUri) throws FileNotFoundException {
        // Obtener el Bitmap desde la URI de la imagen
        ContentResolver resolver = getContentResolver();
        InputStream inputStream = resolver.openInputStream(imagenUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Convertir el Bitmap a Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // O PNG si lo prefieres
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void enviarImagenRetrofit() {
        try (InputStream is = getContentResolver().openInputStream(imageUri);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BitmapFactory.decodeStream(is)
                    .compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] fotoBytes = baos.toByteArray();

            RequestBody reqFile = RequestBody.create(
                    fotoBytes, MediaType.parse("image/jpeg")
            );
            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "imagen", "foto.jpg", reqFile
            );
            Log.d("APP", "Enviando imagen: URI=" + imageUri);
            Log.d("APP", "Bytes de imagen: " + fotoBytes.length);
            //Log.d("APP", "Multipart: " + body.partName() + " / " + body.body().contentType());

            apiService.uploadImage(body).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> c, Response<ResponseData> r) {
                    if (r.isSuccessful() && r.body() != null) {
                        ResponseData d = r.body();
                        String txt = String.format(
                                "Etiqueta: %s\nConfianza: %.2f",
                                d.getPrediccion(), d.getConfianza());
                        runOnUiThread(() -> textViewResultado.setText(txt));
                    } else {
                        runOnUiThread(() ->
                                textViewResultado.setText("Error servidor: " + r.code()));
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> c, Throwable t) {
                    runOnUiThread(() ->
                            textViewResultado.setText("Error red: " + t.getMessage()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            textViewResultado.setText("Error leyendo imagen: " + e.getMessage());
        }
    }
}