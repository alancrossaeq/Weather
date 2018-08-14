package ca.aequilibrium.weather;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapAndFavouritesFragment extends Fragment implements FavouritesFragment.FavouritesListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FavouritesFragment favouritesFragment;
    private View favouritesContainer;
    private MapFragment mapFragment;

    private MapAndFavouritesListener mListener;

    public MapAndFavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapAndFavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapAndFavouritesFragment newInstance(String param1, String param2) {
        MapAndFavouritesFragment fragment = new MapAndFavouritesFragment();
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

        mapFragment = MapFragment.newInstance(null, null);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit();

        favouritesFragment = FavouritesFragment.newInstance(null, null);
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

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
    }

    public void showFavourites() {
//        favouritesFragment.show();
        favouritesContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFavouritestHidden() {
        mListener.onFavouritesHidden();
    }
}
