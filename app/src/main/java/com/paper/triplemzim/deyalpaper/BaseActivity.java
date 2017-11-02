package com.paper.triplemzim.deyalpaper;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

public class BaseActivity extends AppCompatActivity{

    RecyclerView rView;


    ProgressDialog progressDialog;

    RecyclerView.LayoutManager RLM;
    RecyclerView.Adapter rAdapter;

    public static HashMap<Integer, fixedImage> hashMap = new HashMap<>();
    public static List<Integer> imageList;
    public static android.support.v4.app.FragmentManager fragmentManager;
    public static int current_position = 0;
    public static Integer[] urlIdx = new Integer[1100];
    public static SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);



        progressDialog = new ProgressDialog(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        imageList = new ArrayList<>();
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        FragmentDialog fragmentDialog = new FragmentDialog();
        fragmentManager = getSupportFragmentManager();
//        fragmentDialog.show(fragmentManager,"Show dialog");

//        looper();
        init();

        RLM = new GridLayoutManager(this.getApplicationContext(),2, GridLayoutManager.VERTICAL,false);
        rView = (RecyclerView) findViewById(R.id.rView);
//        RLM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rView.setLayoutManager(RLM);
//
        rView.setItemAnimator(new DefaultItemAnimator());
//
        rAdapter = new paperAdapter();
//
        rView.setAdapter(rAdapter);

        final GridLayoutManager glm = (GridLayoutManager) rView.getLayoutManager();


//        rView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int totalItem = glm.getItemCount();
//                int lastpos = glm.findLastCompletelyVisibleItemPosition();
//
//                if( lastpos < 1050 && lastpos > totalItem - 7 ){
//                    Toast.makeText(BaseActivity.this, String.valueOf(lastpos) + " "
//                            + String.valueOf(totalItem), Toast.LENGTH_SHORT).show();
//                    looper();
//                }
//            }
//        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                imageList.clear();
                init();
            }

        });



//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.my_toolbar);
////        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
//
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });




    }


    public void onBackPressed(){
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

            BaseActivity.fragmentManager.popBackStack(backStackId, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_res, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_information) {
            Toast.makeText(this, "Developer: Md. Muhim Muktadir Khan\nJunior Software Engineer,\n" +
                    "REVE Systems\nEmail: triplemzim@gmail.com\nThanks to Ifta vaia", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void init(){
        looper();
        for(int i = 0; i < 1081; i++){
            urlIdx[i] = i;
        }
        Collections.shuffle(Arrays.asList(urlIdx));
    }

    //hudai no need of this function
    public static void gotClicked(Context context, int position){

    }

    public class fixedImage{
        public Bitmap bitmap;
        public int idx;
        String url ;
        fixedImage(Bitmap bitmap, int idx){
            this.bitmap = bitmap;
            this.idx = idx;
            this.url = "";
        }

        fixedImage(){

        }
    }

    public void looper(){
        swipeRefreshLayout.setRefreshing(true);
        for(int i = 0; i < 10; i++){
//            new downloadImage(imageList.size()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            imageList.add(imageList.size());
        }

    }






    // no need of this aysnc task could have deleted but...

    public class downloadImage extends AsyncTask<Void, Void, fixedImage>{

        private fixedImage fi;
        private String url = "https://picsum.photos/180/300?image=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.show();
        }

        downloadImage(int idx){
            fi = new fixedImage(null, idx);
            url = url + String.valueOf(urlIdx[idx]);
        }

        @Override
        protected fixedImage doInBackground(Void... params) {
            Bitmap bitmapTemp = null;

            try {
                bitmapTemp = Glide.with(BaseActivity.this)
                        .load(url)
                        .asBitmap()
                        .signature(new StringSignature(String.valueOf((new Random().nextInt(1000)
                                + 1) * (new Random().nextInt(1000) + 1000) )))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(180,300)
                        .get();
                fi.bitmap = bitmapTemp;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return fi;
        }

        @Override
        protected void onPostExecute(fixedImage fi) {
            Bitmap bitmap = fi.bitmap;

            super.onPostExecute(fi);
            if(bitmap != null) {
                hashMap.put(fi.idx, fi);

                rAdapter.notifyDataSetChanged();
            }

            else{
                new downloadImage(fi.idx).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
//            progressDialog.hide();
            if(hashMap.size()%10 == 0) swipeRefreshLayout.setRefreshing(false);
//            Toast.makeText(BaseActivity.this, "Done" + String.valueOf(imageList.size()), Toast.LENGTH_SHORT).show();
        }
    }

}
