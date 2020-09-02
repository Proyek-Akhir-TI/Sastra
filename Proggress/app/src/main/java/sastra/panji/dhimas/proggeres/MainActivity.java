package sastra.panji.dhimas.proggeres;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView textView, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            NotificationChannel channel = new NotificationChannel("NotifApps", "NotifyApps", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }


        Bundle bundle = getIntent().getExtras();

        Log.d("bundle", "" + bundle);
        Log.d("bundle title", "" + Objects.requireNonNull(bundle).getString("title"));
        Log.d("bundle message", "" + bundle.getString("message"));
        textView = findViewById(R.id.as);
        title = findViewById(R.id.aw);

//        title.setText(bundle.getString("title"));
//        textView.setText(bundle.getString("message"));
    }
}
