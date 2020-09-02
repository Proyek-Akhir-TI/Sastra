package sastra.panji.dhimas.proggeres.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;

public class LupaPassword extends AppCompatActivity {

    EditText nama, email, pass1, pass2, telp;
    Button save;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        nama = findViewById(R.id.lp_nama);
        email = findViewById(R.id.lp_email);
        pass1 = findViewById(R.id.lp_pass1);
        pass2 = findViewById(R.id.lp_pass2);
        save = findViewById(R.id.btn_lupa);
        telp = findViewById(R.id.lp_telp);
        loading = findViewById(R.id.loading_lupa);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                if (isInputValid()) {
                    LupaPassword(nama.getText().toString(), email.getText().toString(), telp.getText().toString(), pass1.getText().toString());
                }
            }
        });

    }

    public void LupaPassword(final String nama, final String email, final String telpon, final String pass) {
        save.setEnabled(false);
        String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/reset";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean status = object.getBoolean("status");
                    String pesan = object.getString("message");
                    if (status) {
                        loading.setVisibility(View.INVISIBLE);
                        setEmpty();
                        save.setEnabled(false);
                        Toasty.success(LupaPassword.this, pesan, Toast.LENGTH_SHORT, true).show();
                    } else {
                        setEmpty();
                        Toasty.error(LupaPassword.this, pesan, Toast.LENGTH_SHORT, true).show();
                        loading.setVisibility(View.INVISIBLE);
                        save.setEnabled(false);

                    }


                } catch (JSONException e) {
                    loading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(LupaPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("telpon", telpon);
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
        queue.add(request);
    }

    public void setEmpty() {
        nama.setText("");
        email.setText("");
        pass1.setText("");
        telp.setText("");
        save.setEnabled(true);
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private Boolean isInputValid() {
        if (isEmpty(nama)) {
            nama.setError("Massukkan Nama");
            return false;
        }
        if (isEmpty(pass1)) {
            pass1.setError("Masukkan Password");
            return false;
        } else if (pass1.length() < 6) {
            pass1.setError("Pasword minimal 6");
            return false;
        }
        if (pass2.equals(pass1.getText().toString().trim())) {
            pass2.setError("Password Tidak Sama");

        }
        if (isEmpty(telp)) {
            telp.setError("Masukkan Telpon");

        }

        return true;
    }

}
