package com.initialsetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.car.R;

public class VroomCarChooseOption extends AppCompatActivity implements View.OnClickListener {
   private RadioButton rdWithinCity,rdInterCity;
    private RadioGroup rggrpRideOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vroom_car_choose_option);
        rdWithinCity=(RadioButton)findViewById(R.id.rdWithinCity);
        rdInterCity=(RadioButton)findViewById(R.id.rdInterCity);
        rggrpRideOption=(RadioGroup)findViewById(R.id.rdGrpRideOption);
        rdWithinCity.setOnClickListener(this);
        rdInterCity.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rdWithinCity:
                Intent mIntent = new Intent(VroomCarChooseOption.this, VroomCarSearchRideActivity.class);
                startActivity(mIntent);

                break;
            case R.id.rdInterCity:
                Intent mIntent1 = new Intent(VroomCarChooseOption.this,VroomCarSearchRideActivity.class);
                startActivity(mIntent1);

                break;
        }
    }

     /*  public void onRadioButtonClicked(View view)
    {
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId())
        {
            case R.id.rdInterCity:
                if(checked) {
                    Intent mIntent = new Intent(VroomCarChooseOption.this, VroomCarSearchRideActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.rdWithinCity: {
                Intent mIntent = new Intent(VroomCarChooseOption.this, VroomCarSearchRideActivity.class);
                startActivity(mIntent);
            }
            break;
        }
    }*/
}
