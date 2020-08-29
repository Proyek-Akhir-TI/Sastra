package sastra.panji.dhimas.proggeres;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import sastra.panji.dhimas.proggeres.helper.PermissionHelper;
import sastra.panji.dhimas.proggeres.helper.Preferences;
import sastra.panji.dhimas.proggeres.user.ListKandang;
import sastra.panji.dhimas.proggeres.user.Login;

public class SplasScreen extends AppCompatActivity {

    PermissionHelper permissionHelper;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_acces);

        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                permissionHelper = new PermissionHelper(SplasScreen.this);
                checkAndRequestPermissions();
            }
        }, 3000);
//

    }

    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {

                if (Preferences.getLoggedInStatus(getBaseContext())) {
                    getCurrentFirebaseToken();
                    intent = new Intent(SplasScreen.this, ListKandang.class);
                    startActivity(intent);
                    finish();
                } else {
                    setCurrentFirebaseToken();
                    intent = new Intent(SplasScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

    private void getCurrentFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Preferences.clearFirebase(getBaseContext());
                        Preferences.setFirebase(getBaseContext(),token);
                        updateToken(token, Preferences.getBearerUser(getBaseContext()));
                        Log.e("currentToken fcm", token);


                    }
                });
    }

    private void setCurrentFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("current",token);
                        Preferences.clearFirebase(getBaseContext());
                        Preferences.setFirebase(getBaseContext(),token);


                    }
                });
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
                Log.d("params",""+params);
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
