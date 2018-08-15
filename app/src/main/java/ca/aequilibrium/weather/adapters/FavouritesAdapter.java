package ca.aequilibrium.weather.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.diffCallbacks.LocationDiffCallback;
import ca.aequilibrium.weather.models.Location;

public class FavouritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Location> items;
    private FavouritesAdapterListener listener;

    public interface FavouritesAdapterListener {
        void onItemSelected(Location location, int position);
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        public CardView cvRootView;
        public TextView tvLocation;
        public TextView tvCountry;
        public FavouriteViewHolder(CardView v) {
            super(v);
            cvRootView = v;
            tvLocation = v.findViewById(R.id.tv_location);
            tvCountry = v.findViewById(R.id.tv_country);
        }
    }

    public FavouritesAdapter(List<Location> items, FavouritesAdapterListener listener) {
        this.items = items == null ? new ArrayList<>() : items;
        this.listener = listener;
    }

    public void swapItems(List<Location> newItems) {

        newItems = newItems == null ? new ArrayList<>() : newItems;

        // compute diffs
        final LocationDiffCallback diffCallback = new LocationDiffCallback(items, newItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear contacts and add
        items.clear();
        items.addAll(newItems);

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);

        FavouriteViewHolder vh = new FavouriteViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FavouriteViewHolder favouriteViewHolder = (FavouriteViewHolder) holder;
        favouriteViewHolder.tvLocation.setText(items.get(position).getName());
        favouriteViewHolder.tvCountry.setText(items.get(position).getCountry());
        favouriteViewHolder.cvRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(items.get(favouriteViewHolder.getAdapterPosition()), favouriteViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
