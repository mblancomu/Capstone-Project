package com.manuelblanco.capstonestage2.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.db.UserDB;
import com.manuelblanco.capstonestage2.dialogs.LoginDialog;
import com.manuelblanco.capstonestage2.fragments.DetailFragment;
import com.manuelblanco.capstonestage2.fragments.FragmentLauncher;
import com.manuelblanco.capstonestage2.fragments.MainFragment;
import com.manuelblanco.capstonestage2.listeners.LoadDataListener;
import com.manuelblanco.capstonestage2.listeners.VotesListener;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.model.User;
import com.manuelblanco.capstonestage2.network.BackendlessHandler;
import com.manuelblanco.capstonestage2.syncadapter.SyncUtils;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnSuggestionListener,
SearchView.OnQueryTextListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Tracker mTracker;
    private String TAG = MainActivity.class.getSimpleName();
    private String mLoginState;
    private NavigationView navigationView;
    private AppController appController;
    private SearchView searchView;
    private SimpleCursorAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        appController = ((AppController)getApplicationContext());

        initCursorAdapter();
        populateTripsTypesTable();

        Activity activity = this;

        if (activity != null) {
            launchMainFragment(Constants.AdapterTypes.TYPE_TRIPS, Constants.FRAGMENT_MAIN);
        }

        verifyIsLogging();
        SyncUtils.CreateSyncAccount(activity);
    }

    private void verifyIsLogging(){
        int count = SqlHandler.getCount(UserDB.TABLE_USER);
        View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.header_user_title);
        if (count > 0){
           User user = SqlHandler.getUserState();
            appController.setLogin(user.equals(getString(R.string.item_logout)) ? false : true);

            if (user.getState().equals(getString(R.string.item_login))){
                username.setText(user.getUsername());
            }else {
                username.setText(getString(R.string.guest_connection));
            }
        }
    }

    private void populateTripsTypesTable(){
        int count = SqlHandler.getCountTrips();
        int countHere = SqlHandler.getCountTripsTypes();

        if (count > 0 && count != countHere){

            SqlHandler.populateTripsTypesDB();
        }
    }

    private void initCursorAdapter(){
        final String[] from = new String[] {"title"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER){
            @Override
            public void setViewText(TextView v, String text) {
                v.setTextColor(Color.BLACK);
                super.setViewText(v, text);
            }
        };
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        doSearchActivateSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_map:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Map")
                        .build());

               Intent i = new Intent(this,MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                FragmentLauncher.launchMainFragment(this,Constants.AdapterTypes.TYPE_TRIPS, Constants.FRAGMENT_MAIN, true);
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_countries:
                Intent countries = new Intent(this,CountriesActivity.class);
                startActivity(countries);
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_left_out);
                navigationView.setCheckedItem(R.id.nav_countries);
                break;
            case R.id.nav_addtrip:

                if (appController.isLogin()){
                    Intent add = new Intent(this, AddTripActivity.class);
                    startActivity(add);
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_left_out);
                }else{
                    LoginDialog dialog = LoginDialog.newInstance();
                    dialog.show(getSupportFragmentManager(),Constants.DIALOG_LOGIN);
                }
                navigationView.setCheckedItem(R.id.nav_addtrip);
                break;
            case R.id.nav_followus:
                Intent follow = new Intent(this, FollowUsActivity.class);
                startActivity(follow);
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_left_out);
                navigationView.setCheckedItem(R.id.nav_followus);
                break;
            case R.id.nav_favorites:
                Intent favorites = new Intent(this, FavoritesActivity.class);
                startActivity(favorites);
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_left_out);
                navigationView.setCheckedItem(R.id.nav_favorites);
                break;
            case R.id.nav_login:

                if (appController.isLogin()){
                    logout();
                }else{
                    Intent login = new Intent(this, LogInActivity.class);
                    login.putExtra(Constants.KEY_LOGIN_FROM_MENU,true);
                    startActivity(login);
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_left_out);
                }
                navigationView.setCheckedItem(R.id.nav_login);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchMainFragment(Constants.AdapterTypes type, String tag){

        if (!isFinishing()) {

            FragmentLauncher.launchMainFragment(this,type, tag, false);

            if (Utils.isTablet(this)){
                final List<Trip> trips = SqlHandler.getAllTrips();
                final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

                FragmentLauncher.launchDetailFragment(fm,
                        Validator.isIdTripFromListValid(MainActivity.this,trips),Constants.FRAGMENT_DETAIL, true);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        changeLoginStateButton(appController.isLogin());

    }

    private void logout(){

        Backendless.UserService.logout(new AsyncCallback<Void>()
        {
            public void handleResponse( Void response )
            {
                appController.setLogin(false);
                changeLoginStateButton(appController.isLogin());
                User user = new User();
                user.setState(getString(R.string.item_logout));
                user.setUsername(appController.getmUserActive());
                SqlHandler.putUserState(user);

                verifyIsLogging();
            }

            public void handleFault( BackendlessFault fault )
            {

            }
        });
    }

    private void changeLoginStateButton(boolean isLogin){

        if (isLogin){
            mLoginState = getResources().getString(R.string.item_logout);
        }else {
            mLoginState = getResources().getString(R.string.item_login);
        }

        if (navigationView != null){
            Menu menu = navigationView.getMenu();
            MenuItem nav_camara = menu.findItem(R.id.nav_login);
            nav_camara.setTitle(mLoginState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {

        int id = (int) searchView.getSuggestionsAdapter().
                getItemId(position);

        String idTrip = SqlHandler.getIdTrip(id+1);

        if(idTrip != null && !idTrip.isEmpty() && !idTrip.equals(""))
        {
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
            hideSoftKeyboard();

            if (!Utils.isTablet(this)) {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(Constants.KEY_IDTRIP, idTrip);
                startActivity(intent);
            }else{
                FragmentLauncher.launchDetailFragment(getSupportFragmentManager(),
                        Validator.isIdTripValid(MainActivity.this,idTrip),Constants.FRAGMENT_DETAIL, true);
            }
        }else {
            UtilsView.showSnackBar((CoordinatorLayout)findViewById(R.id.main_coordinator),
                    getString(R.string.search_empty), getResources().getColor(R.color.colorAccent));
        }

        return true;
    }

    private void doSearchActivateSearch(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnSuggestionListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Search")
                .build());

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchTrips(newText);
        return false;
    }

    private void searchTrips(String query){

        ArrayList<String> trips = SqlHandler.getTripsName();

        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "title" });
        for (int i=0; i<trips.size(); i++) {
            if (trips.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, trips.get(i).toString()});
        }
        mAdapter.changeCursor(c);
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.home_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        navigationView.setCheckedItem(R.id.nav_home);
    }
}