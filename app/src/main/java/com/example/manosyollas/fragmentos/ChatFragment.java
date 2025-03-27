package com.example.manosyollas.fragmentos;
import static com.example.manosyollas.fragmentos.PerfilFragment.convertBase64ToUri;
import static com.example.manosyollas.fragmentos.PerfilFragment.decodeBase64ToUtf8;

import com.example.manosyollas.Util.ManosyOllasSQLite;
import com.example.manosyollas.actividades.MenuActivity;

import com.example.manosyollas.clases.AppDatabase;
import com.example.manosyollas.clases.MessageItem;
import com.example.manosyollas.controladores.MessageAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manosyollas.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private final static String URL_MOSTRAR_MENSAJES ="http://manosyollas.atwebpages.com/services/LeerMensajesPorForo.php";
    private final static String URL_CREAR_MENSAJES ="http://manosyollas.atwebpages.com/services/CrearMensaje.php";
    private static final String URL_MOSTRAR_USUARIO = "http://manosyollas.atwebpages.com/services/ObtenerImagen.php";
    private int foroId;
    private String foroNombre;
    private TextView foroTitle;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private MessageAdapter messageAdapter;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private TextView chatTitle;
    private int currentForumId;// ID del foro actual
    Integer idUsuario;
    private String avatar = null;
    String icon_predeterminado="";

    private List<MessageItem> messageList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentForumId = getArguments().getInt("FORUM_ID");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent menu = new Intent(requireActivity(), MenuActivity.class);
                menu.putExtra("nombre", "Cachimbo UPN");
                menu.putExtra("id", 3);
                startActivity(menu);
                requireActivity().finish();
            }
        });
    }

    //@SuppressLint("MissingInflatedId")
    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar mensajes en el RecyclerView
        //loadMessages();
        //cargarMensajes(int forumId);



        editTextMessage = view.findViewById(R.id.editTextMessage);

        buttonSend = view.findViewById(R.id.buttonSend);
        chatTitle = view.findViewById(R.id.chatTitle);
        ImageButton backButton = view.findViewById(R.id.backButton);


        // Evento para enviar el mensaje
        /*
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                MessageItem chatMessage = new MessageItem(messageText, true); // true indica que es el usuario quien envía
                chatMessageList.add(chatMessage);
                messageAdapter.notifyDataSetChanged();
                editTextMessage.setText("");
            }
        });

         */


        Bundle bundle = getArguments();
        if (bundle != null) {

            String title = bundle.getString("foroTitle", "Chat");
            foroId = Integer.parseInt(getArguments().getString("foroId"));

            // Set the icon and title
            chatTitle.setText(title);
        }
        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(getActivity().getApplicationContext());
        //dbHelper.deleteAllMensajes();
        cargarMensajesFromNube(foroId);


        // Cambiar el título en el Toolbar o ActionBar
        //volver
        // Configurar el evento click para el icono de retroceso
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getActivity(), MenuActivity.class);
                menu.putExtra("nombre","Cachimbo UPN");
                menu.putExtra("id", 3);
                startActivity(menu);
                requireActivity().finish();

            }
        });
        chatTitle.setOnClickListener(v-> {
            selectFragment(new HistDonUsuFragment());
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextMessage=view.findViewById(R.id.editTextMessage);
                sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
                idUsuario = sharedPreferences.getInt("idUsuario", -1);
                obtenerImagen(idUsuario);

                enviarMensaje(foroId,editTextMessage.getText().toString(),String.valueOf(idUsuario),icon_predeterminado,"Usuario2");
                editTextMessage.setText("");
                cargarMensajesFromNube(foroId); // Recargar mensajes después de enviar uno nuevo

            }
        });

        return view;
    }

    private void selectFragment(HistDonUsuFragment pf) {
        FragmentTransaction t = getParentFragmentManager().beginTransaction();
        t.replace(R.id.menRelArea,pf).commit();
    }




    private void enviarMensaje(int forumId, String content, String userId, String userProfileImage, String userName) {
        String messageId = UUID.randomUUID().toString(); // Generar un ID único para el mensaje
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        MessageItem messageItem = new MessageItem(messageId, forumId, content, userId, timestamp, userProfileImage, userName);
        messageItem.setPending(true); // Marcarlo como pendiente (debes agregar un campo en la clase `MessageItem`)

        // Actualizar la lista en el adaptador
        MessageAdapter holita = (MessageAdapter) recyclerView.getAdapter();
        if (holita != null) {
            holita.getMessageList().add(messageItem);
            holita.notifyItemInserted(holita.getMessageList().size() - 1);
            recyclerView.scrollToPosition(holita.getMessageList().size() - 1);
        }

        crearMensaje(forumId,Integer.parseInt(userId),content,holita,messageItem);

    }

    private void cargarMensajesFromNube(int idForo) {
        AppDatabase db = AppDatabase.getInstance(getContext());
        String url = URL_MOSTRAR_MENSAJES + "?idForo=" + idForo; // Asegúrate de que esta URL sea correcta
        AsyncHttpClient ahcMostrarMensajes = new AsyncHttpClient();

        ahcMostrarMensajes.get(url, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                //Log.d("MensajesResponse", rawJsonResponse); // Para depuración
                if (statusCode == 200) {
                    try {
                        if (rawJsonResponse.startsWith("{") || rawJsonResponse.startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(rawJsonResponse);
                            ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(getActivity().getApplicationContext());
                            dbHelper.deleteAllMensajes(); // Elimina los mensajes antiguos

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Crear un nuevo objeto MessageItem
                                String contenido = jsonObject.getString("contenido");
                                String idUsuario = jsonObject.getString("idUsuario");
                                String idMensaje = jsonObject.getString("idMensaje");
                                String nombre = jsonObject.getString("nombre");
                                String photo = jsonObject.getString("photo");
                                String fecha_envio = jsonObject.getString("fecha_envio");

                                //obtenerImagen(Integer.parseInt(idUsuario));

                                MessageItem messageItem = new MessageItem(idMensaje, idForo, contenido, idUsuario, fecha_envio, photo, nombre);

                                // Verificamos si el mensaje ya existe y lo actualizamos
                                int rowsUpdated = dbHelper.updateMessage(messageItem);

                                // Si no se actualizó ninguna fila (no existía el mensaje), lo insertamos
                                if (rowsUpdated == 0) {
                                    dbHelper.insertMessage(messageItem);
                                }
                            }
                            cargarChatsLocalmente(idForo);



                        } else {
                            //Log.e("MensajesResponse", "Respuesta inesperada: " + rawJsonResponse);
                            Toast.makeText(getContext(), "Respuesta del servidor no válida", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error en los datos recibidos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar mensajes: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + statusCode, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
        cargarChatsLocalmente(idForo);

    }

    private List<MessageItem> cargarChatsLocalmente(int forumId) {
        ManosyOllasSQLite dbHelper = new ManosyOllasSQLite(getActivity().getApplicationContext());
        List<MessageItem> messageList = dbHelper.getMessagesByForumId(forumId);
        sharedPreferences = getActivity().getSharedPreferences("IdUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", -1);
        MessageAdapter messageAdapter = new MessageAdapter(messageList,String.valueOf(idUsuario));


        recyclerView.setAdapter(messageAdapter);



        // Desplazar el RecyclerView hasta el último mensaje
        if (messageList.size() > 0) {
            recyclerView.scrollToPosition(messageList.size() - 1);
        }
        return messageList;

    }
    private void crearMensaje(int idForo, int idUsuario, String contenido,MessageAdapter adapter,MessageItem messageItem) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("idForo", idForo);
        params.put("idUsuario", idUsuario);
        params.put("contenido", contenido);


        client.post(URL_CREAR_MENSAJES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(getContext(), "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
                        int index = adapter.getMessageList().indexOf(messageItem);
                        if (index != -1) {
                            adapter.getMessageList().get(index).setPending(false);
                            adapter.notifyItemChanged(index);
                        }


                    } else {
                        Toast.makeText(getContext(), "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void obtenerImagen(int idUsuario) {
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

                        icon_predeterminado = response.getString("avatar");


                        //String intermedio= decodeBase64ToUtf8(avatar);
                        //Uri uri= convertBase64ToUri(intermedio);
                        //imagenPerfil.setImageURI(uri);
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




}