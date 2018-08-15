package ca.aequilibrium.weather.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import ca.aequilibrium.weather.adapters.FavouritesAdapter;
import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.asyncTasks.SaveFavouriteLocationTask;
import ca.aequilibrium.weather.asyncTasks.SimpleCallback;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.viewModels.FavouritesViewModel;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.FavouritesAdapterListener {

    private RecyclerView recyclerView;
    private FavouritesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FavouritesViewModel favouritesViewModel;
    private FavouritesListener listener;

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
        favouritesViewModel = ViewModelProviders.of(getActivity()).get(FavouritesViewModel.class);
        favouritesViewModel.getFavourites(getContext()).observe(this, favourites -> {
            boolean shouldScroll = favourites != null && favourites.size() > adapter.getItemCount();
            // Update the UI.
            adapter.swapItems(favourites);
            if (shouldScroll) {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favourites);

        // use a horizontal layout manager
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FavouritesAdapter(null, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof FavouritesListener) {
            listener = (FavouritesListener) getParentFragment();
        } else if (context instanceof FavouritesListener) {
            listener = (FavouritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FavouritesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface FavouritesListener {
        void onFavouriteSelected(Location favourite, int position);
    }

    @Override
    public void onItemSelected(Location location, int position) {
        listener.onFavouriteSelected(location, position);
    }

    public void addFavouriteLocation(LatLng latLng) {
        new SaveFavouriteLocationTask(getContext(), new SimpleCallback() {
            @Override
            public void onFinished(Object result) {
                favouritesViewModel.loadFavourites(getContext());
            }
        }).execute(latLng);
    }

    public void refresh() {
        favouritesViewModel.loadFavourites(getContext());
    }

    public int favouritesCount() {
        if (adapter != null) {
            return adapter.getItemCount();
        } else {
            return 0;
        }
    }
}
