package com.paper.triplemzim.deyalpaper;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.OnClick;

/**
 * Created by HP on 10/19/2017.
 */

public class FragmentDialog extends DialogFragment {

    ImageView iView;
    Button button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container,false);
        iView = (ImageView) view.findViewById(R.id.big_iView);
        button = (Button) view.findViewById(R.id.Dismiss);
        if(BaseActivity.hashMap.containsKey(BaseActivity.current_position)){
            iView.setImageBitmap(BaseActivity.hashMap.get(BaseActivity.current_position).bitmap);
        }
        else{
            //do nothing
        }

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().setTitle("Wallpaper Preview");
        return view;


//        return super.onCreateView(inflater, container, savedInstanceState);
    }






    public interface getImage{
        public void drawImage(Context context, ImageView iView, int position);
    }
}
