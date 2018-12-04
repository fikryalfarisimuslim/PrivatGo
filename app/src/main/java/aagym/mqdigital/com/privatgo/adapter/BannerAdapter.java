package aagym.mqdigital.com.privatgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

import aagym.mqdigital.com.privatgo.R;
import aagym.mqdigital.com.privatgo.entity.Banner;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<Banner> banner;

    public BannerAdapter(List<Banner> banner) {
        this.banner = banner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.banner_layout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource(banner.get(position).getResource());
    }

    @Override
    public int getItemCount() {
        return banner.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.banner);
        }
    }
}