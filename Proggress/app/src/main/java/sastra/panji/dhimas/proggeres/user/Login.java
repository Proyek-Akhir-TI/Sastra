package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    Button login;
    ProgressBar loading;
    TextView daftar, lupa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        daftar = findViewById(R.id.daftar);
        username = findViewById(R.id.lg_username);
        password = findViewById(R.id.lg_password2);
        login = findViewById(R.id.btn_login);
        loading = findViewById(R.id.loading);
        lupa = findViewById(R.id.lupa);

        daftar.setOnClickListener(this);
        login.setOnClickListener(this);
        lupa.setOnClickListener(this);

    }

    private void Login(final String user, final String pass, final String token) {
        loading.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);

        String loginnya = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginnya, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response.toString());
                    JSONObject object = new JSONObject(response);
                    boolean succes = object.getBoolean("status");
                    String pesan = object.getString("message");
                    if (succes) {

                        String id = object.getString("id");
                        String nm = object.getString("nama");
                        String em = object.getString("email");
                        String tk = object.getString("token");
                        String img = object.getString("images");
                        String kl_id = object.getString("kelompok_id");
                        loading.setVisibility(View.GONE);

                        Preferences.setLoggedInStatus(getBaseContext(), true, id);
                        Preferences.setBearerUser(getBaseContext(), tk);
                        Preferences.setEmail(getBaseContext(), em);
                        Preferences.setKelompok(getBaseContext(), kl_id);
                        Preferences.setUrlImg(getBaseContext(), img);
                        Preferences.setNama(getBaseContext(), nm);
                        Toasty.success(Login.this, "Selamat Datang " + nm, Toasty.LENGTH_LONG, true).show();
                        Intent intent = new Intent(Login.this, ListKandang.class);
                        startActivity(intent);
                        finish();
                    } else {
                        loading.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        Toasty.error(Login.this, pesan, Toast.LENGTH_SHORT, true).show();
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
                    Toasty.error(Login.this, "Waktu Habis!", Toast.LENGTH_SHORT, true).show();
                } else if (error instanceof AuthFailureError) {
                    Toasty.error(Login.this, "Email/Password Salah", Toast.LENGTH_SHORT, true).show();
                } else if (error instanceof ServerError) {
                    Toasty.error(Login.this, "Kesalahan Server!", Toast.LENGTH_SHORT, true).show();
                } else if (error instanceof NetworkError) {
                    Toasty.error(Login.this, "Jaringan Bermasalah!", Toast.LENGTH_SHORT, true).show();
                } else if (error instanceof ParseError) {
                    Toasty.error(Login.this, "Parse Error!", Toast.LENGTH_SHORT, true).show();
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", user);
                params.put("password", pass);
                params.put("api_firebase", token);
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

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (isInputValid()) {
                    String token = Preferences.getFirebase(getBaseContext());
                    Login(Username, Password, token);

                }


                break;
            case R.id.daftar:
                Intent intent2 = new Intent(Login.this, Daftar.class);
                startActivity(intent2);
                break;
            case R.id.lupa:
                Intent intent3 = new Intent(Login.this, LupaPassword.class);
                startActivity(intent3);
                break;
        }

    }

    private Boolean isInputValid() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String str = username.getText().toString().trim();
        if (isEmpty(username)) {
            username.setError("Masukkan email");
            return false;

        } else if (!str.matches(emailPattern)) {
            username.setError("Email Tidak Valid");
            return false;

        }

        if (isEmpty(password)) {
            password.setError("Masukkan Password");
            return false;
        } else if (password.length() < 6) {
            password.setError("Password minimal 6 karakter");
            return false;
        }


        return true;
    }
}
