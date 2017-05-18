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

    public boolean get_enabled() {
        return prefs.getBoolean("enabled", true);
    }
    public void set_enabled(boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("enabled", value);
        editor.commit();
    }

    public boolean get_fractal_galaxy_enabled() {
        return prefs.getBoolean("fractal_galaxy_enabled", true);
    }
    public void set_fractal_galaxy_enabled(boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("fractal_galaxy_enabled", value);
        editor.commit();
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
}
