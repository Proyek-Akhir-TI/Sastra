package sastra.panji.dhimas.proggeres.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    /**
     * Pendeklarasian key-data berupa String, untuk sebagai wadah penyimpanan data.
     * Jadi setiap data mempunyai key yang berbeda satu sama lain
     */
    static final String KEY_USER_TEREGISTER = "user", KEY_PASS_TEREGISTER = "pass";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";

    public static String getUrlActive(Context context) {
        return getSharedPreference(context).getString(URL_ACTIVE, "");
    }

    public static void setUrlActive(Context context, String url) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(URL_ACTIVE, url);
        editor.apply();
    }

    public static String getBearerUser(Context context) {
        return getSharedPreference(context).getString(BEARER_USER, "");
    }

    public static void setBearerUser(Context context, String bearer) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(BEARER_USER, bearer);
        editor.apply();
    }

    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String URL_ACTIVE = "url_candang";
    static final String BEARER_USER = "bearer";

    public static String getUrlImg(Context context) {
        return getSharedPreference(context).getString(URL_IMG, "");
    }

    public static void setUrlImg(Context context, String url) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(URL_IMG, url);
        editor.apply();
    }

    public static String getEMAIL(Context context) {
        return getSharedPreference(context).getString(EMAIL, "");
    }

    public static void setEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public static String getNAMA(Context context) {
        return getSharedPreference(context).getString(NAMA, "");
    }

    public static void setNama(Context context, String nama) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(NAMA, nama);
        editor.apply();
    }

    public static String getKELOMPOK(Context context) {
        return getSharedPreference(context).getString(KELOMPOK, "");
    }

    public static void setKelompok(Context context, String kelompok) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KELOMPOK, kelompok);
        editor.apply();
    }

    static final String URL_IMG = "img", EMAIL = "email", NAMA = "nama";
    static final String KELOMPOK = "0";
    static final String NAMA_KANDANG = "kandang";
    static final String STATUS_KANDANG = "status";

    public static String getNamaKandang(Context context) {
        return getSharedPreference(context).getString(NAMA_KANDANG, "");
    }

    public static void setNamaKandang(Context context, String nm_kandang) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(NAMA_KANDANG, nm_kandang);
        editor.apply();
    }

    public static String getStatusKandang(Context context) {
        return getSharedPreference(context).getString(STATUS_KANDANG, "");
    }

    public static void setStatusKandang(Context context, String status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(STATUS_KANDANG, status);
        editor.apply();
    }

    /**
     * Pendlakarasian Shared Preferences yang berdasarkan paramater context
     */
    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Deklarasi Edit Preferences dan mengubah data
     * yang memiliki key isi KEY_USER_TEREGISTER dengan parameter username
     */
    public static void setRegisteredUser(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_TEREGISTER, username);
        editor.apply();
    }

    /**
     * Mengembalikan nilai dari key KEY_USER_TEREGISTER berupa String
     */
    public static String getRegisteredUser(Context context) {
        return getSharedPreference(context).getString(KEY_USER_TEREGISTER, "");
    }

    /**
     * Deklarasi Edit Preferences dan mengubah data
     * yang memiliki key KEY_PASS_TEREGISTER dengan parameter password
     */
    public static void setRegisteredPass(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PASS_TEREGISTER, password);
        editor.apply();
    }

    /**
     * Mengembalikan nilai dari key KEY_PASS_TEREGISTER berupa String
     */
    public static String getRegisteredPass(Context context) {
        return getSharedPreference(context).getString(KEY_PASS_TEREGISTER, "");
    }

    /**
     * Deklarasi Edit Preferences dan mengubah data
     * yang memiliki key KEY_USERNAME_SEDANG_LOGIN dengan parameter username
     */
    public static void setLoggedInUser(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME_SEDANG_LOGIN, username);
        editor.apply();
    }

    /**
     * Mengembalikan nilai dari key KEY_USERNAME_SEDANG_LOGIN berupa String
     */
    public static String getLoggedInUser(Context context) {
        return getSharedPreference(context).getString(KEY_USERNAME_SEDANG_LOGIN, "");
    }

    /**
     * Deklarasi Edit Preferences dan mengubah data
     * yang memiliki key KEY_STATUS_SEDANG_LOGIN dengan parameter status
     */
    public static void setLoggedInStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN, status);
        editor.apply();
    }

    /**
     * Mengembalikan nilai dari key KEY_STATUS_SEDANG_LOGIN berupa boolean
     */
    public static boolean getLoggedInStatus(Context context) {
        return getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN, false);
    }

    /**
     * Deklarasi Edit Preferences dan menghapus data, sehingga menjadikannya bernilai default
     * khusus data yang memiliki key KEY_USERNAME_SEDANG_LOGIN dan KEY_STATUS_SEDANG_LOGIN
     */
    public static void clearLoggedInUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        editor.remove(BEARER_USER);
        editor.remove(URL_IMG);
        editor.remove(NAMA);
        editor.remove(EMAIL);
        editor.remove(KELOMPOK);
        editor.apply();
    }

    public static void clearKandang(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(URL_ACTIVE);
        editor.remove(NAMA_KANDANG);
        editor.remove(STATUS_KANDANG);
        editor.apply();
    }
}