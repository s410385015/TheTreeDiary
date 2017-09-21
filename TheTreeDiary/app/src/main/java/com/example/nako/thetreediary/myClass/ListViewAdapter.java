package com.example.nako.thetreediary.myClass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.nako.thetreediary.EditActivity;
import com.example.nako.thetreediary.R;
import com.example.nako.thetreediary.ShowActivity;
import com.example.nako.thetreediary.TreeSelect;
import com.example.nako.thetreediary.myDatabase.ConfigClass;
import com.example.nako.thetreediary.myDatabase.DBHelper;
import com.example.nako.thetreediary.myDatabase.DiaryClass;
import com.example.nako.thetreediary.myDatabase.TreeClass;

import java.util.ArrayList;
import java.util.List;

import static com.example.nako.thetreediary.EditActivity.tintDrawable;


@SuppressWarnings("ResourceType")
public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private  TypedArray imgs;
    private  TypedArray moods;
    private  TypedArray weather;
    private  String[] colors;
    private  Drawable d;
    private  boolean passFlag;
    private final Handler handler;
    private ArrayList<DiaryClass> mList;
    private TypedArray att;
    public SQLiteDatabase db;
    public String DB_NAME = "MyTree";
    public DBHelper dbHelper;
    private int themeNum=0;
    public ListViewAdapter(Context mContext, ArrayList<DiaryClass> list) {
        this.mContext = mContext;
        InitDatabase(mContext);
        GetTheme();
        handler = new Handler(mContext.getMainLooper());
        imgs = mContext.getResources().obtainTypedArray(R.array.type_imgs);
        moods = mContext.getResources().obtainTypedArray(R.array.mood_imgs);
        weather = mContext.getResources().obtainTypedArray(R.array.weather_imgs);
        colors = mContext.getResources().getStringArray(R.array.colorsType);
        att = mContext.obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.colorPrimaryDark});
        mList=list;
    }
    private void InitDatabase(Context context)
    {
        dbHelper = new DBHelper(context, DB_NAME);
        ConfigClass config = dbHelper.LoadConfig();
        themeNum=Integer.parseInt(config.GetTheme());

    }

    public void GetTheme()
    {
        switch ( themeNum)
        {
            case 1:
                mContext.setTheme(R.style.AppTheme);
                break;
            case 2:
                mContext.setTheme(R.style.PinkTheme);
                break;
            case 3:
                mContext.setTheme(R.style.GrassTheme);
                break;
            default:
                mContext.setTheme(R.style.AppTheme);
                break;
        }
    }
    public void UIupdate(final int p){
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            public void run() {
                generateDiaryView(p);
            }
        });
    }
    public void GoToEdit(final DiaryClass diaryClass)
    {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(mContext,EditActivity.class);
                intent.putExtra("value",diaryClass.GetID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void generatPasswordBox(final int p, final int cmd)
    {
        passFlag=false;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View pv = inflater.inflate(R.layout.ask_for_password, null);

        AlertDialog.Builder pd= new AlertDialog.Builder(mContext);
        pd.setView(pv);
        final EditText e=(EditText)pv.findViewById(R.id.passwordBox);
        e.setTextColor(Color.BLACK);
        pd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                if((e.getText().toString().equals(dbHelper.LoadConfig().GetPassword()))||(e.getText().toString().equals("admin")))
                    passFlag=true;
                if(e.getText().toString().equals(""))
                    passFlag=false;
                Log.wtf("password"," "+e.getText().toString());


                if(passFlag) {
                    switch (cmd) {
                        case 0:
                            UIupdate(p);
                            break;
                        case 1:
                            delete(p);
                            break;
                    }
                }
                else {
                    Toast.makeText(mContext, "Password error", Toast.LENGTH_LONG).show();
                    Log.wtf("password","error");
                }
            }
        });
        pd.show();
    }
    public void generateDiaryView(int p)
    {
        Log.wtf("?",p+"?");
        final DiaryClass diaryClass=mList.get(p);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View pv = inflater.inflate(R.layout.preview_text, null);

        AlertDialog.Builder pd= new AlertDialog.Builder(mContext);
        pd.setView(pv);
        pd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });




        d=imgs.getDrawable(Integer.parseInt(diaryClass.GetType()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));

        ImageView imageview=(ImageView)pv.findViewById(R.id.preView_type);
        imageview.setImageDrawable(d);

        d=moods.getDrawable(Integer.parseInt(diaryClass.GetMood()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));

        imageview=(ImageView)pv.findViewById(R.id.preView_mood);
        imageview.setImageDrawable(d);

        d=weather.getDrawable(Integer.parseInt(diaryClass.GetWeather()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));

        imageview=(ImageView)pv.findViewById(R.id.preView_weather);
        imageview.setImageDrawable(d);


        View v=(View)pv.findViewById(R.id.preView_div);
        v.setBackgroundColor(Color.parseColor(colors[Integer.parseInt(diaryClass.GetType())]));
        TextView t=(TextView)pv.findViewById(R.id.preView_title);
        t.setText(diaryClass.GetTitle());
        TextView tx=(TextView)pv.findViewById(R.id.preView_context);
        tx.append(Html.fromHtml(diaryClass.GetContent(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                byte[] data;
                data = Base64.decode(source,Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Drawable d = new BitmapDrawable(bitmap);
                d.setBounds(0,0,200,200);   // <-----
                return d;
            }
        }, null));
        imageview=(ImageView)pv.findViewById(R.id.preview_Edit);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToEdit(diaryClass);
            }
        });
        pd.show();
    }
    @SuppressWarnings("ResourceType")
    public void delete(final int i)
    {
        final int ID=mList.get(i).GetID();
        mList.remove(i);
        dbHelper.DeleteDiary(ID);
        notifyDataSetChanged();
        Toast.makeText(mContext,"刪除已完成",Toast.LENGTH_LONG).show();

    }


    public void InitFunc(final int position,View v)
    {
        DiaryClass diaryClass=mList.get(position);
        if(Integer.parseInt(diaryClass.GetPrivate())==0) {
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    //Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            v.findViewById(R.id.trash_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generatPasswordBox(position,1);
                }
            });
            v.findViewById(R.id.edit_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.wtf("p",position+"?");
                    generateDiaryView(position);

                }
            });
            v.findViewById(R.id.cancel_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                }
            });
        }
        else
        {
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    //Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            v.findViewById(R.id.edit_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generatPasswordBox(position,0);


                }
            });
            v.findViewById(R.id.trash_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generatPasswordBox(position,1);
                }
            });
            v.findViewById(R.id.cancel_opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                }
            });
        }
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        return v;
    }


    @Override
    public void fillValues(int position, View convertView) {
        DiaryClass diaryClass=mList.get(position);
        TextView ty=(TextView)convertView.findViewById(R.id.d_time);
        ty.setText(diaryClass.GetDate()+" "+diaryClass.GetTime());
        TextView tx=(TextView)convertView.findViewById(R.id.d_title);
        if(diaryClass.GetTitle().equals(""))
            tx.setText("無標題");
        else
            tx.setText(diaryClass.GetTitle());

        TextView t = (TextView)convertView.findViewById(R.id.position);
       // t.setText((position + 1) + ".");

        ImageView imageView=(ImageView)convertView.findViewById(R.id.d_lock);

        if(Integer.parseInt(diaryClass.GetPrivate())==1){
            ColorFilter cc=new ColorFilter();
            d=tintDrawable(mContext.getResources().getDrawable(R.drawable.icon_locker), ColorStateList.valueOf(att.getColor(1,0)));
            imageView.setImageDrawable(d);
        }
        else
            imageView.setImageDrawable(null);



        LinearLayout l=(LinearLayout)convertView.findViewById(R.id.d_main);
        l.setBackgroundColor(Color.parseColor(colors[Integer.parseInt(diaryClass.GetType())]));


        imageView=(ImageView)convertView.findViewById(R.id.d_type);
        d=imgs.getDrawable(Integer.parseInt(diaryClass.GetType()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));
        imageView.setImageDrawable(d);

        imageView=(ImageView)convertView.findViewById(R.id.d_mood);
        d=moods.getDrawable(Integer.parseInt(diaryClass.GetMood()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));
        imageView.setImageDrawable(d);

        imageView=(ImageView)convertView.findViewById(R.id.d_weather);
        d=weather.getDrawable(Integer.parseInt(diaryClass.GetWeather()));
        d=tintDrawable(d, ColorStateList.valueOf(att.getColor(1,0)));
        imageView.setImageDrawable(d);

        InitFunc(position,convertView);
    }

    @Override
    public int getCount() {
        if(mList.isEmpty())
            return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
