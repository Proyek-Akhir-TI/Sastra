package sastra.panji.dhimas.proggeres.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import sastra.panji.dhimas.proggeres.Model.Kandang;
import sastra.panji.dhimas.proggeres.Model.Peternak;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.ListKandangAdapter;

public class ListKandang extends AppCompatActivity {
    public static final String EXTRA_MOVIES = "peternak";
    private static String URL = "https://be7cb6f49717.ngrok.io";
    Peternak peternak;
    RecyclerView rvKandang;
    final private ArrayList<Kandang> list = new ArrayList<>();
    Button logout, tambah, refresh;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText namaKandang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kandang);

        rvKandang = findViewById(R.id.rv_Kandang);
        rvKandang.setHasFixedSize(true);
        TextView nama = findViewById(R.id.nama_peternak);
        CircleImageView circleImageView = findViewById(R.id.user_photo);
        peternak = getIntent().getParcelableExtra(EXTRA_MOVIES);
        TextView kelompok_id = findViewById(R.id.nama_kelompok);
        TextView email = findViewById(R.id.email_peternak);
        logout = findViewById(R.id.btn_logout);
        tambah = findViewById(R.id.tambah_kandang);
        refresh = findViewById(R.id.btn_refresh);
        if (peternak != null) {
            Glide.with(this)
                    .load(URL + peternak.getPhoto())
                    .apply(new RequestOptions().override(500, 500))
                    .into(circleImageView);
            nama.setText(peternak.getNama());
            email.setText(peternak.getEmail());

            switch (peternak.getKelompok_id()) {
                case 0:
                    kelompok_id.setText("Tunas Harapan");
                    break;
                case 1:
                    kelompok_id.setText("Tunas Muda");
                    break;
                case 2:
                    kelompok_id.setText("Tunas Bangsa");
                    break;


            }
            listCandang(peternak.getToken());
        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(ListKandang.this, Login.class);
                startActivity(logout);
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCandang(peternak.getToken());
            }
        });

    }

    public void listCandang(final String bearer) {
        String auth = "/api/peternak/auth/kandang";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + auth, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kandang");
                    if (array.length() == 0) {
                        Toast.makeText(ListKandang.this, "Tidak ada Kandang ", Toast.LENGTH_LONG).show();

                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject candang = array.getJSONObject(i);
                            Kandang kandang = new Kandang();
                            kandang.setName(candang.getString("name"));
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
        String auth = "/api/peternak/auth/kandang";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + auth, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kandang");
                    if (array.length() == 0) {
                        Toast.makeText(ListKandang.this, "Tidak ada Kandang ", Toast.LENGTH_LONG).show();

                    } else {

                        if (array.length() != list.size()) {
                            JSONObject candang = array.getJSONObject(array.length() - 1);
                            Kandang kandang = new Kandang();
                            kandang.setName(candang.getString("name"));
                            list.add(kandang);


                        } else {
                            Toast.makeText(ListKandang.this, "Tidak ada Kandang baru", Toast.LENGTH_LONG).show();

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

    public void showKandang() {
        rvKandang.setLayoutManager(new LinearLayoutManager(this));
        ListKandangAdapter listKandangAdapter = new ListKandangAdapter(list);

        rvKandang.setAdapter(listKandangAdapter);
    }

    private void DialogForm() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.form_kandang, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getApplicationContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        namaKandang = findViewById(R.id.txt_nama);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                              Toast.makeText(ListKandang.this,namaKandang.getText().toString(),Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

//    private void addKandang(final String bearer, final String name, final String user_id) {
//        String tambah = "/api/peternak/auth/kandang";
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, URL + tambah, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Toast.makeText(ListKandang.this, "Berhasil Menambah", Toast.LENGTH_SHORT).show();
//                namaKandang.setText("");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name", name);
//                params.put("user_id", user_id);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/json");
//                params.put("Authorization", "Bearer " + bearer);
//
//                return params;
//            }
//
//        };
//        queue.add(request);
//    }


}
