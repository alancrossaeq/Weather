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

    private FavouritesFragment favouritesFragment;
    private View favouritesContainer;
    private MapFragment mapFragment;
    private boolean mFavouritesHidden = false;

    private MapAndFavouritesListener mListener;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_and_favourites, container, false);

        favouritesContainer = view.findViewById(R.id.favourites_container);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            favouritesContainer.setElevation(((AppCompatActivity) getActivity()).getSupportActionBar().getElevation());
        }

        mFavouritesHidden = false;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MapAndFavouritesListener) {
            mListener = (MapAndFavouritesListener) getParentFragment();
        } else if (context instanceof MapAndFavouritesListener) {
            mListener = (MapAndFavouritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapAndFavouritesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MapAndFavouritesListener {
        void onFavouritesHidden();
        void onFavouriteSelected(Location favourite, int position);
    }

    public void showFavourites() {
        mFavouritesHidden = false;
        favouritesContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFavouriteSelected(Location favourite, int position) {
        mListener.onFavouriteSelected(favourite, position);
    }

    @Override
    public void onMapMoved() {
        mFavouritesHidden = true;
//        favouritesContainer.setVisibility(View.GONE);
        favouritesContainer.setVisibility(View.INVISIBLE);
        mListener.onFavouritesHidden();
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
        return mFavouritesHidden;
    }
}
