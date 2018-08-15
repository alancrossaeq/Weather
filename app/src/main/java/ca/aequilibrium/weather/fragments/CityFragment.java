package ca.aequilibrium.weather.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.models.CityView;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.viewModels.CityViewModel;
import ca.aequilibrium.weather.viewModels.FavouritesViewModel;

public class CityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Location mLocation;

    private ImageView ivHeaderImage;
    private TextView tvHeaderTemp;
    private TextView tvHeaderHumidity;
    private TextView tvHeaderRain;
    private TextView tvHeaderWinds;

    private CityViewModel mCityViewModel;

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
        mCityViewModel = ViewModelProviders.of(getActivity()).get(CityViewModel.class);
        mCityViewModel.getCityView(getContext(), mLocation).observe(this, cityView -> {
            updateUIWithData(cityView);
        });
    }

    private void updateUIWithData(CityView cityView) {
//        ivHeaderImage
        if (cityView.getForecast() != null) {
            tvHeaderTemp.setText(cityView.getForecast().getMain().getTemp() + "°C");
            tvHeaderHumidity.setText(getString(R.string.humidity) + ": " + cityView.getForecast().getMain().getHumidity() + "%");
            tvHeaderRain.setText(getString(R.string.rainfall) + ": " + (cityView.getForecast().getRain() == null ? "0" : cityView.getForecast().getRain().getThreeHourVolume()) + "ml (3hr)");
            tvHeaderWinds.setText(getString(R.string.winds) + ": " + cityView.getForecast().getWind().getSpeed() + "mph");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        ivHeaderImage = view.findViewById(R.id.iv_header_image);
        tvHeaderTemp = view.findViewById(R.id.tv_header_temp);
        tvHeaderHumidity = view.findViewById(R.id.tv_header_humidity);
        tvHeaderRain = view.findViewById(R.id.tv_header_rain);
        tvHeaderWinds = view.findViewById(R.id.tv_header_winds);

        Toolbar cityToolbar = view.findViewById(R.id.city_toolbar);
        cityToolbar.setTitle(mLocation.getName());

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
