package sastra.panji.dhimas.proggeres.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sastra.panji.dhimas.proggeres.R;

public class Daftar extends AppCompatActivity {

    private Spinner spNamen;
    private Button daftar, chooseImg;
    private TextView Loginya;

    private EditText nama, email, alamat, password, telpon;
    private ProgressBar loading;

    //Image
    Uri fileUri;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    Intent intent;


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
                int kelompok = spNamen.getSelectedItemPosition();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (Name.equals("")) {
                    nama.setError("Masukkan Nama");

                } else if (Email.equals("")) {
                    email.setError("Masukkan Email");

                } else if (!Email.matches(emailPattern)) {

                    email.setError("Email Salah");
                } else if (Alam.equals("")) {

                    alamat.setError("Masukkan Alamat");
                } else if (Pass.equals("") || Pass.length() < 6) {
                    password.setError("Masukkan Password Min 6");

                } else if (Telp.equals("") || Telp.length() < 12) {
                    telpon.setError("Masukkan nomor Telpon");

                } else if (decoded == null) {

                    chooseImg.setError("Pilih Gambar Profile");
                } else {

                    daftarPeternak(Name, Email, Alam, Pass, Telp, kelompok);
                }


            }
        });
    }

    private void daftarPeternak(final String namanya, final String emailnya, final String alamatnya, final String passwordnya, final String telponya, final int kelompok_id) {
        loading.setVisibility(View.VISIBLE);
        daftar.setVisibility(View.GONE);
        String daftarnya = "api/peternak/auth/daftar";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, R.string.SERVER + daftarnya, new Response.Listener<String>() {
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
                        Toast.makeText(Daftar.this, pesan, Toast.LENGTH_LONG).show();
                    } else {
                        loading.setVisibility(View.GONE);
                        daftar.setVisibility(View.VISIBLE);

                        Toast.makeText(Daftar.this, pesan, Toast.LENGTH_LONG).show();

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
                            "Authentiasi salah",
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
                params.put("name", namanya);
                params.put("role_id", Integer.toString(4));
                params.put("email", emailnya);
                params.put("password", passwordnya);
                params.put("address", alamatnya);
                params.put("telp", telponya);
                params.put("kelompok_id", Integer.toString(kelompok_id));
                params.put("photo", getStringImage(decoded));
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(Daftar.this.getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void pickImage() {
        //  ivPhoto.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Daftar.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        // imageView.setImageBitmap(decoded);
    }


    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void setEmpty() {
        nama.setText("");
        telpon.setText("");
        alamat.setText("");
        password.setText("");
        email.setText("");

    }
}
