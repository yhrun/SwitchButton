package com.switchbutton.yhrun.switchbutton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButton mSwitchButton = (SwitchButton)findViewById(R.id.swtichButton);
        mSwitchButton.setOnSwitchButtonChangerListener(new SwitchButton.OnSwitchButtonChangeListener() {
            @Override
            public void onSwitchButtonChange(boolean isCheck) {
                //Toast.makeText(getApplicationContext(),""+isCheck,Toast.LENGTH_SHORT);
                Log.e("ischeck status : ", "" + isCheck);
            }

            @Override
            public void onSwitchButtonReset() {

            }
        });
    }

}
