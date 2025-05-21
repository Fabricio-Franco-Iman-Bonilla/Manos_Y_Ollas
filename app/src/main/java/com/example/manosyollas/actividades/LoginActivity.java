package com.example.manosyollas.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.manosyollas.R;
import com.example.manosyollas.fragmentos.InicioFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.security.MessageDigest;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

import java.security.MessageDigest;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText txtCorreo, txtContrasena;
    Button btnIngresar;
    CheckBox checkRecordar;
    ImageView icFlotante2;

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    private static final String URL_LOGIN = "http://manosyollas.atwebpages.com/services/Login_Usuario.php";
    private static final String URL_MOSTRAR_ID = "http://manosyollas.atwebpages.com/services/MostrarID.php";

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                Task<GoogleSignInAccount> accountTask=GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try{
                    GoogleSignInAccount signInAccount =accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                auth=FirebaseAuth.getInstance();
                                Glide.with(LoginActivity.this).load(Objects.requireNonNull(auth.getCurrentUser().getPhotoUrl()));

                                Toast.makeText(LoginActivity.this,"Sesión iniciada correctamente!!",Toast.LENGTH_SHORT).show();
                                // Inicio de sesión exitoso, redirigir a la siguiente Activity
                                Intent principal = new Intent(LoginActivity.this, MenuActivity.class);
                                principal.putExtra("id", 2);
                                startActivity(principal);
                                finish();
                                Log.d("Firebase", "EXITO CARAMBA");
                            }else{
                                Toast.makeText(LoginActivity.this,"Fallo al iniciar sesión con Google!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Firebase", e.getMessage());
                        }
                    });

                } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        txtCorreo = findViewById(R.id.logTxtEmail);
        txtContrasena = findViewById(R.id.logTxtPassword);
        btnIngresar = findViewById(R.id.LogBtnIngresar);
        checkRecordar = findViewById(R.id.logChkRecordar);
        icFlotante2 = findViewById(R.id.ic_flotante2);
        FirebaseApp.initializeApp(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient=GoogleSignIn.getClient(LoginActivity.this,options);

        auth=FirebaseAuth.getInstance();

        SignInButton signInButton = findViewById(R.id.signIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        //PROBE PROBE
        btnIngresar.setOnClickListener(this);
        icFlotante2.setOnClickListener(this);



    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.LogBtnIngresar) {

            SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", checkRecordar.isChecked()); // Save logged in state
            editor.apply();


            iniciarSesion(txtCorreo.getText().toString(), txtContrasena.getText().toString(), false);
        }else if(v.getId() == R.id.ic_flotante2) {
            Intent inicioIntent = new Intent(this, InicioActivity.class);
            startActivity(inicioIntent);
        }
    }
    private void iniciarSesion(String txtCorreo,String txtContrasena, boolean recordar) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //String clavehash=hashPassword(txtContrasena);
        params.put("email", txtCorreo);
        params.put("password", txtContrasena);

        client.post(URL_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("success") && response.getBoolean("success")) {
                        JSONObject usuario = response.getJSONObject("usuario");

                        int idUsuario = usuario.getInt("usuarioId"); // ✅ CORREGIDO
                        // Guarda el ID de usuario en SharedPreferences
                        obtenerIdUsuario(txtCorreo, idUsuario); // Pasa el idUsuario a obtenerIdUsuario

                        // Inicio de sesión exitoso, redirigir a la siguiente Activity
                        Intent principal = new Intent(LoginActivity.this, MenuActivityIA.class);
                        principal.putExtra("id", 2);
                        startActivity(principal);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(LoginActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerIdUsuario(String correo, int idUsuario) {
        // Guardar en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUsuario", idUsuario); // Usar el idUsuario pasado
        editor.putString("correo", correo);
        editor.apply();
    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}