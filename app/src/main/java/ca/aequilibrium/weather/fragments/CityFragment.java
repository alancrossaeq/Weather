package ca.aequilibrium.weather.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.adapters.ForecastAdapter;
import ca.aequilibrium.weather.asyncTasks.DeleteFavouriteLocationTask;
import ca.aequilibrium.weather.asyncTasks.SimpleCallback;
import ca.aequilibrium.weather.enums.UnitType;
import ca.aequilibrium.weather.models.CityView;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.network.NetworkConstants;
import ca.aequilibrium.weather.utils.ImageLoader;
import ca.aequilibrium.weather.utils.PreferencesHelper;
import ca.aequilibrium.weather.utils.StringUtils;
import ca.aequilibrium.weather.viewModels.CityViewModel;

public class CityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Location mLocation;

    private ImageView ivHeaderImage;
    private TextView tvHeaderTemp;
    private TextView tvHeaderHumidity;
    private TextView tvHeaderRain;
    private TextView tvHeaderWinds;
    private TextView tvHeaderDescription;

    private RecyclerView recyclerView;
    private ForecastAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private CityViewModel cityViewModel;
    private CityListener listener;

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
        cityViewModel = ViewModelProviders.of(getActivity()).get(CityViewModel.class);
        cityViewModel.getCityView(getContext(), mLocation).observe(this, cityView -> {
            updateUIWithData(cityView);
        });
    }

    private void updateUIWithData(CityView cityView) {
        UnitType unitType = PreferencesHelper.readUnitSystemType(getContext());
        String speedMetric = unitType == UnitType.METRIC ? getString(R.string.unit_kph) : getString(R.string.unit_mph);
        String tempMetric = unitType == UnitType.METRIC ? getString(R.string.unit_celsius) : getString(R.string.unit_fahrenheit);
        String volumeMetric = unitType == UnitType.METRIC ? getString(R.string.unit_ml) : getString(R.string.unit_ounces);

        if (cityView.getForecast() != null) {
            ImageLoader.getInstance(getContext()).loadImageFromUrl(ivHeaderImage, NetworkConstants.openWeatherImgUrl + cityView.getForecast().getWeather().get(0).getIcon() + ".png");
            tvHeaderTemp.setText(cityView.getForecast().getMain().getTemp() + tempMetric);
            tvHeaderHumidity.setText(getString(R.string.humidity) + ": " + cityView.getForecast().getMain().getHumidity() + "%");
            tvHeaderRain.setText(getString(R.string.rainfall) + ": " + ((cityView.getForecast().getRain() == null || cityView.getForecast().getRain().getThreeHourVolume() == null) ? "0" : cityView.getForecast().getRain().getThreeHourVolume()) + volumeMetric + " (3hr)");
            tvHeaderWinds.setText(getString(R.string.winds) + ": " + cityView.getForecast().getWind().getSpeed() + speedMetric);
            tvHeaderDescription.setText(StringUtils.capitalizeFirstLetter(cityView.getForecast().getWeather().get(0).getDescription()));
        }

        if (cityView.getFiveDayForecast() != null) {
            adapter.swapItems(cityView.getFiveDayForecast().getList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        ivHeaderImage = view.findViewById(R.id.iv_header_image);
        tvHeaderTemp = view.findViewById(R.id.tv_header_temp);
        tvHeaderHumidity = view.findViewById(R.id.tv_header_humidity);
        tvHeaderRain = view.findViewById(R.id.tv_header_rain);
        tvHeaderWinds = view.findViewById(R.id.tv_header_winds);
        tvHeaderDescription = view.findViewById(R.id.tv_header_description);

        Toolbar cityToolbar = view.findViewById(R.id.city_toolbar);
        cityToolbar.setTitle(mLocation.getName());

        recyclerView = view.findViewById(R.id.recycler_view_fivedayforecast);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ForecastAdapter(getActivity(),null);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof CityListener) {
            listener = (CityListener) getParentFragment();
        } else if (context instanceof CityListener) {
            listener = (CityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CityListener {
        void onFavouriteDeleted();
    }

    public void deleteFavourite() {
        new DeleteFavouriteLocationTask(getContext(), new SimpleCallback() {
            @Override
            public void onFinished(Object result) {
                listener.onFavouriteDeleted();
            }
        }).execute(mLocation);
    }
}
