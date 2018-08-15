package ca.aequilibrium.weather.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.enums.UnitType;
import ca.aequilibrium.weather.utils.PreferencesHelper;

public class SettingsFragment extends Fragment {

    private TextView tvResetFavourites;
    private TextView tvUnitSystem;
    private UnitType mSelectedType;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvResetFavourites = view.findViewById(R.id.tv_reset_favourites);
        tvResetFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFavouritesTapped();
            }
        });
        tvUnitSystem = view.findViewById(R.id.tv_units);
        tvUnitSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnitSystemTapped();
            }
        });

        return view;
    }

    private void resetFavouritesTapped() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.reset_favourites_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteFavouriteLocationsTask(getContext()).execute();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and show it
        builder.create().show();
    }

    private void setUnitSystemTapped() {
        mSelectedType = PreferencesHelper.readUnitSystemType(getContext());
        CharSequence[] choices = {getContext().getString(R.string.metric), getContext().getString(R.string.imperial)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.settings_units)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(choices, mSelectedType.getValue(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedType = UnitType.valueOf(which);
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PreferencesHelper.setUnitSystemType(getContext(), mSelectedType);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create().show();
    }

    private static class DeleteFavouriteLocationsTask extends AsyncTask<String, Void, String> {

        private Context appContext;

        DeleteFavouriteLocationsTask(Context appContextIn) {
            appContext = appContextIn;
        }

        @Override
        protected String doInBackground(String... params) {

            AppDatabase.getAppDatabase(appContext).locationDao().deleteAll();

            return "task finished";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
