package com.example.nako.thetreediary;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fontmanager.FontManager;
import com.example.nako.thetreediary.myClass.ImageLoader;
import com.example.nako.thetreediary.myClass.MoodSelecter;
import com.example.nako.thetreediary.myClass.MyViewPagerAdapter;
import com.example.nako.thetreediary.myDatabase.ConfigClass;
import com.example.nako.thetreediary.myDatabase.DBHelper;
import com.example.nako.thetreediary.myDatabase.DiaryClass;
import com.example.nako.thetreediary.myDatabase.TreeClass;
import com.example.nako.thetreediary.myWeather.Cloud;
import com.example.nako.thetreediary.myWeather.CloudHvRainView;
import com.example.nako.thetreediary.myWeather.CloudSunView;
import com.example.nako.thetreediary.myWeather.CloudView;
import com.example.nako.thetreediary.myWeather.SunView;
import com.example.nako.thetreediary.myWeather.WindView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import info.hoang8f.android.segmented.SegmentedGroup;



public class EditActivity extends AppCompatActivity {
    public ViewPager myEdit;
    public SegmentedGroup myOpt;
    public ArrayList viewList;
    public HTextView hTextView;
    public Button insert_Pic;
    public Button inser_Face;
    public Button DeletB;
    public Button SaveB;
    public Button b_color;
    public Button previewB;
    public View p_write;
    public View p_date;
    public BoomMenuButton bmb;
    public ImageView b_dateset;
    public ImageView b_timeset;
    public ImageView b_lock;
    public ImageView b_mood;
    public int color=Color.BLACK;
    public EditText editBox;
    public EditText editTitle;
    public PopupWindow popupWindow;
    public Dialog dialog;
    public Spinner spinner;
    public TextView tx;
    public TextView preview_t;
    public TextView preview_c;
    public Integer[] size;
    public float ts=14;
    public TextInsideCircleButton.Builder builder;
    public boolean timerflag=true;
    public boolean insertFlag=false;
    public boolean isPrivate=false;
    private static final int EDIT=1;
    Timer timer = new Timer(true);
    public  DatePickerDialog d;
    private Context context;
    private int mYear, mMonth, mDay,mhour,mmin;
    private boolean timeSetFlag=false;
    private String setDate="null",setTime="zero";
    private int weatherSelect=0;
    private AlertDialog.Builder pd;
    private int mood=0;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    private boolean backFlag;
    private int color_id_t;
    private int color_id_d;
    private com.example.nako.thetreediary.myClass.ColorFilter cc;
    public  SQLiteDatabase db;
    public  String DB_NAME = "MyTree";
    public  DBHelper dbHelper;
    private ArrayList<TreeClass> Forest;
    private ConfigClass config;
    private DiaryClass diaryClass;
    private int TreeID;
    private String MoodNum="0";
    private String WeatherNum="0";
    private int ID;
    private boolean SetFlag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        TreeID=bundle.getInt("TreeID",0);
        ID=bundle.getInt("value",-1);
        InitDatabase();
        //Log.wtf("?",TreeID+"?????");
        InitTime();

        GetThemeColor();
        setContentView(R.layout.editpage);
        context = this;
        IntentData();
        InitViewPager();
        InitSG();
        InitType();

