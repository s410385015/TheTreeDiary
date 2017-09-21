package com.example.nako.thetreediary.myClass;

import android.app.Activity;
import android.content.Intent;

import com.example.nako.thetreediary.R;

/**
 * Created by Nako on 2017/1/8.
 */

public class Util {
    private static int sTheme;

    public final static int THEME_DEFAULT = 0;
    public final static int THEME_WHITE = 1;
    public final static int THEME_BLUE = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case 1:
                activity.setTheme(R.style.AppTheme);
                break;
            case 2:
                activity.setTheme(R.style.PinkTheme);
                break;
            case 3:
                activity.setTheme(R.style.PinkTheme);
                break;
        }
    }
}
