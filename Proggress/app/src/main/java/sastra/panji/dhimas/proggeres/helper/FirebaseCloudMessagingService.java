package sastra.panji.dhimas.proggeres.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import sastra.panji.dhimas.proggeres.MainActivity;
import sastra.panji.dhimas.proggeres.R;

//public class FirebaseCloudMessagingService extends FirebaseMessagingService {
//
//    public String TAG = "FIREBASE MESSAGING";
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        Map<String, String> data = remoteMessage.getData();
//        String dataPayload = data.get("data");
//
//
//        /*
//         * Cek jika notif berisi data payload
//         * pengiriman data payload dapat dieksekusi secara background atau foreground
//         */
//
//        if (remoteMessage.getData().size() > 0) {
//            Log.e("TAG", "Message data payload: " + remoteMessage.getData());
//
//            try {
//                JSONObject jsonParse = new JSONObject(dataPayload);
//                showNotif(jsonParse.getString("title"), jsonParse.getString("message"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        /*
//         * Cek jika notif berisi data notification payload
//         * hanya dieksekusi ketika aplikasi bejalan secara foreground
//         * dan dapat push notif melalui UI Firebase console
//         */
//        if (remoteMessage.getNotification() != null) {
//            Log.e("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            showNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        }
//
//    }
//
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        Log.d("TAG", "Refreshed token: " + token);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void showNotif(String title, String message){
//
//        Bundle bundle = new Bundle();
//        bundle.putString("title", title);
//        bundle.putString("message", message);
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "NotifApps")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.splash) // icon
//                .setAutoCancel(true) // menghapus notif ketika user melakukan tap pada notif
//                .setLights(200,200,200) // light button
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // set sound
//                .setOnlyAlertOnce(true) // set alert sound notif
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent); // action notif ketika di tap
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(1, notifBuilder.build());
//    }
//
////    private void getCurrentFirebaseToken(){
////        FirebaseInstanceId.getInstance().getInstanceId()
////                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
////                        if (!task.isSuccessful()) {
////                            Log.w("TAG", "getInstanceId failed", task.getException());
////                            return;
////                        }
////
////                        // Get new Instance ID token
////                        String token = task.getResult().getToken();
////                        Log.e("currentToken", token);
////
////                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
////                        Log.d("TAG", msg);
////                        Toast.makeText(FirebaseCloudMessagingService.this, msg, Toast.LENGTH_SHORT).show();
////                    }
////                });
////    }
//}

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String dataPayload = data.get("data");

        //tampil ketika aplikasi backgroud/ tidak di buka
        if (remoteMessage.getData().size() > 0) {
            Log.e("TAG", "Message data payload: " + remoteMessage.getData());

            try {
                JSONObject jsonParse = new JSONObject(dataPayload);
                showNotif(jsonParse.getString("title"), jsonParse.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //tampil ketika foreground / aplikasi berjalan
        if (remoteMessage.getNotification() != null) {
            Log.e("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            tampilNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showNotif(String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "NotifApps")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.splash) // icon
                .setAutoCancel(true) // menghapus notif ketika user melakukan tap pada notif
                .setLights(200, 200, 200) // light button
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // set sound
                .setOnlyAlertOnce(true) // set alert sound notif
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent); // action notif ketika di tap
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notifBuilder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void tampilNotif(String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.splash)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true) // menghapus notif ketika user melakukan tap pada notif
                .setLights(200, 200, 200) // light button
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // set sound
                .setOnlyAlertOnce(true) // set alert sound notif
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }
}