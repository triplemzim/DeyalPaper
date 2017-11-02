package com.paper.triplemzim.deyalpaper;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Random;

/**
 * Created by HP on 10/18/2017.
 */

public class paperAdapter extends RecyclerView.Adapter<paperAdapter.ViewHolder>{

//    Bitmap commonBitmap;
    final String baseUrl = "https://picsum.photos/180/300?image=";

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iview;
        public CardView cview;
        public ProgressBar progressBar;
        public Button reload;

        public ViewHolder(View itemView) {
            super(itemView);
            iview = (ImageView) itemView.findViewById(R.id.iView);
            cview = (CardView) itemView.findViewById(R.id.cView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            reload = (Button) itemView.findViewById(R.id.iButton);
        }
    }

    @Override
    public paperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
//        commonBitmap = BitmapFactory.decodeResource(parent.getContext().getResources(), R.drawable.nullimage);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final paperAdapter.ViewHolder holder, final int position) {

        String url = "";
        url = baseUrl + String.valueOf(BaseActivity.urlIdx[position]);
        Glide.with(holder.itemView.getContext())
                .load(url)
//                .placeholder(R.drawable.loading1)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.reload.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }



                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        BaseActivity.swipeRefreshLayout.setRefreshing(false);
                        holder.reload.setVisibility(View.INVISIBLE);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .centerCrop()
                .crossFade()
                .into(holder.iview);


        holder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "Clicked: " + String.valueOf(position)+" "
                        +String.valueOf(BaseActivity.imageList.size()),
                        Toast.LENGTH_SHORT).show();
                BaseActivity.current_position = position;

                Fragment fragment = new FragmentPhoto();
                android.support.v4.app.FragmentTransaction transaction = BaseActivity.fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                transaction.add(R.id.frameFragment,fragment, null);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        holder.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reload.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                Random rand = new Random();
                int temp = rand.nextInt(800) + 1;
                BaseActivity.urlIdx[position] = Integer.valueOf(temp);
//                Toast.makeText(holder.itemView.getContext(), String.valueOf(BaseActivity.imageList.size()), Toast.LENGTH_SHORT).show();

                final String finalUrl = baseUrl + String.valueOf(temp);
                Glide.with(holder.itemView.getContext())
                        .load(finalUrl)
//                        .placeholder(R.drawable.loading1)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.INVISIBLE);
                                holder.reload.setVisibility(View.VISIBLE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                BaseActivity.swipeRefreshLayout.setRefreshing(false);
                                holder.reload.setVisibility(View.INVISIBLE);
                                holder.progressBar.setVisibility(View.INVISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .into(holder.iview);
            }
        });


        if(position == BaseActivity.imageList.size() - 5){
            for(int i = 0; i < 10; i++){
                BaseActivity.imageList.add(BaseActivity.imageList.size());
            }

//            Toast.makeText(holder.itemView.getContext(), String.valueOf(position)
//                    + String.valueOf(BaseActivity.imageList.size()), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public int getItemCount() {
        return BaseActivity.imageList.size();
    }

}
