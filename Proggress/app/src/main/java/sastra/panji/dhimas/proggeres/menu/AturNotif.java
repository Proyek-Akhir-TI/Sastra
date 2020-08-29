package sastra.panji.dhimas.proggeres.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class AturNotif extends AppCompatActivity {
    private String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/kandang/setberat";
    private ProgressBar loading;
    private Button simpan;
    private EditText berat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_notif);
        loading = findViewById(R.id.loading_setting);
        simpan = findViewById(R.id.btn_simpan_berat);
        berat = findViewById(R.id.berat_sarang);
        loading.setVisibility(View.INVISIBLE);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int beratnya = Integer.valueOf(berat.getText().toString().trim()) * 1000;
                if (berat.getText().toString().equals("")) {
                    berat.setError("Berat tidak boleh Kosong");
                } else if (Integer.valueOf(berat.getText().toString()) < 5) {
                    berat.setError("Berat Maksimal 5Kg");
                } else if (Integer.valueOf(berat.getText().toString()) < 1) {
                    berat.setError("Masukkan Angka Yang Valid");
                } else {
                    loading.setVisibility(View.VISIBLE);
                    setBerat(Preferences.getIdKandang(getBaseContext()), String.valueOf(beratnya),Preferences.getBearerUser(getBaseContext()));
                }
            }
        });
    }


    private void setBerat(final String id, final String berat, final String bearer) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("berat_sarang", berat);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Authorization", "Bearer " + bearer);
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(request);
    }
}
