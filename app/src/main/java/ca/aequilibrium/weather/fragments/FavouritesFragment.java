package ca.aequilibrium.weather.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.adapters.FavouritesAdapter;
import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.utils.LocationUtils;
import ca.aequilibrium.weather.viewModels.FavouritesViewModel;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.FavouritesAdapterListener {

    private boolean viewHidden = false;

    private RecyclerView mRecyclerView;
    private FavouritesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FavouritesViewModel mFavouritesViewModel;

    private FavouritesListener mListener;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavouritesViewModel = ViewModelProviders.of(getActivity()).get(FavouritesViewModel.class);
        mFavouritesViewModel.getFavourites(getContext()).observe(this, favourites -> {
            // Update the UI.
            mAdapter.swapItems(favourites);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favourites);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        List<Location> data = new ArrayList<>();
        Location location = new Location();
        location.setName("123");
//        data.add(location);
//        data.add("string 2");
//        data.add("string 3");
        mAdapter = new FavouritesAdapter(data, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setTag(mRecyclerView.getVisibility());
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!viewHidden && mRecyclerView.getVisibility() == View.INVISIBLE || mRecyclerView.getVisibility() == View.GONE || mRecyclerView.getHeight() == 0) {
                    mListener.onFavouritestHidden();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof FavouritesListener) {
            mListener = (FavouritesListener) getParentFragment();
        } else if (context instanceof FavouritesListener) {
            mListener = (FavouritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FavouritesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface FavouritesListener {
        void onFavouritestHidden();
        void onFavouriteSelected(Location favourite, int position);
    }

    @Override
    public void onItemSelected(Location location, int position) {
//        CityFragment cityFragment = CityFragment.newInstance(item, null);
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .add(cityFragment, "cityFragment")
//                // Add this transaction to the back stack
//                .addToBackStack(null)
//                .commit();
        mListener.onFavouriteSelected(location, position);
    }

    public void addFavouriteLocation(LatLng latLng) {
        new SaveFavouriteLocationTask(getContext(), this).execute(latLng);
    }

    private static class SaveFavouriteLocationTask extends AsyncTask<LatLng, Void, String> {

//        private LatLng latLng;
        private Context appContext;
        private FavouritesFragment favouritesFragment;

        // only retain a weak reference to the activity
        SaveFavouriteLocationTask(Context appContextIn, FavouritesFragment favouritesFragmentIn) {
            appContext = appContextIn;
            favouritesFragment = favouritesFragmentIn;
        }

        @Override
        protected String doInBackground(LatLng... params) {

            LatLng latLng = params[0];

            String locationName = LocationUtils.cityNameForCoordinates(appContext, latLng);
            Location location = new Location();
            location.setLatLng(latLng);
            location.setName(locationName);
            AppDatabase.getAppDatabase(appContext).locationDao().insertAll(location);

            return "task finished";
        }

        @Override
        protected void onPostExecute(String result) {
            if (favouritesFragment != null) {
//                favouritesFragment.mFavouritesViewModel.getFavourites(appContext).observe(favouritesFragment, favourites -> {
////                    // Update the UI.
////                    favouritesFragment.mAdapter.swapItems(favourites);
////                });
                favouritesFragment.mFavouritesViewModel.loadFavourites(appContext);
            }
        }
    }
}
