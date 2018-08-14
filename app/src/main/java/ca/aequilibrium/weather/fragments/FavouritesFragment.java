package ca.aequilibrium.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.weather.adapters.FavouritesAdapter;
import ca.aequilibrium.weather.R;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.FavouritesAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean viewHidden = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FavouritesListener mListener;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favourites);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        List<String> data = new ArrayList<>();
        data.add("string 1");
        data.add("string 2");
        data.add("string 3");
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
        void onFavouriteSelected(String favourite, int position);
    }

    @Override
    public void onItemSelected(String item, int position) {
//        CityFragment cityFragment = CityFragment.newInstance(item, null);
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .add(cityFragment, "cityFragment")
//                // Add this transaction to the back stack
//                .addToBackStack(null)
//                .commit();
        mListener.onFavouriteSelected(item, position);
    }
}
