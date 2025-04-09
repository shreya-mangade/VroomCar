package com.initialsetup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.car.R;

/**
 * Created by Balvant on 7/25/2015.
 */
public class VroomCarEditProfileActivity extends Activity implements View.OnClickListener {


//    private ImageButton imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup_edit_profile_layout);

        initialisViews();
    }

    private void initialisViews() {

//        imgBack = (ImageButton) findViewById(R.id.img_back);
//        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;
        }

    }
}
