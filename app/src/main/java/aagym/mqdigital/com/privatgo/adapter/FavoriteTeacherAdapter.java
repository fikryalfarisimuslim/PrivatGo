package aagym.mqdigital.com.privatgo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import aagym.mqdigital.com.privatgo.R;
import aagym.mqdigital.com.privatgo.entity.FavoriteTeacher;

public class FavoriteTeacherAdapter extends RecyclerView.Adapter<FavoriteTeacherAdapter.RestaurantViewHolder> {
    private ArrayList<FavoriteTeacher> mFavoriteTeachers = new ArrayList<>();
    private Context mContext;

    public FavoriteTeacherAdapter(Context context, ArrayList<FavoriteTeacher> favoriteTeachers) {
        mContext = context;
        mFavoriteTeachers = favoriteTeachers;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_teacher_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        public RestaurantViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
        }
    }
}
