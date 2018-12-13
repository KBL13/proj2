package mobile.cs.fsu.edu.rentanything;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.support.constraint.Constraints.TAG;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    //to get token from anywhere in any activity/java class use this line
    //SharedPrefManager.getInstance(this).getToken()

    public static final String TOKEN_BROADCAST = "token_broadcast";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);
    }
    private void storeToken(String token)
    {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }
}
