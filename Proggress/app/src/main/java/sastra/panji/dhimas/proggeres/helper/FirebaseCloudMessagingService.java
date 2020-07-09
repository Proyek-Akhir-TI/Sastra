package sastra.panji.dhimas.proggeres.helper;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import sastra.panji.dhimas.proggeres.menu.testUpload;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    public String TAG = "FIREBASE MESSAGING";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            startActivity(new Intent(this, testUpload.class));

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("TAG", "Refreshed token: " + token);
    }

//    void showNotif(){
//        // passing data title dan message ke MainActivity
//        Bundle bundle = new Bundle();
//        bundle.putString("title", title);
//        bundle.putString("message", message);
//
//// setup intent
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "NotifApps")
//                .setContentTitle()
//                .setContentText(message)
////                .setSmallIcon(R.drawable.ic_assessment) // icon
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

//    private void getCurrentFirebaseToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("TAG", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        Log.e("currentToken", token);
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("TAG", msg);
//                        Toast.makeText(FirebaseCloudMessagingService.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
