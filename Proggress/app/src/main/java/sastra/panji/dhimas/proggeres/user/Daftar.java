package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.Model.Kelompok;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class Daftar extends AppCompatActivity {

    //Image
    Uri fileUri;
    Bitmap decoded;
    int bitmap_size = 60; // range 1 - 100
    private Spinner spNamen;
    private Button daftar, chooseImg;
    private TextView Loginya;
    private EditText nama, email, alamat, password, telpon;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);


        spNamen = findViewById(R.id.spinner);
        daftar = findViewById(R.id.btn_daftar);
        nama = findViewById(R.id.fm_nama);
        email = findViewById(R.id.fm_email);
        alamat = findViewById(R.id.fm_alamat);
        password = findViewById(R.id.fm_passwod);
        telpon = findViewById(R.id.fm_telpon);
        loading = findViewById(R.id.fm_loading);
        chooseImg = findViewById(R.id.btn_choose_image);
        Loginya = findViewById(R.id.login);
        loading.setVisibility(View.VISIBLE);

        daftar.setEnabled(false);
        getKelompok();
        Loginya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Daftar.this, Login.class);
                startActivity(login);
                finish();
            }
        });

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });


        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = nama.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                String Telp = telpon.getText().toString().trim();
                String Alam = alamat.getText().toString().trim();
                String Email = email.getText().toString().trim();
                Kelompok kelompok = (Kelompok) spNamen.getSelectedItem();
                int Id = Integer.valueOf(kelompok.getKelompok_id());


                if (isInputValid()) {
                    daftarPeternak(Name, Email, Alam, Pass, Telp, Id);
                }

            }
        });
    }

    private void daftarPeternak(final String namanya, final String emailnya, final String alamatnya, final String passwordnya, final String telponya, final int kelompok_id) {
        loading.setVisibility(View.VISIBLE);
        daftar.setVisibility(View.GONE);
        String daftarnya = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/register";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, daftarnya, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean error = object.getBoolean("error");
                    String pesan = object.getString("message");
                    if (!error) {
                        loading.setVisibility(View.GONE);
                        daftar.setVisibility(View.VISIBLE);
                        setEmpty();
                        Toasty.success(Daftar.this, pesan, Toasty.LENGTH_LONG, true).show();
                    } else {
                        loading.setVisibility(View.GONE);
                        daftar.setVisibility(View.VISIBLE);
                        setEmpty();
                        Toasty.error(Daftar.this, pesan, Toasty.LENGTH_LONG, true).show();

                    }

                } catch (JSONException e) {
                    loading.setVisibility(View.GONE);
                    daftar.setVisibility(View.VISIBLE);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.setVisibility(View.GONE);

                daftar.setVisibility(View.VISIBLE);


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            "Time Out!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(),
                            "Authentikasi salah",
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
                error.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", namanya);
                params.put("role_id", Integer.toString(4));
                params.put("email", emailnya);
                params.put("password", passwordnya);
                params.put("alamat", alamatnya);
                params.put("telpon", telponya);
                params.put("firebase", Preferences.getFirebase(getBaseContext()));
                params.put("kelompok_id", Integer.toString(kelompok_id));
                params.put("photo", getStringImage(decoded));
                params.put("status", String.valueOf(0));
                Log.e("Params", "" + params);
                return params;

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                Log.e("Header ", "" + params);
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();
                if (fileUri != null) {

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(fileUri);
                        decoded = BitmapFactory.decodeStream(inputStream);
                        chooseImg.setText(fileUri.getLastPathSegment());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void pickImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4, 4)
                .start(Daftar.this);

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void setEmpty() {
        nama.setText("");
        telpon.setText("");
        alamat.setText("");
        password.setText("");
        email.setText("");
        decoded.recycle();
        chooseImg.setText("Pilih Gambar");
        decoded = null;

    }

    private void getKelompok() {
        String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/kelompok";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<Kelompok> kelompoks = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kel");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        String id = data.getString("id");
                        String nama = data.getString("nama");
                        kelompoks.add(new Kelompok(id, nama));
                    }

                    ArrayAdapter<Kelompok> arrayAdapter = new ArrayAdapter<Kelompok>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, kelompoks);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Log.d("Array", "" + arrayAdapter);
                    spNamen.setAdapter(arrayAdapter);
                    loading.setVisibility(View.INVISIBLE);
                    daftar.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                Log.e("Header ", "" + params);
                return params;

            }
        };
        queue.add(request);
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private Boolean isInputValid() {

        if (isEmpty(nama)) {
            nama.setError("Masukkan Nama Anda");
            return false;

        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String str = email.getText().toString().trim();
        if (isEmpty(email)) {
            email.setError("Masukkan email");
            return false;

        } else if (!str.matches(emailPattern)) {
            email.setError("Email Tidak Valid");
            return false;

        }
        if (isEmpty(alamat)) {
            alamat.setError("Masukkan Alamat");
            return false;

        }
        if (isEmpty(password) || password.length() < 6) {
            password.setError("Masukkan Password");
            return false;
        }
        if (isEmpty(telpon) && telpon.length() <= 11) {
            telpon.setError("Masukkan Nomor Telpon");
            return false;

        }

        if (decoded == null) {
            Toasty.warning(Daftar.this, "Harap masukkan foto", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }

}
