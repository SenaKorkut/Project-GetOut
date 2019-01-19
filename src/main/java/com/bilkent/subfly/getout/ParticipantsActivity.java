package com.bilkent.subfly.getout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantsActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView name;

    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_list);

        extras = getIntent().getExtras();
        photo = findViewById(R.id.userImage);
        name = findViewById(R.id.userName);

        if (extras != null) {
            name.setText(extras.getString("name"));
        }
    }
}
