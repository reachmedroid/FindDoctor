package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Models.DoctorsFeed;
import com.example.myapplication.R;
import com.example.myapplication.Utils.CommonUtils;

import java.net.URLEncoder;
import java.util.List;

public class SearchResultViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<DoctorsFeed> mItemList;
    private Context context;
    public SearchResultViewAdapter(Context context, List<DoctorsFeed> itemList) {
        this.mItemList = itemList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_layout, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progress, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        TextView tvDoctorAddress;
        ImageView imgProfilePicture;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorAddress= itemView.findViewById(R.id.tvDoctorAddress);
            imgProfilePicture= itemView.findViewById(R.id.profile_image);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }



    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        viewHolder.tvItem.setText(mItemList.get(position).getName());
        viewHolder.tvDoctorAddress.setText(mItemList.get(position).getAddress());
            GlideUrl glideUrl = new GlideUrl(CommonUtils.BASE_URL + CommonUtils.DOCTOR_URL
                    + mItemList.get(position).getDoctorID()
                    + CommonUtils.PROFILE_URL +  mItemList.get(position).getName().replaceAll(" ", "%20"),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + getCurrentAccessToken())
                            .addHeader("Content-type","application/x-www-form-urlencoded")
                            .build());

            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .thumbnail(0.2f)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(viewHolder.imgProfilePicture);
    }

    private String getCurrentAccessToken(){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(CommonUtils.PREFERENCE_FILE, 0);
        return pref.getString(CommonUtils.SAVED_ACCESS_NAME,"");
    }
}
