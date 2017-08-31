package de.ivleafcloverapps.tasknotifier;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Christian on 30.08.2017.
 */

public class SettingsActivity extends Activity {

    private Switch switchHelloWorld;
    private Button buttonBack;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_settings);

        // get the view elements
        switchHelloWorld = findViewById(R.id.switchHelloWorld);
        buttonBack = findViewById(R.id.buttonBack);

        // set saved state and listener for switch
        // our context is this activity
        Context context = this;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switchHelloWorld.setChecked(sharedPreferences.getBoolean("switchOn", false));
        switchHelloWorld.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("switchOn", b);
                editor.apply();
            }
        });

        // set listener for button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.finish();
            }
        });
    }
}
