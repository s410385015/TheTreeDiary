package com.example.nako.thetreediary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.nako.thetreediary.myClass.Achievementdata;
import com.example.nako.thetreediary.myClass.ColorFilter;
import com.example.nako.thetreediary.myClass.CustomListAdapter;
import com.example.nako.thetreediary.myClass.ListViewAdapter;
import com.example.nako.thetreediary.myClass.Util;
import com.example.nako.thetreediary.myDatabase.ConfigClass;
import com.example.nako.thetreediary.myDatabase.DBHelper;
import com.example.nako.thetreediary.myDatabase.DiaryClass;
import com.example.nako.thetreediary.myDatabase.TreeClass;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

public class TreeSelect extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<PageView> pageList;
    private List<CreateView> createList;
    private int pageNum=0;
    private Toolbar toolbar;
    private int AddtreeNum=0;
    private MaterialSearchBar searchBar;
    private MaterialSearchBar.OnSearchActionListener sListener;
    private int color_id_t;
    private int color_id_d;
    private ColorFilter cc;
    private EditText eUser;
    private EditText ePass;

    public SQLiteDatabase db;
    public String DB_NAME = "MyTree";
    public DBHelper dbHelper;
    private ConfigClass config;
    private int themeNum=0;
    private ArrayList<TreeClass> Forest;
    private int treeAmount=0;
    private Context myContext;
    private int DestroyNum=0;
    private int SearchNum=0;
    private Drawer result;
    private NumberProgressBar numberProgressBar;
    private int PageItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        PageItem=intent.getIntExtra("FromTree",-1);


        InitDatabase();
        GetThemeColor();
        setContentView(R.layout.activity_tree_select);
        myContext=this;
        toolbar = (Toolbar) findViewById(R.id.toolbar_treeselect);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle("");
        InitLeftMenu();
        InitEditButton();
        InitViewPager();


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = this.getIntent();
        PageItem=intent.getIntExtra("FromTree",-1);
        InitDatabase();
        InitViewPager();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            MenuSwitch(2);
            result.setSelection(1,false);
            result.closeDrawer();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void InitDatabase()
    {
        dbHelper = new DBHelper(this, DB_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(db,1,1);
        config = dbHelper.LoadConfig();
        themeNum=Integer.parseInt(config.GetTheme());
        Forest=dbHelper.LoadTrees();
        if(Forest!=null)
            treeAmount=Forest.size();
    }

    private void GetThemeColor()
    {

        switch ( Integer.parseInt(config.GetTheme()))
        {
            case 1:
                themeNum=1;
                setTheme(R.style.AppTheme);
                break;
            case 2:
                themeNum=2;
                setTheme(R.style.PinkTheme);
                break;
            case 3:
                themeNum=3;
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
        cc=new ColorFilter();
    }
    public void InitEditButton()
    {
        Button b=(Button)findViewById(R.id.gotoEdit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(treeAmount==0)
                {
                    Toast.makeText(myContext, "尚未有樹存在，請新建", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(TreeSelect.this,EditActivity.class);
                intent.putExtra("TreeID",pageNum);
                startActivity(intent);
            }
        });
    }
    public void InitViewPager()
    {
        mViewPager = (ViewPager) findViewById(R.id.treeSwitch);
        pageList = new ArrayList<>();
        for(int i=0;i<treeAmount;i++)
            pageList.add(new PageView(TreeSelect.this,Forest.get(i)));
        if(treeAmount==0)
            pageList.add(new PageView(TreeSelect.this,null));
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageNum=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(PageItem==-1)
            mViewPager.setCurrentItem(0);
        else
            mViewPager.setCurrentItem(PageItem);
    }


    private void MenuSwitch(int i)
    {

        switch (i)
        {
            case 2:
                EditButtonMode(true);
                ToolBarMode(true);
                InitViewPager();
                break;
            case 3:
                EditButtonMode(false);
                ToolBarMode(true);
                ViewChange(i);
                break;
            case 4:
                EditButtonMode(false);
                ToolBarMode(true);
                ViewChange(i);
                break;
            case 5:
                EditButtonMode(false);
                ToolBarMode(true);
                ViewChange(i);
                break;
            case 6:
                EditButtonFuc();
                ToolBarMode(true);
                ViewChange(i);
                break;
            case -1:
                DestroyNum++;
                if(DestroyNum==5) {
                    dbHelper.onUpgrade(dbHelper.getWritableDatabase(),1, 1);
                    Toast.makeText(myContext,"Destroy Database",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
                break;
        }

    }
    private void ViewChange(int i)
    {
        mViewPager = (ViewPager) findViewById(R.id.treeSwitch);
        createList = new ArrayList<>();
        createList.add(new CreateView(TreeSelect.this,i));

        mViewPager.setAdapter(new CreateAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageNum=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    private void InitLeftMenu()
    {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.profile_tree)
                .addProfiles(
                        new ProfileDrawerItem().withName(config.GetUserName()).withIcon(getResources().getDrawable(R.drawable.profile_icon))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        return false;
                    }
                })
                .build();

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("我的小樹").withIcon(R.drawable.ic_menu_mytree);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("種植小樹").withIcon(R.drawable.ic_menu_add);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("搜尋").withIcon(R.drawable.ic_menu_search);;
        SecondaryDrawerItem item4 =new SecondaryDrawerItem().withIdentifier(4).withName("成就").withIcon(R.drawable.ic_topic_diary);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("設定").withIcon(R.drawable.ic_settings_black_24dp);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        item1,
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        MenuSwitch(position);
                        Log.wtf("?",position+"?????????????");
                        return false;
                    }
                })
                .build();


        result.addStickyFooterItem(new PrimaryDrawerItem().withName("關於").withIdentifier(6));
        result.setSelection(1,false);
        result.closeDrawer();

    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class CreateAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return createList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(createList.get(position));
            return createList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    public class PageView extends RelativeLayout {

        public PageView(Context context,TreeClass tree) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tree_detail, null);
            if(tree==null) {
                TextView textView = (TextView) view.findViewById(R.id.textTree);
                textView.setText("尚未有樹建立");
            }
            else {
                TextView textView = (TextView) view.findViewById(R.id.textTree);
                textView.setText(tree.GetTree());
                textView=(TextView)view.findViewById(R.id.Diarytotal);

                numberProgressBar=(NumberProgressBar)view.findViewById(R.id.number_progress_bar);

                int []exp={0,5,15,35,60};
                int total;
                if(dbHelper.LoadDiaryByTree(tree.GetTree())==null)
                 total=0;
                else
                    total=dbHelper.LoadDiaryByTree(tree.GetTree()).size();
                int expbar=0;
                int level=1;
                for(int i=1;i<=4;i++)
                {
                    if(total>exp[i]) {
                        level++;
                        expbar=exp[i];
                    }
                }


                TypedArray imgs = getResources().obtainTypedArray(R.array.tree_imgs);
                ImageView imageView = (ImageView) view.findViewById(R.id.treeImage);

                imageView.setImageDrawable(imgs.getDrawable(tree.GetTreePhoto()+((level-1)*5)));
                if(level<=4)
                    numberProgressBar.setProgress((total-expbar)*100/((exp[level]-exp[level-1])));
                else
                    numberProgressBar.setProgress(100);
                if(total>=exp[4])
                    textView.setText("日記總數:"+total+"   LV: MAX");
                else
                    textView.setText("日記總數:"+total+"   LV: "+level);

                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(TreeSelect.this, ShowActivity.class);
                        intent.putExtra("TreeID", pageNum);
                        Log.wtf("Page",pageNum+"?");
                        finish();
                        startActivity(intent);

                    }
                });
            }
            addView(view);
        }
    }
    private void EditButtonMode(boolean on)
    {
        Button b=(Button)findViewById(R.id.gotoEdit);
        b.setBackground(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
        if(on) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(treeAmount==0) {
                        Toast.makeText(myContext, "尚未有樹存在，請新建", Toast.LENGTH_LONG).show();
                        return;
                    }Intent intent = new Intent();
                    intent.setClass(TreeSelect.this,EditActivity.class);
                    intent.putExtra("TreeID",pageNum);
                    startActivity(intent);
                }
            });
        }
        else
            b.setVisibility(View.INVISIBLE);
    }
    private void EditButtonFuc()
    {
        Button b=(Button)findViewById(R.id.gotoEdit);


        b.setBackground(getResources().getDrawable(R.drawable.ic_save_white_24dp));
        b.setVisibility(View.VISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ePass.getText().toString().equals(""))
                {
                    Toast.makeText(myContext,"密碼不可留空",Toast.LENGTH_LONG).show();
                    return;
                }
                dbHelper.UpdateTheme(String.valueOf(themeNum));
                dbHelper.UpdateUserName(eUser.getText().toString());
                dbHelper.UpdatePassword(ePass.getText().toString());
                Intent intent = getIntent();
                startActivity(intent);
                finish();
            }
        });
    }
    private void ToolBarMode(boolean on)
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_treeselect);
        if(on)
            toolbar.setVisibility(View.VISIBLE);
        else
            toolbar.setVisibility(View.INVISIBLE);
    }

    private void PopPassword(final Context context, final MaterialTextField mt)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View pv = inflater.inflate(R.layout.ask_for_password, null);
        AlertDialog.Builder pd= new AlertDialog.Builder(context);
        pd.setView(pv);
        final EditText e=(EditText)pv.findViewById(R.id.passwordBox);
        e.setTextColor(getResources().getColor(color_id_t));
        pd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                if(e.getText().toString().equals(dbHelper.LoadConfig().GetPassword())||e.getText().toString().equals("admin"))
                {
                   mt.toggle();
                }
                else {
                    Toast.makeText(context, "Password error", Toast.LENGTH_LONG).show();
                    Log.wtf("password","error");
                }
            }
        });
        pd.show();

    }

    public class  CreateView extends RelativeLayout{
        public SQLiteDatabase db;
        public String DB_NAME = "MyTree";
        public DBHelper dbHelper;

        public CreateView(Context context,int i)
        {
            super(context);
            dbHelper = new DBHelper(context, DB_NAME);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            switch (i)
            {
                case 3:
                    CreateAdd(context);
                    break;
                case 4:
                    CreateSearch(context);
                    break;
                case 5:
                    CreateReward(context);
                    break;
                case 6:
                    CreateSetting(context);
                    break;
                default:
                    Create(context);
                    break;
            }
        }


        public void CreateAdd(Context context)
        {
            AddtreeNum=0;
            View view = LayoutInflater.from(context).inflate(R.layout.tree_adder, null);
            final TextView t=(TextView)view.findViewById(R.id.seTitle);
            t.setText("設定名稱");
            t.setTextColor(ContextCompat.getColor(context,color_id_t));
            final EditText e=(EditText)view.findViewById(R.id.Addedit);
            e.setTextColor(ContextCompat.getColor(context,color_id_d));

            MaterialSpinner spinner = (MaterialSpinner)view.findViewById(R.id.Addspinner);
            spinner.setArrowColor(ContextCompat.getColor(context,color_id_t));
            spinner.setTextColor(ContextCompat.getColor(context,color_id_t));
            spinner.setItems("樹A", "樹B", "樹C", "樹D", "樹E");
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                }
            });

            spinner.setVisibility(INVISIBLE);

            final TypedArray imgs=getResources().obtainTypedArray(R.array.add_imgs);

            ImageView imageView=(ImageView)view.findViewById(R.id.leftOpt);
            imageView.setImageDrawable(cc.colorDrawable(R.drawable.tree_add_left,color_id_t,context));
            final ImageView im=(ImageView)view.findViewById(R.id.tree_add_image);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                        AddtreeNum=((AddtreeNum-1)%5);
                    if(AddtreeNum<0)
                        AddtreeNum+=5;
                    im.setImageDrawable(imgs.getDrawable(AddtreeNum));
                }
            });
            imageView=(ImageView)view.findViewById(R.id.rightOpt);
            imageView.setImageDrawable(cc.colorDrawable(R.drawable.tree_add_right,color_id_t,context));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                        AddtreeNum=(AddtreeNum+1)%5;
                    im.setImageDrawable(imgs.getDrawable(AddtreeNum));
                }
            });
            Button ab=(Button)view.findViewById(R.id.AddButton);
            ab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TreeClass treeClass=new TreeClass();
                    treeClass.SetTree(e.getText().toString());
                    if(!treeClass.GetTree().equals("")) {
                        treeClass.SetTreePhoto(AddtreeNum);
                        dbHelper.SaveTree(treeClass);
                        Intent intent = getIntent();
                        intent.putExtra("FromTree", pageList.size());
                        finish();
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(myContext,"樹的名字不可為空",Toast.LENGTH_LONG).show();
                }
            });
            //imgs.recycle();
            addView(view);
        }
        public void CreateSearch(final Context context)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.diary_search, null);
            final ListView mListView = (ListView)view.findViewById(R.id.listview);
            MaterialSpinner spinner = (MaterialSpinner)view.findViewById(R.id.Searchspinner);
            spinner.setArrowColor(ContextCompat.getColor(context,color_id_t));
            spinner.setTextColor(ContextCompat.getColor(context,color_id_t));
            spinner.setItems("標題", "內容",  "類型");
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            SearchNum=position;
                }
            });
            ImageView imageView=(ImageView)view.findViewById(R.id.search_button);
            imageView.setImageDrawable(cc.colorDrawable(R.drawable.ic_search,color_id_t,context));
            final EditText editText=(EditText)view.findViewById(R.id.EditSearch);
            editText.setTextColor(ContextCompat.getColor(context,color_id_d));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s=editText.getText().toString();
                    ArrayList<DiaryClass> d=new ArrayList<DiaryClass>();
                    Log.wtf("search"," "+SearchNum);
                    switch (SearchNum){
                        case 0:
                            d=dbHelper.SearchDairy("title",s);
                            break;
                        case 2:
                            String[] str=getResources().getStringArray(R.array.type);
                            Boolean f=false;
                            for(int i=0;i<str.length;i++)
                            {
                                if(s.equals(str[i]))
                                {
                                    d=dbHelper.SearchDairy("type",String.valueOf(i));
                                    f=true;
                                }
                            }
                            if(!f) Toast.makeText(context,"無此搜尋項目",Toast.LENGTH_LONG).show();

                            break;
                        case 1:
                            d=dbHelper.SearchDairy("content",s);
                            break;
                    }
                    ListViewAdapter mAdapter = new ListViewAdapter(context,d);
                    mListView.setAdapter(mAdapter);
                    mAdapter.setMode(Attributes.Mode.Single);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                            //Toast.makeText(context, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context,editText.getText(),Toast.LENGTH_LONG);
                }
            });
            addView(view);

        }
        public void CreateReward(Context context)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.achievement, null);
            ArrayList image_details = getListData();
            final ListView lv1 = (ListView)view.findViewById(R.id.custom_list);
            lv1.setAdapter(new CustomListAdapter(context, image_details));
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = lv1.getItemAtPosition(position);
                    Achievementdata newsData = (Achievementdata) o;
                    //Toast.makeText(MainActivity.this, newsData , Toast.LENGTH_LONG).show();
                }
            });
            addView(view);
        }
        public void CreateSetting(final Context context)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.tree_setting, null);
            TextView t=(TextView)view.findViewById(R.id.setThemeText);
            t.setTextColor(ContextCompat.getColor(context,color_id_d));

            final MaterialTextField materialTextField=(MaterialTextField)view.findViewById(R.id.settingOpen);

            materialTextField.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"?",Toast.LENGTH_LONG).show();
                    PopPassword(context,materialTextField);
                }
            });

            final MaterialSpinner spinner = (MaterialSpinner)view.findViewById(R.id.ThemeSpinner);
            spinner.setItems("Sky",   "Love","Grass");
            spinner.setArrowColor(ContextCompat.getColor(context,color_id_t));
            spinner.setTextColor(ContextCompat.getColor(context,color_id_t));
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    themeNum=position+1;
                    if(position==0)
                    {
                        spinner.setTextColor(myContext.getResources().getColor(R.color.colorPrimary));
                    }
                    else if(position==1)
                    {
                        spinner.setTextColor(myContext.getResources().getColor(R.color.PinkPrimary));
                    }
                    else
                    {
                        spinner.setTextColor(myContext.getResources().getColor(R.color.GrassPrimary));
                    }
                }
            });


            eUser=(EditText)view.findViewById(R.id.userEdit);
            eUser.setText(config.GetUserName());
            ePass=(EditText)view.findViewById(R.id.passEdit);
            ePass.setText(config.GetPassword());

            spinner.setSelectedIndex(themeNum-1);

            addView(view);


        }

        public void Create(Context context)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.test, null);
            addView(view);
        }

        private ArrayList getListData() {
            ArrayList<Achievementdata> results = new ArrayList<Achievementdata>();
            Achievementdata todayArticleNum = new Achievementdata();
            todayArticleNum.setName("今日發文篇數");
            todayArticleNum.setProgress(todayArticleNumToAchieve(dbHelper.GetDiaryNumber(),todayArticleNum));
            results.add(todayArticleNum);
            Achievementdata articleNum = new Achievementdata();
            articleNum.setName("發文總篇數");
            articleNum.setProgress(articleNumToAchieve(dbHelper.GetDiaryNumber(),articleNum));
            results.add(articleNum);
            Achievementdata totalTree = new Achievementdata();
            totalTree.setName("擁有樹木");
            totalTree.setProgress(totalTreeToAchieve(dbHelper.GetTreeNum(),totalTree));
            results.add(totalTree);
            Achievementdata goodMoodArtical = new Achievementdata();
            goodMoodArtical.setName("好心情文章");
            goodMoodArtical.setProgress(goodMoodArticalToAchieve(dbHelper.GetDiaryByMood(0),goodMoodArtical));
            results.add(goodMoodArtical);
            Achievementdata badMoodArtical = new Achievementdata();
            badMoodArtical.setName("壞心情文章");
            badMoodArtical.setProgress(badMoodArticalToAchieve(dbHelper.GetDiaryByMood(1),badMoodArtical));
            results.add(badMoodArtical);
            Achievementdata continueLoginDay = new Achievementdata();
            continueLoginDay.setName("連續登入天數");
            continueLoginDay.setProgress(continueLoginDayToAchieve(1,continueLoginDay));
            results.add(continueLoginDay);
            Achievementdata loginDay = new Achievementdata();
            loginDay.setName("總共登入天數");
            loginDay.setProgress(loginDayToAchieve(1,loginDay));
            results.add(loginDay);
            return results;
        }

        private String articleNumToAchieve(int articleNum , Achievementdata newData)
        {
            int[] goal= {5,20,50,100,300};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>articleNum)
                {
                    String process = articleNum + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "篇";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String todayArticleNumToAchieve(int todayArticleNum , Achievementdata newData)
        {
            int[] goal= {1,2,3,5,10};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>todayArticleNum)
                {
                    String process = todayArticleNum + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "篇";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String totalTreeToAchieve(int totalTree , Achievementdata newData)
        {
            int[] goal= {1,5,10,20,50};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>totalTree)
                {
                    String process = totalTree + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "顆";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String goodMoodArticalToAchieve(int goodMoodArtical , Achievementdata newData)
        {
            int[] goal= {1,5,10,20,50};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>goodMoodArtical)
                {
                    String process = goodMoodArtical + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process =  "達成" + goal[i] + "篇";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String badMoodArticalToAchieve(int badMoodArtical , Achievementdata newData)
        {
            int[] goal= {1,5,10,20,50};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>badMoodArtical)
                {
                    String process = badMoodArtical + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "篇";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String continueLoginDayToAchieve(int continueLoginDay , Achievementdata newData)
        {
            int[] goal= {3,10,20,50,100};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>continueLoginDay)
                {
                    String process = continueLoginDay + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "天";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }

        private String loginDayToAchieve(int loginDay , Achievementdata newData)
        {
            int[] goal= {5,20,50,100,365};
            for(int i = 0 ; i < 5 ; i++ )
            {
                if(goal[i]>loginDay)
                {
                    String process = loginDay + "/" + goal[i];
                    newData.setRate((float)i);
                    return process;
                }
                if(i == 4)
                {
                    String process = "達成" + goal[i] + "天";
                    newData.setRate((float)i+1);
                    return process;
                }
            }
            return "0/0";
        }
    }
}

