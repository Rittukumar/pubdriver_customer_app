package com.oceanstyxx.pubdriver.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by mohsin on 09/10/16.
 */

public class SessionManager
{
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";

    private static final String KEY_DRIVER_ID = "DRIVER_ID";

    private static final String KEY_IS_LOGGEDIN_REMEMBER = "isLoggedInRemember";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setKeyCustomerId(Integer id) {
        editor.putInt(KEY_CUSTOMER_ID, id);

        // commit changes
        editor.commit();
    }

    public Integer getCustomerId(){
        return pref.getInt(KEY_CUSTOMER_ID,0);
    }

    public void setKeyDriverId(String driverId) {
        editor.putString(KEY_DRIVER_ID, driverId);

        // commit changes
        editor.commit();
    }

    public String getDriverId(){
        return pref.getString(KEY_DRIVER_ID,"");
    }

    public void setLoginRemember(boolean isLoggedInRemeber)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN_REMEMBER, isLoggedInRemeber);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login remember session modified!");
    }

    public boolean isLoggedInRemember()
    {
        return pref.getBoolean(KEY_IS_LOGGEDIN_REMEMBER, false);
    }

}
