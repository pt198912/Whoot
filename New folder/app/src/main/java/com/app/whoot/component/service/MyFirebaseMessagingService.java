package com.app.whoot.component.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.whoot.R;
import com.app.whoot.bean.FcmTokenBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.app.whoot.util.Constants.EVENT_TYPE_FCM;
import static com.app.whoot.util.Constants.EVENT_TYPE_MESSAGING;
import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private int fcm=0;
    private List<String> mName=new ArrayList<>();
    private List<String> mCode=new ArrayList<>();
    private String gold;
    private String silver;
    private String diamond;
    private String userName;
    private String inviteCount;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + "1111111111111111111");
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String string = remoteMessage.getData().toString();
           // string="{inviteCount=8, silver=1, userName=rffffgg, code=1001, gold=2, title=jjjj, content=Congratulations! You've successfully referdrffffgg! Here's a Whoot! Token for inviting8people View>>, request=1}";
            String replace = string.replace("{", "").replace("}", "");
            String replace1 = replace.replace(",", "\",\"").replace("=", "\":\"");
            String s = "{\"" + replace1 + "\"}";
            String Fcmreplace = s.replace(" ", "");
            Log.d(TAG, "onMessageReceived: ");
            try {
                JSONObject jsonObject = new JSONObject(Fcmreplace);
                String code = jsonObject.optString("code");
                if (code.equals("1001")){
                    userName = jsonObject.optString("userName");

                    String serverTime = jsonObject.optString("serverTm");
                    ConfigureInfoManager.getInstance().setLastServerTime(Long.parseLong(serverTime));
                    inviteCount = jsonObject.optString("inviteCount");
                    gold = jsonObject.optString("gold");
                    silver = jsonObject.optString("silver");
                    diamond = jsonObject.optString("diamond");


                    sendNotification("");
                }else if (code.equals("1000")){
                    EventBusCarrier eventBusCarrier = new EventBusCarrier();
                    eventBusCarrier.setEventType(EVENT_TYPE_MESSAGING);
                    eventBusCarrier.setObject("0");
                    EventBus.getDefault().post(eventBusCarrier); //普通事件发布
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (string.length()>0){


            }

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }
        //mCountent=content.replace("#count",inviteCount).replace("#name","userName");
        // Check if message contains a notification payload.


        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
  /*  @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }*/
    // [END on_new_token]



    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

        Log.d("updated",messageBody);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        EventBusCarrier eventBusCarrier = new EventBusCarrier();
        FcmTokenBean bean=new FcmTokenBean();
        bean.setFcmCoupn(gold);
        bean.setFcmTken(silver);
        bean.setFcmdiamond(diamond);
        bean.setFcmname(userName);
        bean.setFcminviteCount(inviteCount);
        bean.setFcmToken(messageBody);
        eventBusCarrier.setEventType(EVENT_TYPE_FCM);
        eventBusCarrier.setObject(bean);

        EventBus.getDefault().post(eventBusCarrier); //普通事件发布
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(this.getResources().getString(R.string.invite_yi))
                        .setContentText(this.getResources().getString(R.string.fcm_message_me)+inviteCount+this.getResources().getString(R.string.fcm_three))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

    }

    public boolean isPowerOfTwo(int n) {
        if(n <= 0)  return false;
        return (n &= (n-1)) == 0;
        //位运算，如果是2的n次方，转化成二进制是10000...的形式，所以它与上-1的数为0
    }
}
