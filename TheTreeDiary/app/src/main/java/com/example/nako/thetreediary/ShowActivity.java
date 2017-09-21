package com.example.nako.thetreediary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.nako.thetreediary.myClass.ColorFilter;
import com.example.nako.thetreediary.myClass.ListViewAdapter;
import com.example.nako.thetreediary.myClass.MoodSelecter;
import com.example.nako.thetreediary.myDatabase.ConfigClass;
import com.example.nako.thetreediary.myDatabase.DBHelper;
import com.example.nako.thetreediary.myDatabase.DiaryClass;
import com.example.nako.thetreediary.myDatabase.TreeClass;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;
    private int choose_index;
    private int color_id_t;
    private int color_id_d;
    private ColorFilter cc;
    public SQLiteDatabase db;
    public String DB_NAME = "MyTree";
    public DBHelper dbHelper;
    private ConfigClass config;
    private ArrayList<TreeClass> Forest;
    private ArrayList<DiaryClass> Diarys;
    private int TreeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        TreeID=bundle.getInt("TreeID",0);
        Log.wtf("TreeID",TreeID+"?");


        InitDatabase();
        GetThemeColor();

        setContentView(R.layout.activity_show);
        InitOpt();
        mListView = (ListView) findViewById(R.id.listview);


        mAdapter = new ListViewAdapter(this,Diarys);

        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choose_index=position;
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            intent.setClass(ShowActivity.this, TreeSelect.class);
            intent.putExtra("FromTree",TreeID);
            finish();
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void InitDatabase()
    {
        dbHelper = new DBHelper(this, DB_NAME);
        config = dbHelper.LoadConfig();
        Forest=dbHelper.LoadTrees();
        ArrayList<TreeClass> Forest=dbHelper.LoadTrees();
        Diarys=dbHelper.LoadDiaryByTree(Forest.get(TreeID).GetTree());


        if(Diarys==null)
            Diarys=new ArrayList<DiaryClass>();
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

    }
    public void InitOpt()
    {
        Intent intent = this.getIntent();

        TextView textView=(TextView)findViewById(R.id.treeTitle);
        textView.setText("    "+Forest.get(TreeID).GetTree());
        //Log.wtf("Tree+",Forest.get(0).GetTree()+" "+Forest.get(1).GetTree());
        ImageView imageView=(ImageView)findViewById(R.id.backtoselect);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("FromTree",TreeID );
                intent.setClass(ShowActivity.this,TreeSelect.class);
                startActivity(intent);
            }
        });
    }
}
