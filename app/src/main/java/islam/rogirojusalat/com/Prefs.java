package islam.rogirojusalat.com;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

class Prefs {
    private static final String PREF_NAME = "rogirojusalat_prefs";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_FONT_ZOOM = "font_zoom";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String DELIMITER = "||";

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    static boolean isFavorite(Context context, String file) {
        for (String entry : prefs(context).getStringSet(KEY_FAVORITES, new LinkedHashSet<>())) {
            if (entry.startsWith(file + DELIMITER)) {
                return true;
            }
        }
        return false;
    }

    static void toggleFavorite(Context context, String file, String title) {
        SharedPreferences p = prefs(context);
        Set<String> current = new LinkedHashSet<>(p.getStringSet(KEY_FAVORITES, new LinkedHashSet<>()));
        String toRemove = null;
        for (String entry : current) {
            if (entry.startsWith(file + DELIMITER)) {
                toRemove = entry;
                break;
            }
        }
        if (toRemove != null) {
            current.remove(toRemove);
        } else {
            current.add(file + DELIMITER + title);
        }
        p.edit().putStringSet(KEY_FAVORITES, current).apply();
    }

    static Set<String> getFavoriteEntries(Context context) {
        return new LinkedHashSet<>(prefs(context).getStringSet(KEY_FAVORITES, new LinkedHashSet<>()));
    }

    static String fileFromEntry(String entry) {
        int idx = entry.indexOf(DELIMITER);
        return idx >= 0 ? entry.substring(0, idx) : entry;
    }

    static String titleFromEntry(String entry) {
        int idx = entry.indexOf(DELIMITER);
        return idx >= 0 ? entry.substring(idx + DELIMITER.length()) : entry;
    }

    static int getFontZoom(Context context) {
        return prefs(context).getInt(KEY_FONT_ZOOM, 100);
    }

    static void setFontZoom(Context context, int zoom) {
        prefs(context).edit().putInt(KEY_FONT_ZOOM, zoom).apply();
    }

    static boolean isDarkMode(Context context) {
        return prefs(context).getBoolean(KEY_DARK_MODE, false);
    }

    static void setDarkMode(Context context, boolean dark) {
        prefs(context).edit().putBoolean(KEY_DARK_MODE, dark).apply();
    }
}
