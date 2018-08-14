package ca.aequilibrium.weather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.models.Location;

public class CityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Location mLocation;

    private TextView tvMain;

//    private OnFragmentInteractionListener mListener;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(Location location) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        tvMain = view.findViewById(R.id.tv_main);
        tvMain.setText(mLocation.getName());

        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