        //InitWeather();
        //ArrayList<DiaryClass> t = dbHelper.LoadDiaryByTree("Demo");
        //Log.wtf("yoyo", t==null? "NULL":String.valueOf(t.size()));


    }
    private void InitDatabase()
    {
        dbHelper = new DBHelper(this, DB_NAME);
        config = dbHelper.LoadConfig();
        Forest=dbHelper.LoadTrees();
        diaryClass=new DiaryClass();
       // ArrayList<TreeClass>Forest=dbHelper.LoadTrees();
        if(ID!=-1)
        {
            diaryClass=dbHelper.LoadDiaryById(ID);
        }
        else {
            diaryClass.SetTree(Forest.get(TreeID).GetTree());
        }
    }


    private void GetThemeColor()
    {
        switch ( Integer.parseInt(config.GetTheme()))
        {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.PinkTheme);
                break;
            case 3:
                setTheme(R.style.GrassTheme);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }


        TypedArray att = obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.colorPrimaryDark});
        color_id_t=att.getResourceId(0,0);
        color_id_d=att.getResourceId(1,0);
        att.recycle();
        cc=new com.example.nako.thetreediary.myClass.ColorFilter();


    }
    private void IntentData()
    {
        backFlag=true;

        Intent intent = this.getIntent();
        int temp=intent.getIntExtra("Sour",0);

        if(temp==1)
            backFlag=false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(backFlag) {
                Log.wtf("?", "click back");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#FF0000'>確定離開?內容會被刪除唷!</font>"));

                Drawable d = getResources().getDrawable(android.R.drawable.ic_dialog_info);
                d = tintDrawable(d, ColorStateList.valueOf(getResources().getColor(color_id_d)));


                builder.setIcon(d);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(EditActivity.this, TreeSelect.class);
                                intent.putExtra("FromTree",TreeID);
                                finish();
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                DeletB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.create().show();
                    }
                });
                builder.create().show();
            }
            else {
                super.onKeyDown(keyCode, event);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    public void InitType()
    {

        TypedArray imgs=getResources().obtainTypedArray(R.array.type_imgs);
        int img_index=0;
        //imgs.getDrawable(0);
        //d=tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)));


        bmb = (BoomMenuButton)findViewById(R.id.bmb_type);
        assert bmb != null;
        bmb.clearBuilders();
        String[] s=getResources().getStringArray(R.array.type);
        String[] colors = getResources().getStringArray(R.array.colorsType);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                //.normalTextRes(R.string.type1)
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[0]));

        bmb.addBuilder(builder);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                //.normalTextRes(R.string.type2)
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[1]));
        bmb.addBuilder(builder);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                //.normalTextRes(R.string.type3)
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[2]));
        bmb.addBuilder(builder);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                //.normalTextRes(R.string.type4)
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[3]));
        bmb.addBuilder(builder);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[4]));
        bmb.addBuilder(builder);
        builder = new TextInsideCircleButton.Builder()
                .normalImageDrawable(
                        tintDrawable(imgs.getDrawable(img_index++), ColorStateList.valueOf(getResources().getColor(R.color.B)))
                )
                //.normalTextRes(R.string.type6)
                .pieceColor(Color.WHITE).normalColor(Color.parseColor(colors[5]));
        bmb.addBuilder(builder);


       bmb.setOnBoomListener(new OnBoomListener() {
           @Override
           public void onClicked(int index, BoomButton boomButton) {
               tx=(TextView)findViewById(R.id.textType);
               String[] s=getResources().getStringArray(R.array.type);
               tx.setText(s[index]);
               String[] colors = getResources().getStringArray(R.array.colorsTypeDark);
               tx.setTextColor(Color.parseColor(colors[index]));
               diaryClass.SetType(String.valueOf(index));
           }

           @Override
           public void onBackgroundClick() {

           }

           @Override
           public void onBoomWillHide() {

           }

           @Override
           public void onBoomDidHide() {

           }

           @Override
           public void onBoomWillShow() {

           }

           @Override
           public void onBoomDidShow() {

           }
       });
        if(ID!=-1)
        {
            tx=(TextView)findViewById(R.id.textType);
            s=getResources().getStringArray(R.array.type);
            tx.setText(s[Integer.parseInt(diaryClass.GetType())]);
            colors = getResources().getStringArray(R.array.colorsTypeDark);
            tx.setTextColor(Color.parseColor(colors[Integer.parseInt(diaryClass.GetType())]));
        }
    }

    public void InitMood()
    {
        Spinner spinner = (Spinner)findViewById(R.id.moodSpinner);
        ArrayAdapter<CharSequence> moodList = ArrayAdapter.createFromResource(EditActivity.this,
                R.array.mood,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(moodList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(color_id_d));
                b_mood=(ImageView)findViewById(R.id.viewMood);
                mood=position;
                Drawable d;
                switch (position) {
                    case 0:
                        d=getResources().getDrawable(R.drawable.ic_mood_happy);
                        d=tintDrawable(d, ColorStateList.valueOf(getResources().getColor(color_id_d)));
                        b_mood.setImageDrawable(d);
                        MoodNum="0";
                        break;
                    case 1:
                        d=getResources().getDrawable(R.drawable.ic_mood_unhappy);
                        d=tintDrawable(d, ColorStateList.valueOf(getResources().getColor(color_id_d)));
                        b_mood.setImageDrawable(d);
                        MoodNum="1";
                        break;
                    case 2:
                        d=getResources().getDrawable(R.drawable.ic_mood_soso);
                        d=tintDrawable(d, ColorStateList.valueOf(getResources().getColor(color_id_d)));
                        b_mood.setImageDrawable(d);
                        MoodNum="2";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(0);
        if(ID!=-1)
        {
            spinner.setSelection(Integer.parseInt(diaryClass.GetMood()));
        }
    }
    public void InitWeather()
    {
        Spinner spinner = (Spinner)findViewById(R.id.weatherSpinner);
        ArrayAdapter<CharSequence> weatherList = ArrayAdapter.createFromResource(EditActivity.this,
                R.array.weather,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(weatherList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(color_id_d));
                weatherSelect=pos;
                LinearLayout layout = (LinearLayout)findViewById(R.id.weatherlayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                params.width = 200;
                params.height = 200;
                layout.removeAllViews();
                switch (weatherSelect) {
                    case 0:
                        SunView sunView=new SunView(EditActivity.this);
                        sunView.setLayoutParams(params);
                        layout.addView(sunView);
                        WeatherNum="0";
                        break;
                    case 1:
                        CloudHvRainView hvR=new CloudHvRainView(EditActivity.this);
                        hvR.setLayoutParams(params);
                        layout.addView(hvR);
                        WeatherNum="1";
                        break;
                    case 2:
                        CloudSunView cloud = new CloudSunView(EditActivity.this);
                        cloud.setLayoutParams(params);
                        layout.addView(cloud);
                        WeatherNum="2";
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setSelection(0);

        if(ID!=-1)
        {
            spinner.setSelection(Integer.parseInt(diaryClass.GetWeather()));
        }
    }
    public void InitTime()
    {
        SimpleDateFormat sDateFormat ;
        String date ;
        sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = sDateFormat.format(new java.util.Date());
        setDate=date;
        sDateFormat = new SimpleDateFormat("HH:mm");
        date = sDateFormat.format(new java.util.Date());
        setTime=date;

        if(ID!=-1)
        {
            Log.wtf("IDIDID?",ID+"?");
            setDate=diaryClass.GetDate();
            setTime=diaryClass.GetTime();
            timeSetFlag=true;
        }
    }
    public void timeAnim()
    {
        hTextView = (HTextView)findViewById(R.id.timeCur);
        SimpleDateFormat sDateFormat ;
        String date ;


        if(!timeSetFlag) {
            sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = sDateFormat.format(new java.util.Date());
            setDate=date;
            diaryClass.SetDate(date);
            sDateFormat = new SimpleDateFormat("HH:mm");
            date = sDateFormat.format(new java.util.Date());
            diaryClass.SetTime(date);
            setTime=date;
            if (timerflag) {
                hTextView.setTextSize(45);
                hTextView.setAnimateType(HTextViewType.LINE);
                hTextView.animateText(setDate); // animate
                timerflag = !timerflag;
            } else {
                timerflag = !timerflag;
                hTextView.setTextSize(60);
                hTextView.setAnimateType(HTextViewType.LINE);
                hTextView.animateText(setTime);
            }
        }
        else{


            if (timerflag) {
                //hTextView = (HTextView) findViewById(R.id.timeShow);
                hTextView.setAnimateType(HTextViewType.LINE);
                hTextView.setTextSize(45);
                hTextView.animateText(setDate); // animate
                timerflag = !timerflag;
            } else {
                timerflag = !timerflag;

                //hTextView = (HTextView) findViewById(R.id.timeShow);
                hTextView.setAnimateType(HTextViewType.LINE);
                hTextView.setTextSize(60);
                hTextView.animateText(setTime); // animate
            }
        }
    }
    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            EditActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   timeAnim();
                }
            });

        }
    };
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
    public void InitButton()
    {
        editBox=(EditText)findViewById(R.id.textTitle);
        if(ID!=-1)
        {
            editBox.setText(diaryClass.GetTitle());
        }
        editBox=(EditText)findViewById(R.id.editText);

        SaveB=(Button)findViewById(R.id.textSave);
        SaveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               diaryClass.SetContent(Html.toHtml(editBox.getText()));
                EditText tx=(EditText)findViewById(R.id.textTitle);
                diaryClass.SetTitle(tx.getText().toString());
                diaryClass.SetDate(setDate);
                diaryClass.SetTime(setTime);
                if(isPrivate)
                    diaryClass.SetPrivate("1");
                diaryClass.SetMood(MoodNum);
                diaryClass.SetWeather(WeatherNum);
                dbHelper.SaveDiary(diaryClass);
                Intent intent = new Intent();
                intent.setClass(EditActivity.this,TreeSelect.class);
                intent.putExtra("FromTree",TreeID);
                finish();
                startActivity(intent);
            }
        });

        DeletB=(Button)findViewById(R.id.textDelete);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( Html.fromHtml("<font color='#FF0000'>確定刪除?</font>"));

        Drawable d=getResources().getDrawable(android.R.drawable.ic_dialog_info);
        d=tintDrawable(d, ColorStateList.valueOf(getResources().getColor(color_id_d)));


        builder.setIcon(d);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                            editBox=(EditText)findViewById(R.id.editText);
                            editBox.setText("");

                            editTitle=(EditText)findViewById(R.id.textTitle);
                            editBox.setText("");
                            Toast.makeText(context,"內容已刪除",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                    }
                });

        DeletB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.create().show();
            }
        });




        previewB=(Button)findViewById(R.id.textPreview);

        previewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(EditActivity.this);
                View pv = inflater.inflate(R.layout.preview_text, null);
                preview_t=(TextView)pv.findViewById(R.id.preView_title);
                preview_c=(TextView)pv.findViewById(R.id.preView_context);
                pd= new AlertDialog.Builder(EditActivity.this);
                pd.setView(pv);
                pd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                editBox=(EditText)findViewById(R.id.editText);
                preview_c.setText(editBox.getText());

                editTitle=(EditText)findViewById(R.id.textTitle);
                preview_t.setText(editTitle.getText());

                ImageView im=(ImageView)pv.findViewById(R.id.preView_type);
                im.setImageDrawable(null);
                im=(ImageView)pv.findViewById(R.id.preView_mood);
                im.setImageDrawable(null);
                im=(ImageView)pv.findViewById(R.id.preView_weather);
                im.setImageDrawable(null);
                im=(ImageView)pv.findViewById(R.id.preview_Edit);
                im.setImageDrawable(null);

                pd.show();
            }
        });

        b_lock=(ImageView)findViewById(R.id.lock);
        b_lock.setImageDrawable(cc.colorDrawable(R.drawable.icon_unlocker,color_id_d,context));
        b_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPrivate) {
                    b_lock.setImageDrawable(cc.colorDrawable(R.drawable.icon_locker, color_id_d, context));
                }
                else {
                    b_lock.setImageDrawable(cc.colorDrawable(R.drawable.icon_unlocker, color_id_d, context));
                }
                isPrivate=!isPrivate;
            }
        });
        if(ID!=-1)
        {
            if(diaryClass.GetPrivate().equals("1")) {
                b_lock.setImageDrawable(cc.colorDrawable(R.drawable.icon_locker, color_id_d, context));
                isPrivate=true;
            }
            else
            {
                b_lock.setImageDrawable(cc.colorDrawable(R.drawable.icon_unlocker, color_id_d, context));
                isPrivate=false;
            }
        }
    }
    public void InitTimeSet()
    {
        //timer.schedule(new MyTimerTask(), 5000, 5000);

        Calendar now = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dod=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String a="-";
                String b="-";
                if(monthOfYear<10)
                    a="-0";
                if(dayOfMonth<10)
                    b="-0";
                monthOfYear++;
                setDate=year+a+monthOfYear+b+dayOfMonth;
                Log.wtf("date",monthOfYear+"?");
                Toast.makeText(context,getResources().getString(R.string.date_set),Toast.LENGTH_LONG).show();

            }
        };

        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                dod,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(getResources().getColor(color_id_d));
        b_dateset=(ImageView)findViewById(R.id.setDate);
        b_dateset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
                timeSetFlag=true;

            }

        });


        TimePickerDialog.OnTimeSetListener tot=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                    String a="";
                    String b="";
                    if(hourOfDay<10)
                        a="0";
                    if(minute<10)
                        b="0";
                    mhour=hourOfDay;
                    mmin=minute;
                    setTime=a+mhour+":"+b+mmin;
                    timeAnim();

                    Toast.makeText(context,getResources().getString(R.string.time_set),Toast.LENGTH_LONG).show();


            }
        };
        final TimePickerDialog tpd=TimePickerDialog.newInstance(
                tot,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setAccentColor(getResources().getColor(color_id_d));
        b_timeset=(ImageView) findViewById(R.id.setTime);
        b_timeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show(getFragmentManager(), "Timepickerdialog");
                timeSetFlag=true;

            }
        });
    }
    public void InitEditor()
    {

        editBox=(EditText)findViewById(R.id.editText);
        editBox.setTextColor(Color.BLACK);
        //editBox.setTextSize();
        if(ID!=-1){
            String html = diaryClass.GetContent();
            html.replace("<p dir=\"ltr\">", "");
            html.replace("</p>", "");
            editBox.append(Html.fromHtml(html, new Html.ImageGetter() {
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
        }
        editBox.addTextChangedListener(new TextWatcher() {

            int l=0;
            int index=0;
            int code;
            char registered;
            boolean cflag;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code=0;
                cflag=false;

                if(s.length()>0) {
                    registered = s.charAt(s.length() - 1);
                    code = registered;
                    if((code<12549||code>12586)&&(s.length()>l||code>13312))
                        cflag=true;

                }
                Log.wtf("?",code+"?"+cflag);


                if(cflag&&!insertFlag) {
                    editBox.removeTextChangedListener(this);
                    SpannableString spannableString = new SpannableString(s.subSequence(s.length()-1,s.length()));
                   // AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) ts, true);
                    //spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(color),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editBox.getText().delete(s.length()-1, s.length());
                    editBox.append(spannableString);
                    editBox.addTextChangedListener(this);

                }

                l=s.length();
                index=editBox.getSelectionStart();

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        inser_Face = (Button) findViewById(R.id.insertFace);
        inser_Face.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                insertFlag=true;
                MoodSelecter moodSelecter = new MoodSelecter();
                moodSelecter.show(getSupportFragmentManager(), "moodSelect");
                insertFlag=false;
                return;
            }
        });


    }
    public void InitTextSet()
    {
        insert_Pic=(Button)findViewById(R.id.insertP);
        insert_Pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageLoader imageLoader = new ImageLoader();
                imageLoader.show(getSupportFragmentManager(), "imageLoader");

            }
        });

        spinner = (Spinner)findViewById(R.id.textSizeS);
        size = new Integer[]{14,16,18,20,22,24,26,28,30,32,34,36};
        ArrayAdapter<Integer> SizeList = new ArrayAdapter<Integer>(EditActivity.this,android.R.layout.simple_spinner_item,size);
        SizeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SizeList);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ts=(Integer)spinner.getSelectedItem();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }) ;
        spinner.setVisibility(View.INVISIBLE);
        b_color=(Button)findViewById(R.id.textColor);
        b_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(EditActivity.this)
                        .setTitle("Choose color")
                        .initialColor(color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                    color=selectedColor;
                                    b_color.setBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

    }

    public void InitSG()
    {
        myOpt=(SegmentedGroup)findViewById(R.id.SG_Opt);
        myOpt.setTintColor(getResources().getColor(color_id_d));
        myOpt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.write:
                        myEdit.setCurrentItem(0);
                        return;
                    case R.id.date:
                        myEdit.setCurrentItem(1);
                        return;
                }
            };
        });
    }
    public void InitViewPager(){

        RadioButton initB=(RadioButton)findViewById(R.id.write);
        initB.toggle();

        myEdit=(ViewPager)findViewById(R.id.Diaryview);
        final LayoutInflater lf=getLayoutInflater().from(this);
        p_write=lf.inflate(R.layout.edit_write,null);
        p_date=lf.inflate(R.layout.edit_date,null);

        ArrayList viewList = new ArrayList<View>();
        viewList.add(p_write);
        viewList.add(p_date);




        myEdit.setAdapter(new MyViewPagerAdapter(viewList));

        myEdit.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            boolean flag=true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if(position==0&&flag)
                    {
                        Log.wtf("t","?");
                        flag=false;
                        onPageSelected(0);
                    }

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton initB;
                LinearLayout l=(LinearLayout)findViewById(R.id.textToolbar);
                switch (position){
                    case 0:
                        initB=(RadioButton)findViewById(R.id.write);
                        initB.toggle();
                        l.setVisibility(LinearLayout.VISIBLE);
                        if(!SetFlag) {
                            InitEditor();
                            InitTextSet();
                            InitButton();

                            InitTimeSet();
                            InitType();
                        }
                        return;
                    case 1:
                        initB=(RadioButton)findViewById(R.id.date);
                        initB.toggle();
                        l.setVisibility(LinearLayout.GONE);
                        timeAnim();
                        if(!SetFlag) {
                            InitWeather();
                            InitMood();
                            SetFlag = true;
                        }
                        return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        timer.schedule(new MyTimerTask(), 5000, 5000);
        myEdit.setCurrentItem(0);
    }



}
