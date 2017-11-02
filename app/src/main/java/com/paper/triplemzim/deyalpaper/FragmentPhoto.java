package com.paper.triplemzim.deyalpaper;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by HP on 10/23/2017.
 */

public class FragmentPhoto extends Fragment {


    ImageView iview;
    Button closeButton, downloadButton;
    private String url = "https://picsum.photos/400/500?image=";
    View rootView;
    public Bitmap bitmap;
    ProgressDialog progressDialog;
    ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        rootView = inflater.inflate(R.layout.fragment_photo,container,false);
        closeButton = (Button) rootView.findViewById(R.id.Dismiss);
        downloadButton = (Button) rootView.findViewById(R.id.Download);
        iview = (ImageView) rootView.findViewById(R.id.big_iView);
        int pos = BaseActivity.current_position;
        closeButton.setVisibility(Button.VISIBLE);
        downloadButton.setVisibility(Button.VISIBLE);
        downloadButton.setEnabled(false);
        downloadButton.setText("Downloading...");
        bitmap = null;
        progressDialog = new ProgressDialog(rootView.getContext());

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressOnFragment);


        url = url + String.valueOf(BaseActivity.urlIdx[pos]);

//        try {
//            bitmap = Glide.with(rootView.getContext())
//                    .load(url)
//                    .asBitmap()
//                    .into(400,500)
//                    .get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.spinning_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        downloadButton.setText("Set as DeyalPaper");
                        progressBar.setVisibility(View.GONE);
                        downloadButton.setEnabled(true);
                        return false;
                    }
                })
                .crossFade(1000)
                .into(iview);


        iview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeButton.setVisibility(Button.VISIBLE);
                downloadButton.setVisibility(Button.VISIBLE);

                BaseActivity.fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.frameFragment, new FragmentPhoto(), null)
                        .addToBackStack(null)
                        .commit();

                BaseActivity.fragmentManager.popBackStack();



                int backStackCount = BaseActivity.fragmentManager.getBackStackEntryCount();
                for (int i = 0; i < backStackCount; i++) {

                    // Get the back stack fragment id.
                    int backStackId = BaseActivity.fragmentManager.getBackStackEntryAt(i).getId();

                    BaseActivity.fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
//                BaseActivity.fragmentManager.popBackStack();
            }
        });



        
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(rootView.getContext(), "Clicked on download", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                new downloadImage().execute();
            }
        });


        return rootView;
    }

    public class downloadImage extends AsyncTask<Void, Void, Bitmap> {

        Context context;
        downloadImage(){
            this.context = rootView.getContext();

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmapTemp = null;

            try {
                bitmapTemp = Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .signature(new StringSignature(String.valueOf((new Random().nextInt(1000)
                                + 1) * (new Random().nextInt(1000) + 1000) )))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(400,500)
                        .get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bitmapTemp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            if(bitmap != null){
                int pos = BaseActivity.current_position;
                progressDialog.hide();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(rootView.getContext());

                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(rootView.getContext(), "Wallpaper Set!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(rootView.getContext(), "Can't set now! Try again!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
            else{
                new downloadImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
//            Toast.makeText(BaseActivity.this, "Done" + String.valueOf(imageList.size()), Toast.LENGTH_SHORT).show();
        }
    }





}
