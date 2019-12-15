package com.duynm.qlbanhang.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.SimplePagerAdapter;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.ui.home.listorder.ListOrderFragment;
import com.duynm.qlbanhang.ui.home.listproduct.ListProductFragment;
import com.duynm.qlbanhang.ui.home.more.MoreFragment;
import com.duynm.qlbanhang.ui.home.overview.OverviewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    //Fragments
    private OverviewFragment overviewFragment;
    private ListOrderFragment listOrderFragment;
    private ListProductFragment listProductFragment;
    private MoreFragment moreFragment;

    //View components
    private ViewPager vpMain;
    private TextView toolbarTitle;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        vpMain = findViewById(R.id.vp_main);
        toolbarTitle = findViewById(R.id.tv_toolbar_title);
        navigationView = findViewById(R.id.navigation);
    }

    private void initUI() {
        setupNavigation();
        setupViewPager();
    }

    private void setupNavigation() {
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setupViewPager() {
        SimplePagerAdapter pagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());

        overviewFragment = new OverviewFragment();
        listOrderFragment = new ListOrderFragment();
        listProductFragment = new ListProductFragment();
        moreFragment = new MoreFragment();

        pagerAdapter.addFragment(overviewFragment, getString(R.string.overview));
        pagerAdapter.addFragment(listOrderFragment, getString(R.string.list_order));
        pagerAdapter.addFragment(listProductFragment, getString(R.string.list_product));
        pagerAdapter.addFragment(moreFragment, getString(R.string.more));

        vpMain.setAdapter(pagerAdapter);
        vpMain.setOffscreenPageLimit(pagerAdapter.getCount());
        vpMain.setCurrentItem(0);

        vpMain.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_overview:
                vpMain.setCurrentItem(0);
                return true;
            case R.id.menu_order:
                vpMain.setCurrentItem(1);
                return true;
            case R.id.menu_product:
                vpMain.setCurrentItem(2);
                return true;
            case R.id.menu_more:
                vpMain.setCurrentItem(3);
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                navigationView.setSelectedItemId(R.id.menu_overview);
                break;
            case 1:
                navigationView.setSelectedItemId(R.id.menu_order);
                break;
            case 2:
                navigationView.setSelectedItemId(R.id.menu_product);
                break;
            case 3:
                navigationView.setSelectedItemId(R.id.menu_more);
                break;
        }
        SimplePagerAdapter adapter = (SimplePagerAdapter) vpMain.getAdapter();
        if (adapter != null)
            toolbarTitle.setText(adapter.getPageTitle(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
