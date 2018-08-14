package ca.aequilibrium.weather;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mDataset;
    private FavouritesAdapterListener mListener;

    public interface FavouritesAdapterListener {
        void onItemSelected(String item, int position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mRootView;
        public TextView mTVLocation;
        public ImageView mIVWeatherIcon;
        public FavouriteViewHolder(CardView v) {
            super(v);
            mRootView = v;
            mIVWeatherIcon = v.findViewById(R.id.iv_weather_icon);
            mTVLocation = v.findViewById(R.id.tv_location);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FavouritesAdapter(List<String> data, FavouritesAdapterListener listener) {
        mDataset = data;
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);

        FavouriteViewHolder vh = new FavouriteViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FavouriteViewHolder favouriteViewHolder = (FavouriteViewHolder) holder;
        favouriteViewHolder.mIVWeatherIcon.setImageURI(Uri.parse("http://openweathermap.org/img/w/04n.png"));
        favouriteViewHolder.mTVLocation.setText(mDataset.get(position));
        favouriteViewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(mDataset.get(favouriteViewHolder.getAdapterPosition()), favouriteViewHolder.getAdapterPosition());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
