package net.walker9.fractaly;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by joel on 2/27/17.
 */

public class PersistentData {
    private static final String PREFS_NAME = "FractalyData";

    private SharedPreferences prefs;

    public PersistentData(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public ArrayList<String> get_active_images() {
        Set<String> set = prefs.getStringSet("active_images", null);
        if (set == null) {
            return new ArrayList<String>();
        } else {
            return new ArrayList<String>(set);
        }
    }
    public void set_active_images(ArrayList<String> active_images) {
        Set<String> set = new HashSet<String>(active_images);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("active_images", set);
        editor.commit();
    }

    public long get_next_request_timestamp() {
        return prefs.getLong("next_request_timestamp", 0);
    }
    public void set_next_request_timestamp(long timestamp) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("next_request_timestamp", timestamp);
        editor.commit();
    }

    public String get_poll_key() {
        return prefs.getString("poll_key", null);
    }
    public void set_poll_key(String poll_key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("poll_key", poll_key);
        editor.commit();
    }
}
