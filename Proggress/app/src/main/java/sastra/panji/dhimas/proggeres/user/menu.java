package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import sastra.panji.dhimas.proggeres.sarang.BeratSarang;
import sastra.panji.dhimas.proggeres.Model.Kandang;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;
import sastra.panji.dhimas.proggeres.menu.AturNotif;
import sastra.panji.dhimas.proggeres.Kelembapan.Kelembapan;
import sastra.panji.dhimas.proggeres.menu.MapsActivity;
import sastra.panji.dhimas.proggeres.Suhu.Suhu;

public class menu extends AppCompatActivity {

    Kandang candang;
    TextView nama;
    CardView suhu, maps, hapus, kelembapan, setting, berat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nama = findViewById(R.id.plh_kandang);

        suhu = findViewById(R.id.menu_suhu);
        maps = findViewById(R.id.menu_maps);
        hapus = findViewById(R.id.menu_delete);
        setting = findViewById(R.id.menu_setting);
        kelembapan = findViewById(R.id.menu_kelembapan);
        berat = findViewById(R.id.menu_berat);
        nama.setText(Preferences.getNamaKandang(getBaseContext()));

        berat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(menu.this, BeratSarang.class);
                startActivity(setting);

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(menu.this, AturNotif.class);
                startActivity(setting);

            }
        });
        suhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent suhu = new Intent(menu.this, Suhu.class);
                startActivity(suhu);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps = new Intent(menu.this, MapsActivity.class);

                startActivity(maps);
            }
        });
        kelembapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kelembapan = new Intent(menu.this, Kelembapan.class);

                startActivity(kelembapan);
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hapus(Preferences.getIdKandang(getBaseContext()), Preferences.getBearerUser(getBaseContext()));
            }
        });
    }

    private void hapus(final String id, final String bearer) {
        String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/hapuskandang";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(menu.this, "Hapus Berhasil", Toast.LENGTH_LONG).show();
                Preferences.clearKandang(getBaseContext());
                Intent i = new Intent(menu.this, ListKandang.class);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
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
        queue.add(stringRequest);
    }


}
