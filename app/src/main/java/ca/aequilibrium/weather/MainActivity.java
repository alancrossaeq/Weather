package ca.aequilibrium.weather;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MapAndFavouritesFragment.MapAndFavouritesListener {

    private DrawerLayout mDrawerLayout;
    private boolean favouritesHidden = false;
    private boolean showingMap = true;
    private MenuItem currentSelectedMenuItem;

    private MapAndFavouritesFragment mapAndFavouritesFragment;
    private Fragment currentFragment;

    private boolean showingHamburger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        showHamburger();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        selectDrawerItem(firstItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem favouritesButton = menu.findItem(R.id.action_favourites);
        favouritesButton.setVisible(!favouritesHidden);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                if (showingHamburger) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    getSupportFragmentManager().popBackStack();
                    showHamburger();
                }
                return true;
            case R.id.action_favourites:
                mapAndFavouritesFragment.showFavourites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHamburger() {
        showingHamburger = true;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void showBackButton() {
        showingHamburger = false;
        getSupportActionBar().setHomeAsUpIndicator(0);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment;
        if (menuItem == currentSelectedMenuItem) {
            return;
        }
        currentSelectedMenuItem = menuItem;
        switch(menuItem.getItemId()) {
            case R.id.nav_map:
                showingMap = true;
                mapAndFavouritesFragment = mapAndFavouritesFragment == null ? MapAndFavouritesFragment.newInstance(null, null) : mapAndFavouritesFragment;
                fragment = mapAndFavouritesFragment;
                break;
            case R.id.nav_help:
                fragment = HelpFragment.newInstance(null, null);
                break;
            default:
                fragment = SettingsFragment.newInstance(null, null);
                break;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onFavouritesHidden() {
        favouritesHidden = true;
        invalidateOptionsMenu();
    }

    @Override
    public void onFavouriteSelected(String favourite, int position) {
        CityFragment cityFragment = CityFragment.newInstance(favourite, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent, cityFragment).addToBackStack(null).commit();

        showBackButton();
    }
}
