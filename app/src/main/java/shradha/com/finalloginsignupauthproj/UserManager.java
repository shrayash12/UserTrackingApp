package shradha.com.finalloginsignupauthproj;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;

public class UserManager {
    static UserManager userManager = null;
    static Context mContext;
    SharedPreferences sharedPreferences;

    public static UserManager getInstance(Context context) {
        if (userManager == null) {
            mContext = context;
            userManager = new UserManager();
        }
        return userManager;
    }

    public void saveUserNamePassword(String email, String password) {
        sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SHARE_PREF_CREDENTIAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_SHARE_PREF_EMAIL, email);
        editor.putString(Constants.KEY_SHARE_PREF_PASSWORD, password);
        editor.apply();
    }

    public Pair<String, String> getUserNameAndPassword() {
        sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SHARE_PREF_CREDENTIAL, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Constants.KEY_SHARE_PREF_EMAIL, "");
        String password = sharedPreferences.getString(Constants.KEY_SHARE_PREF_PASSWORD, "");
        return new Pair(email, password);
    }

    public void saveIsAdmin(Boolean isAdmin) {
        sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SHARE_PREF_CREDENTIAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_SHARE_PREF_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public Boolean isAdmin() {
        sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SHARE_PREF_CREDENTIAL, Context.MODE_PRIVATE);
        Boolean isAdmin = sharedPreferences.getBoolean(Constants.KEY_SHARE_PREF_IS_ADMIN, false);
        return isAdmin;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SHARE_PREF_CREDENTIAL, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
