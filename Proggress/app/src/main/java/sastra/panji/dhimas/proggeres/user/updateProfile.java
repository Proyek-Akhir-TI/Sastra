package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class updateProfile extends AppCompatActivity {

    private EditText update_email, update_alamat, update_telpon, update_nama, update_password;
    private CircleImageView update_foto;
    private TextView kandang, kelompok;
    private Bitmap decoded;
    private Uri fileUri;
    private ProgressBar loading;
    private int bitmap_size = 60;
    private Button simpan;
    private String update_url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/update";
    private String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        simpan = findViewById(R.id.btn_update_simpan);
        update_alamat = findViewById(R.id.update_alamat);
        update_email = findViewById(R.id.update_email);
        update_telpon = findViewById(R.id.update_telpon);
        update_foto = findViewById(R.id.update_foto);
        update_nama = findViewById(R.id.update_nama);
        kandang = findViewById(R.id.jumlah_kandang);
        kelompok = findViewById(R.id.update_kelompok);
        update_password = findViewById(R.id.update_password);
        loading = findViewById(R.id.loading_update);
        loading.setVisibility(View.VISIBLE);

        update_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailnya = update_email.getText().toString().trim();
                String namanya = update_nama.getText().toString().trim();
                String passwordnya = update_password.getText().toString().trim();
                String alamat = update_alamat.getText().toString().trim();
                String telpon = update_telpon.getText().toString().trim();

                if (isInputValid()) {
                    updateProfile(Preferences.getBearerUser(getBaseContext()), namanya, emailnya, passwordnya, alamat, telpon, decoded);
                }
            }
        });

        getProfile(Preferences.getBearerUser(getBaseContext()));
    }

    private void getProfile(final String bearer) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.setVisibility(View.INVISIBLE);
                    Log.d("respon", response);
                    JSONObject object = new JSONObject(response);
                    String kandangnya = object.getString("kandang");
                    String kelompoknya = object.getJSONObject("kelompok").getString("nama");
                    kandang.setText(kandangnya);
                    kelompok.setText(kelompoknya);
                    update_nama.setText(object.getJSONObject("profil").getString("nama"));
                    update_email.setText(object.getJSONObject("profil").getString("email"));
                    update_alamat.setText(object.getJSONObject("profil").getString("alamat"));
                    update_telpon.setText(object.getJSONObject("profil").getString("telpon"));

                    Glide.with(updateProfile.this)
                            .load("http://ta.poliwangi.ac.id/~ti17183/laravel/public/storage/uploads/" + object.getJSONObject("profil").getString("photo"))
                            .apply(new RequestOptions().override(500, 500))
                            .into(update_foto);
                    decoded = ((BitmapDrawable) update_foto.getDrawable()).getBitmap();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
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

    private void updateProfile(final String bearer, final String nama, final String email, final String password, final String alamat, final String telpon, final Bitmap photo) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", response);
                    JSONObject object = new JSONObject(response);
                    String pesan = object.getString("message");
                    boolean status = object.getBoolean("status");
                    String images = object.getString("images");
                    String nama = object.getString("nama");
                    String email = object.getString("email");
                    if (status) {
                        Toasty.success(updateProfile.this, pesan, Toasty.LENGTH_SHORT, true).show();
                        Preferences.setUrlImg(getBaseContext(), images);
                        Preferences.setNama(getBaseContext(), nama);
                        Preferences.setEmail(getBaseContext(), email);
                        Intent i = new Intent(updateProfile.this, ListKandang.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toasty.error(updateProfile.this, pesan, Toasty.LENGTH_SHORT, true).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("alamat", alamat);
                params.put("telpon", telpon);
                params.put("photo", getStringImage(photo));
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
                        update_foto.setImageBitmap(decoded);


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
                .start(updateProfile.this);

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private Boolean isInputValid() {

        if (isEmpty(update_nama)) {
            update_nama.setError("Masukkan Nama Anda");
            return false;

        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String str = update_email.getText().toString().trim();
        if (isEmpty(update_email)) {
            update_email.setError("Masukkan email");
            return false;

        } else if (!str.matches(emailPattern)) {
            update_email.setError("Email Tidak Valid");
            return false;

        }
        if (isEmpty(update_alamat)) {
            update_email.setError("Masukkan Alamat");
            return false;

        }
        if (isEmpty(update_password) || update_password.length() < 6) {
            update_password.setError("Masukkan Password");
            return false;
        }
        if (isEmpty(update_telpon) && update_telpon.length() <= 11) {
            update_telpon.setError("Masukkan Nomor Telpon");
            return false;

        }

        if (decoded == null) {
            Toasty.warning(updateProfile.this, "Harap masukkan foto", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }


}
