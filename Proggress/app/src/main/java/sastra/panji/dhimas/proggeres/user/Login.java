package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sastra.panji.dhimas.proggeres.Model.Peternak;
import sastra.panji.dhimas.proggeres.R;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    ProgressBar loading;
    TextView daftar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getCurrentFirebaseToken();
        daftar = findViewById(R.id.daftar);
        username = findViewById(R.id.lg_username);
        password = findViewById(R.id.lg_password2);
        login = findViewById(R.id.btn_login);
        loading = findViewById(R.id.loading);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Daftar.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (!Username.isEmpty() || !Password.isEmpty()) {

                    Login(Username, Password);
                } else {
                    username.setError("Masukkan Username");
                    password.setError("Masukkan Password");
                }
            }
        });


    }

    private void Login(final String user, final String pass) {
        loading.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        String loginnya = "api/peternak/auth/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, R.string.SERVER + loginnya, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response.toString());
                    JSONObject object = new JSONObject(response);
                    boolean succes = object.getBoolean("status");
                    Peternak peternak = new Peternak();
                    if (succes) {

                        String id = object.getString("id");
                        String nm = object.getString("nama");
                        String tk = object.getString("Token");
                        String img = object.getString("images");
                        loading.setVisibility(View.GONE);
                        peternak.setId(Integer.parseInt(id));
                        peternak.setNama(nm);
                        peternak.setToken(tk);
                        peternak.setPhoto(img);

                        Toast.makeText(Login.this, "Selamat Datang " + nm, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, ListKandang.class);
                        intent.putExtra(ListKandang.EXTRA_MOVIES, peternak);
                        startActivity(intent);
                        finish();


                    } else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Login Gagal!", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    loading.setVisibility(View.GONE);

                    login.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            "Time Out!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(),
                            "Email/Password Salah",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            "Email Telah Digunaan",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),
                            "Networ Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(),
                            "PArse Error",
                            Toast.LENGTH_LONG).show();
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", user);
                params.put("password", pass);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("Accept", "application/json");

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getCurrentFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("currentToken fcm", token);

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("TAG", msg);
                        Toast.makeText(Login.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
