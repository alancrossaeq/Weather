package ca.aequilibrium.weather.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.models.Location;

public class MapAndFavouritesFragment extends Fragment implements FavouritesFragment.FavouritesListener, MapFragment.MapListener {

    private View vFavouritesContainer;

    private FavouritesFragment favouritesFragment;
    private MapFragment mapFragment;
    private MapAndFavouritesListener listener;
    private boolean favouritesHidden = false;

    public MapAndFavouritesFragment() {
        // Required empty public constructor
    }

    public static MapAndFavouritesFragment newInstance(String param1, String param2) {
        MapAndFavouritesFragment fragment = new MapAndFavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapFragment = MapFragment.newInstance();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit();

        favouritesFragment = FavouritesFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.favourites_container, favouritesFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_and_favourites, container, false);

        vFavouritesContainer = view.findViewById(R.id.favourites_container);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vFavouritesContainer.setElevation(((AppCompatActivity) getActivity()).getSupportActionBar().getElevation());
        }

        favouritesHidden = false;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MapAndFavouritesListener) {
            listener = (MapAndFavouritesListener) getParentFragment();
        } else if (context instanceof MapAndFavouritesListener) {
            listener = (MapAndFavouritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapAndFavouritesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface MapAndFavouritesListener {
        void onFavouritesHidden();
        void onFavouritesShown();
        void onFavouriteSelected(Location favourite, int position);
    }

    public void showFavourites() {
        favouritesHidden = false;
        vFavouritesContainer.setVisibility(View.VISIBLE);
        listener.onFavouritesShown();
    }

    @Override
    public void onFavouriteSelected(Location favourite, int position) {
        listener.onFavouriteSelected(favourite, position);
    }

    @Override
    public void onMapMoved() {
        favouritesHidden = true;
        vFavouritesContainer.setVisibility(View.INVISIBLE);
        listener.onFavouritesHidden();
    }

    @Override
    public void onMarkerAdded(LatLng latLng) {
        favouritesFragment.addFavouriteLocation(latLng);
        showFavourites();
    }

    public void refresh() {
        mapFragment.refresh();
        favouritesFragment.refresh();
    }

    public boolean shouldShowBookmarksToolbarButton() {
        return favouritesHidden && favouritesFragment.favouritesCount() > 0;
    }
}
