package sastra.panji.dhimas.proggeres.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class tambahKandang extends AppCompatActivity {

    Uri fileUri;
    Bitmap bitmap, decoded;

    int bitmap_size = 60; // range 1 - 100
    EditText namaKandang, urlkandang, telponKandang;
    Button simpan;
    TextView result_id, result_status, id, status;
    ProgressBar loading;
    CircleImageView circleImageView;
    boolean isfoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kandang);
        circleImageView = findViewById(R.id.add_candang);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        telponKandang = findViewById(R.id.telpon_tambah_kandang);
        urlkandang = findViewById(R.id.url_tambah_kandang);
        namaKandang = findViewById(R.id.nama_tambah_kandang);
        simpan = findViewById(R.id.btn_tambah_kandang);
        result_id = findViewById(R.id.result_id_tambah_kandang);
        result_status = findViewById(R.id.result_status_tambah_kandang);
        id = findViewById(R.id.id_tambah_kandang);
        status = findViewById(R.id.status_tambah_kandang);
        loading = findViewById(R.id.loading_tambah_kandang);


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Url = urlkandang.getText().toString().trim();
                String Nama = namaKandang.getText().toString().trim();
                String telpon = telponKandang.getText().toString().trim();

                if (isInputValid()) {
                    if (isValidUrl(Url)) {
                        loading.setVisibility(View.VISIBLE);
                        tambahKandang(Nama, Preferences.getKELOMPOK(getBaseContext()), Url, Preferences.getBearerUser(getBaseContext()), Preferences.getIdUser(getBaseContext()), telpon);

                    }
                }

            }
        });

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
                        circleImageView.setImageBitmap(decoded);

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
                .start(tambahKandang.this);

    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void tambahKandang(final String nama, final String kelompok_id, final String urlnya, final String bearer, final String user_id, final String telpon) {
        final String url = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/tambahkandang";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("kandang");

                    id.setText("Kandang Id = ");
                    status.setText("Status = ");
                    result_id.setText(array.getJSONObject(0).getString("id"));
                    result_status.setText(object.getString("status"));

                    Toasty.info(tambahKandang.this, object.getString("status"), Toasty.LENGTH_SHORT, true).show();
                    loading.setVisibility(View.INVISIBLE);
                    decoded = null;
                    setEmpty();
                } catch (JSONException e) {
                    loading.setVisibility(View.INVISIBLE);
                    Toasty.info(tambahKandang.this, e.toString(), Toasty.LENGTH_SHORT, true).show();
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
                params.put("url", urlnya);
                params.put("foto", getStringImage(decoded));
                params.put("no_telpon", telpon);
                params.put("user_id", user_id);
                params.put("kelompok_id", kelompok_id);

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private Boolean isInputValid() {

        if (isEmpty(namaKandang)) {
            namaKandang.setError("Masukkan Nama Kandang");
            return false;

        }
        if (isEmpty(urlkandang)) {
            urlkandang.setError("Masukkan URL");
            return false;
        }
        if (decoded == null) {
            Toasty.warning(tambahKandang.this, "Harap isi foto", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }

    private void setEmpty() {
        telponKandang.setText("");
        urlkandang.setText("");
        namaKandang.setText("");
        circleImageView.setImageResource(R.mipmap.ic_sarang);
    }

    private boolean isValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(String.valueOf(url)) && Patterns.WEB_URL.matcher(String.valueOf(url)).matches();
        } catch (MalformedURLException e) {

        }

        return false;
    }
}
