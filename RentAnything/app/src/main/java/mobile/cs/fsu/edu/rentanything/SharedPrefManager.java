package mobile.cs.fsu.edu.rentanything;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "test";
    private static final String SHARED_PREF_NAME_test = "test1";
    private static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_ACCESS_LOCATION = "loc";


    private static Context mContext;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context)
    {
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean storeToken(String token)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        return true;
    }

    public boolean storeLocation(String location)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME_test, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);
    }

    public String getLocation(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME_test, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_LOCATION,null);
    }
}
