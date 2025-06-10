package session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "loginSession";
    private static final String KEY_IS_LOGIN = "isLogin";
    private static final String KEY_USERNAME = "username";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Tạo session login
    public void saveLogin(String username) {
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // Kiểm tra đã login chưa
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    // Lấy username
    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    // Xóa session (logout)
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
