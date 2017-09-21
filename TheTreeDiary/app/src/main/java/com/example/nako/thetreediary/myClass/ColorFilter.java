package com.example.nako.thetreediary.myClass;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import static com.example.nako.thetreediary.EditActivity.tintDrawable;

/**
 * Created by Nako on 2017/1/8.
 */

public class ColorFilter {
    public ColorFilter(){}
    public Drawable colorDrawable(int DrawableId, int ColorId, Context context)
    {
        return tintDrawable(context.getResources().getDrawable(DrawableId), ColorStateList.valueOf(context.getResources().getColor(ColorId)));
    }
}
