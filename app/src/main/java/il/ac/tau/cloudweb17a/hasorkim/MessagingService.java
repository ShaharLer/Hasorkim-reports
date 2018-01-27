package il.ac.tau.cloudweb17a.hasorkim;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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


                Notification n = new Notification.Builder(MessagingService.this)
                        .setContentTitle(title)
                        .setContentText("סטטוס עדכני: "+ newStatus)
                        .setSmallIcon(R.drawable.dog_icon)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (notificationManager != null) {
                    notificationManager.notify(0, n);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
