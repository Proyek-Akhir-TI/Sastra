package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.Model.Kandang;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.ListKandangAdapter;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class ListKandang extends AppCompatActivity {

    final private ArrayList<Kandang> list = new ArrayList<>();
    RecyclerView rvKandang;
    Button logout;
    ImageView tambah, refresh;
    ProgressBar progressBar;
    CircleImageView circleImageView;
    TextView nama, email;
    Kandang kandang;
    private String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/listkandang";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kandang);

        rvKandang = findViewById(R.id.rv_Kandang);
        rvKandang.setHasFixedSize(true);
        nama = findViewById(R.id.nama_peternak);
        circleImageView = findViewById(R.id.user_photo);

        email = findViewById(R.id.email_peternak);
        logout = findViewById(R.id.btn_logout);
        tambah = findViewById(R.id.tambah_kandang);
        progressBar = findViewById(R.id.loading_list);
        refresh = findViewById(R.id.refresh_kandang);

        Glide.with(this)
                .load("http://ta.poliwangi.ac.id/~ti17183/laravel/public" + Preferences.getUrlImg(getBaseContext()))
                .apply(new RequestOptions().override(500, 500))
                .into(circleImageView);
        nama.setText(Preferences.getNAMA(getBaseContext()));
        email.setText(Preferences.getEMAIL(getBaseContext()));

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tambah = new Intent(ListKandang.this, tambahKandang.class);
                startActivity(tambah);
                onPause();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateToken("0", Preferences.getBearerUser(getBaseContext()));
                Preferences.clearLoggedInUser(getBaseContext());
                Intent logout = new Intent(ListKandang.this, Login.class);
                startActivity(logout);
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                refreshCandang(Preferences.getBearerUser(getBaseContext()));
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListKandang.this, updateProfile.class);
                startActivity(i);
            }
        });
//        listCandang(Preferences.getBearerUser(getBaseContext()));
    }

    @Override
    protected void onResume() {
        listCandang(Preferences.getBearerUser(getBaseContext()));

        super.onResume();
        Glide.with(this)
                .load("http://ta.poliwangi.ac.id/~ti17183/laravel/public" + Preferences.getUrlImg(getBaseContext()))
                .apply(new RequestOptions().override(500, 500))
                .into(circleImageView);
        nama.setText(Preferences.getNAMA(getBaseContext()));
        email.setText(Preferences.getEMAIL(getBaseContext()));
    }

    @Override
    protected void onPause() {
        list.clear();
        super.onPause();
    }

    public void listCandang(final String bearer) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kandang");

                    if (array.length() == 0) {
                        Toasty.info(ListKandang.this, "Tidak ada Kandang ", Toast.LENGTH_SHORT, true).show();

                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject candang = array.getJSONObject(i);
                            kandang = new Kandang();
                            kandang.setName(candang.getString("nama"));
                            kandang.setFoto(candang.getString("foto"));
                            kandang.setId(candang.getString("id"));
                            kandang.setUrl(candang.getString("url"));
                            kandang.setLatitude(candang.getString("latitude"));
                            kandang.setLongitude(candang.getString("longitude"));
                            kandang.setTelpon(candang.getString("no_telpon"));

                            list.add(kandang);

                        }
                        showKandang();

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListKandang.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + bearer);

                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void refreshCandang(final String bearer) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kandang");
                    if (array.length() == 0) {
                        Toast.makeText(ListKandang.this, "Tidak ada Kandang ", Toast.LENGTH_LONG).show();

                    } else {

                        if (array.length() != list.size()) {
                            JSONObject candang = array.getJSONObject(array.length() - 1);
                            Kandang kandang = new Kandang();
                            kandang.setName(candang.getString("nama"));
                            kandang.setFoto(candang.getString("foto"));
                            kandang.setId(candang.getString("id"));
                            kandang.setUrl(candang.getString("url"));
                            kandang.setLatitude(candang.getString("latitude"));
                            kandang.setLongitude(candang.getString("longitude"));
                            kandang.setTelpon(candang.getString("no_telpon"));

                            list.add(kandang);


                        } else {
                            Toasty.info(ListKandang.this, "Tidak ada Kandang baru", Toasty.LENGTH_SHORT).show();

                        }

                        showKandang();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListKandang.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + bearer);

                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void showKandang() {
        rvKandang.setLayoutManager(new LinearLayoutManager(this));
        ListKandangAdapter listKandangAdapter = new ListKandangAdapter(list);
        rvKandang.setAdapter(listKandangAdapter);

        listKandangAdapter.setOnItemClickCallback(new ListKandangAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Kandang data) {
                showSelectedHero(data);
            }
        });
    }


    private void showSelectedHero(Kandang kandang) {

        Preferences.clearKandang(getBaseContext());
        Preferences.setUrlActive(getBaseContext(), kandang.getUrl());
        Preferences.setNamaKandang(getBaseContext(), kandang.getName());
        Preferences.setStatusKandang(getBaseContext(), String.valueOf(kandang.getIsActive()));
        Preferences.setIdKandang(getBaseContext(), kandang.getId());
        Preferences.setLatKandang(getBaseContext(), kandang.getLatitude());
        Preferences.setLongKandang(getBaseContext(), kandang.getLongitude());
        Preferences.setNoTelpon(getBaseContext(), kandang.getTelpon());
        Log.d("Telpon", ""+kandang.getTelpon());
        Intent i = new Intent(ListKandang.this, menu.class);
        startActivity(i);


    }

    private void updateToken(final String token, final String bearer) {
        String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/firebase";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fcm coy", response);
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
                params.put("firebase", token);
                Log.d("params", "" + params);
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + bearer);

                return params;
            }

        };
        queue.add(request);
    }


}
