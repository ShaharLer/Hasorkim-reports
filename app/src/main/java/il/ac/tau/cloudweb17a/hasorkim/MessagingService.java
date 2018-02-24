package il.ac.tau.cloudweb17a.hasorkim;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static il.ac.tau.cloudweb17a.hasorkim.Report.translateStatus;


public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = MessagingService.class.getSimpleName();

    String reportId;
    String title;
    String newStatus;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            reportId = remoteMessage.getData().get("report");
            title = remoteMessage.getData().get("title");
            newStatus = translateStatus(remoteMessage.getData().get("newStatus"));

            Log.d(TAG, "Message Data reportId: " + reportId);
            Log.d(TAG, "Message Data title: " + title);
            Log.d(TAG, "Message Data newStatus: " + newStatus);
        } else {
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("body"));
        }


        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports").child(reportId);

        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Report report = dataSnapshot.getValue(Report.class);
                report.setId(reportId);

                Intent resultIntent = new Intent(getApplicationContext(), ActiveReportActivity.class);

                resultIntent.putExtra("Report", report);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getApplicationContext(),
                                (int) System.currentTimeMillis(),
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String CHANNEL_ID = "23123";


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create the NotificationChannel
                    CharSequence name = getString(R.string.channel_name);
                    String description = getString(R.string.channel_description);
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this

                    notificationManager.createNotificationChannel(mChannel);
                }


                Notification.Builder builder = new Notification.Builder(MessagingService.this)
                        .setContentTitle(title)
                        .setContentText("סטטוס עדכני: " + newStatus)
                        .setSmallIcon(R.drawable.dog_icon)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setChannelId(CHANNEL_ID);
                }


                if (notificationManager != null) {
                    notificationManager.notify(0, builder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
