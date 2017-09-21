package com.example.nako.thetreediary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

public class AddNewTree extends AppCompatActivity {
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tree);
        toolbar = (Toolbar) findViewById(R.id.toolbar_treeadd);
        setSupportActionBar(toolbar);
        InitLeftMenu();
    }
    private void InitLeftMenu()
    {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.profile_tree)
                .addProfiles(
                        new ProfileDrawerItem().withName("Nako").withIcon(getResources().getDrawable(R.drawable.profile_icon))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("我的小樹").withIcon(R.drawable.ic_topic_memo);;;
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("種植小樹").withIcon(R.drawable.ic_topic_memo);;;
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("搜尋").withIcon(R.drawable.ic_topic_memo);;
        SecondaryDrawerItem item4 =new SecondaryDrawerItem().withIdentifier(4).withName("成就").withIcon(R.drawable.ic_topic_diary);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("設定").withIcon(R.drawable.ic_settings_black_24dp);
        Drawer result = new DrawerBuilder()
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
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();


        result.addStickyFooterItem(new PrimaryDrawerItem().withName("關於").withIdentifier(6));
        result.setSelection(2);

    }
}
