package se.mah.af6589.personalfinanceapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentActivity {

    private Controller controller;
    private FloatingActionButton fab;
    private Listener listener = new Listener();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tvNavName, tvNavLastName;
    private ImageView ivEdit;
    public static final int requestLoginCode = 100;

    @Override
    public void setFragment(Fragment fragment, boolean backstack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment);
        if (backstack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void restartFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public DataFragment addDataFragment() {
        DataFragment dataFragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        dataFragment = (DataFragment) fragmentManager.findFragmentByTag("data");
        if (dataFragment == null) {
            dataFragment = new DataFragment();
            fragmentManager.beginTransaction().add(dataFragment, "data").commit();
        }
        return dataFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        initializeFab();
        initializeNavDrawer();

        controller = new Controller(this);
        if (!controller.isLoggedIn()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, requestLoginCode);
        }
        controller.openCurrentFragment();
        controller.updateUserInformation();
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
    }

    private void initializeNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);

        tvNavName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvNavName);
        tvNavLastName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvNavLastName);
        ivEdit = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_edit_user);
        ivEdit.setOnClickListener(listener);
    }

    public void setNavName(String name) {
        tvNavName.setText(name);
    }

    public void setNavLastName(String name) {
        tvNavLastName.setText(name);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            controller.removeRetainedFragments(getFragmentManager());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestLoginCode) {
            if (resultCode == RESULT_OK) {
                controller.userLoggedIn(data);
            }
            if (resultCode != RESULT_OK) {
                controller.failedActivityResult();
            }
        }

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.v("MAIN_RES", contents);
                controller.barcodeScanned(contents);
            }
            if(resultCode == RESULT_CANCELED){
                Log.v("MAIN", "cancel");
                Toast.makeText(this, "Barcode scanning canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Controller getController() {
        return controller;
    }

    private class Listener implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                controller.setHomeFragment();
            } else if (id == R.id.nav_expenditures) {
                controller.navExpenditureClicked();
            } else if (id == R.id.nav_incomes) {
                controller.navIncomeClicked();
            } else if (id == R.id.nav_barcode) {
                controller.navBarcodeClicked();
            } else if (id == R.id.nav_saved_barcodes) {
                controller.navSavedBarcodesClicked();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public void onClick(View view) {
            if (view == fab) {
                controller.fabClicked();
            }
            if (view == ivEdit) {
                controller.editUserClicked();
            }
        }
    }
}
