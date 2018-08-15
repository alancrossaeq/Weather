package ca.aequilibrium.weather.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.utils.PreferencesHelper;
import ca.aequilibrium.weather.viewModels.FavouritesViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_LOCATIONS = 1;

    private MapView mvMap;

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private FavouritesViewModel favouritesViewModel;
    private List<Location> favouriteLocations;
    private boolean movedToUser = false;
    private MapListener listener;
    private Context context;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouritesViewModel = ViewModelProviders.of(getActivity()).get(FavouritesViewModel.class);
        favouritesViewModel.getFavourites(getContext()).observe(this, favourites -> {
            favouriteLocations = favourites;
            populateFavouriteMarkers();
        });
    }

    private void populateFavouriteMarkers() {
        if (googleMap != null && favouriteLocations != null) {
            googleMap.clear();
            for (Location favourite : favouriteLocations) {
                googleMap.addMarker(new MarkerOptions().position(favourite.getLatLng()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mvMap = view.findViewById(R.id.mapView);
        mvMap.onCreate(savedInstanceState);

        mvMap.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mvMap.getMapAsync(this);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location androidLocation) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(androidLocation);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!PreferencesHelper.readLocationPermissionAlreadyRequested(context)) {
                requestMapPermissions();
            }
        } else {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MapListener) {
            listener = (MapListener) getParentFragment();
        } else if (context instanceof MapListener) {
            listener = (MapListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface MapListener {
        void onMapMoved();
        void onMarkerAdded(LatLng latLng);
    }


    @Override
    public void onMapReady(GoogleMap googleMapIn) {

        googleMap = googleMapIn;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng));
                listener.onMarkerAdded(latLng);
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == REASON_GESTURE) {
                    listener.onMapMoved();
                }
            }
        });

        populateFavouriteMarkers();

        android.location.Location androidLocation = getLocation();
        makeUseOfNewLocation(androidLocation);
    }

    public android.location.Location getLocation() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        android.location.Location androidLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return androidLocation;
    }

    public void requestMapPermissions() {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATIONS);
            PreferencesHelper.setLocationPermissionAlreadyRequested(context, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1) {
                    // permission was granted, do the location related stuff!
                    onMapReady(googleMap);
                } else {

                    // permission denied, boo!
//                    requestMapPermissions();
                }
                return;
            }
        }
    }

    private void makeUseOfNewLocation(android.location.Location location) {
        if (location != null && !movedToUser) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (true) {
                if (true) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                } else {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
            }
            movedToUser = true;
        }
    }

    public void refresh() {
        favouritesViewModel.loadFavourites(getContext());
    }
}
