package ca.aequilibrium.weather.activities;

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

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.fragments.CityFragment;
import ca.aequilibrium.weather.fragments.HelpFragment;
import ca.aequilibrium.weather.fragments.MapAndFavouritesFragment;
import ca.aequilibrium.weather.fragments.SettingsFragment;
import ca.aequilibrium.weather.models.Location;

public class MainActivity extends AppCompatActivity implements MapAndFavouritesFragment.MapAndFavouritesListener, CityFragment.CityListener {

    private DrawerLayout drawerLayout;
    private MenuItem currentSelectedMenuItem;

    private MapAndFavouritesFragment mapAndFavouritesFragment;
    private CityFragment cityFragment;
    private Fragment currentNavFragment;

    private boolean showingHamburger = false;
    private boolean showingCityFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        showHamburger();

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        selectDrawerItem(firstItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem favouritesButton = menu.findItem(R.id.action_favourites);
        favouritesButton.setVisible(currentNavFragment instanceof MapAndFavouritesFragment && mapAndFavouritesFragment.shouldShowBookmarksToolbarButton());

        MenuItem deleteOption = menu.findItem(R.id.action_delete);
        deleteOption.setVisible(showingCityFragment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                cityFragment.deleteFavourite();
                return true;
            case android.R.id.home:
                if (showingHamburger) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    getSupportFragmentManager().popBackStack();
                    if (showingCityFragment) {
                        showingCityFragment = false;
                    }
                    showHamburger();
                    invalidateOptionsMenu();
                }
                return true;
            case R.id.action_favourites:
                mapAndFavouritesFragment.showFavourites();
                invalidateOptionsMenu();
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
        Fragment fragment;
        if (menuItem == currentSelectedMenuItem) {
            return;
        }
        currentSelectedMenuItem = menuItem;
        switch(menuItem.getItemId()) {
            case R.id.nav_map:
                mapAndFavouritesFragment = mapAndFavouritesFragment == null ? MapAndFavouritesFragment.newInstance(null, null) : mapAndFavouritesFragment;
                currentNavFragment = fragment = mapAndFavouritesFragment;
                break;
            case R.id.nav_help:
                currentNavFragment = fragment = HelpFragment.newInstance();
                break;
            default:
                currentNavFragment = fragment = SettingsFragment.newInstance();
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
        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    @Override
    public void onFavouritesHidden() {
        invalidateOptionsMenu();
    }

    @Override
    public void onFavouritesShown() {
        invalidateOptionsMenu();
    }

    @Override
    public void onFavouriteSelected(Location favourite, int position) {
        cityFragment = CityFragment.newInstance(favourite);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent, cityFragment).addToBackStack(null).commit();

        showBackButton();

        showingCityFragment = true;
        invalidateOptionsMenu();
    }

    @Override
    public void onFavouriteDeleted() {
        mapAndFavouritesFragment.refresh();
        getSupportFragmentManager().popBackStack();
        showingCityFragment = false;
        showHamburger();
        invalidateOptionsMenu();
    }
}
