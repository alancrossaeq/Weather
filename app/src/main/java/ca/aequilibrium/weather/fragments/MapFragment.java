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
import android.view.MotionEvent;
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
import ca.aequilibrium.weather.viewModels.FavouritesViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_LOCATIONS = 1;

    private MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private FavouritesViewModel mFavouritesViewModel;
    private List<Location> mFavouriteLocations;

    private boolean movedToUser = false;

    private MapListener mListener;
    private Context mContext;

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
        mFavouritesViewModel = ViewModelProviders.of(getActivity()).get(FavouritesViewModel.class);
        mFavouritesViewModel.getFavourites(getContext()).observe(this, favourites -> {
            mFavouriteLocations = favourites;
            populateFavouriteMarkers();
        });
    }

    private void populateFavouriteMarkers() {
        if (googleMap != null && mFavouriteLocations != null) {
            googleMap.clear();
            for (Location favourite : mFavouriteLocations) {
                googleMap.addMarker(new MarkerOptions().position(favourite.getLatLng()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

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
            requestMapPermissions();
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
            mListener = (MapListener) getParentFragment();
        } else if (context instanceof MapListener) {
            mListener = (MapListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapListener");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MapListener {
        void onMapMoved();
        void onMarkerAdded(LatLng latLng);
    }


    @Override
    public void onMapReady(GoogleMap googleMapIn) {
//        initialMapAdjustComplete = false;

        googleMap = googleMapIn;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestMapPermissions();
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng));
                mListener.onMarkerAdded(latLng);
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == REASON_GESTURE) {
                    mListener.onMapMoved();
                }
            }
        });

        populateFavouriteMarkers();

        android.location.Location androidLocation = getLocation();
        makeUseOfNewLocation(androidLocation);
    }

    public android.location.Location getLocation() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestMapPermissions();
            return null;
        }

        android.location.Location androidLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);// LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        return androidLocation;
    }

    public void requestMapPermissions() {
        // Should we show an explanation?
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    onMapReady(googleMap);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    requestMapPermissions();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
        mFavouritesViewModel.loadFavourites(getContext());
    }
}
