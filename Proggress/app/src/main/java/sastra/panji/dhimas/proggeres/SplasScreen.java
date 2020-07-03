package sastra.panji.dhimas.proggeres;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sastra.panji.dhimas.proggeres.helper.PermissionHelper;
import sastra.panji.dhimas.proggeres.menu.testUpload;
import sastra.panji.dhimas.proggeres.user.Login;

public class SplasScreen extends AppCompatActivity {

    PermissionHelper permissionHelper;
    Intent intent;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_acces);

        permissionHelper = new PermissionHelper(this);
        checkAndRequestPermissions();
    }

    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {
                intent = new Intent(SplasScreen.this, Login.class);
                startActivity(intent);
                finish();
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
}
