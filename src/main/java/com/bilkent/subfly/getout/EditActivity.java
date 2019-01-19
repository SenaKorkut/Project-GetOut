package com.bilkent.subfly.getout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Model.Event;
import Model.GameEvent;
import Model.GroupWorkEvent;
import Model.MealEvent;
import Model.SportEvent;
import Model.TransportationEvent;
import Model.User;

public class EditActivity extends AppCompatActivity {
    // Tag for alert on terminal
    private static final String TAG = "Creator Created...";
    //For using in dialog panes
    private Calendar calendar = Calendar.getInstance();

    //Firebase initialization
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference databaseEvents = FirebaseDatabase.getInstance().getReference("events");
    private DatabaseReference transportationEvents = databaseEvents.child("transportation_events");
    private DatabaseReference sportEvents = databaseEvents.child("sport_events");
    private DatabaseReference mealEvents = databaseEvents.child("meal_events");
    private DatabaseReference groupWorkEvents = databaseEvents.child("group_work_events");
    private DatabaseReference gameEvents = databaseEvents.child("game_events");

    //For date picking
    DatePickerDialog.OnDateSetListener newDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    //For time picking
    TimePickerDialog.OnTimeSetListener newTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTime();
        }
    };

    //Variables
    private EditText eventNamePlaceHolder;
    private Spinner type;
    private EditText placePlaceHolder;
    private EditText date;
    private EditText time;
    private EditText numberOfParticipants;
    private EditText descriptionPlaceHolder;
    private Button create;
    private Button discard;
    private Event event;
    private Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialization
        setContentView(R.layout.fragment_layout_create);
        extras = getIntent().getExtras();
        eventNamePlaceHolder = findViewById(R.id.eventTitleEditTextID);
        type = (Spinner) findViewById(R.id.type);
        placePlaceHolder = findViewById(R.id.place);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        numberOfParticipants = (EditText) findViewById(R.id.numberOfPart);
        descriptionPlaceHolder = (EditText) findViewById(R.id.descriptionPlaceHolder);
        create = (Button) findViewById(R.id.save);
        discard = (Button) findViewById(R.id.discard);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (extras != null) {
            eventNamePlaceHolder.setText(extras.getString("title"));
            time.setText(extras.getString("hour"));
            descriptionPlaceHolder.setText(extras.getString("description"));
            date.setText(extras.getString("date"));
            numberOfParticipants.setText(extras.getString("personNumber"));
            placePlaceHolder.setText(extras.getString("location"));
        }

        //Firebase init for user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String dummyMail = user.getEmail();
        final User dummyUser = new User(dummyMail);

        //For spinner (dropdown menu) on create part
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.event_types));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter);

        //Printing activity on terminal
        Log.d(TAG,"Creator started...");

        //Time picker dialog in date selection
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditActivity.this, newDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Time picker dialog for time selection
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditActivity.this, newTime, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //Create button listener
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                final String nameOfUser = email.substring( 0 , 1 ).toUpperCase( ) + email.substring( 1 , email.indexOf( '.' )) + " " + email.substring(email.indexOf( '.' )+ 1 , email.indexOf( '.' ) + 2 ).toUpperCase( ) + email.substring( email.indexOf( '.' ) + 2 , email.indexOf( '@' ) );
                final String title = extras.getString("title");
                final String desc = extras.getString("description");
                try{
                    String eventType = type.getSelectedItem().toString();
                    switch (eventType){
                        case "Sport":
                            event = new SportEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            break;
                        case "Transportation":
                            event = new TransportationEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            break;
                        case "Game":
                            event = new GameEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            break;
                        case "Group Work":
                            event = new GroupWorkEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            break;
                        case "Meal":
                            event = new MealEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            break;
                    }
                    final DatabaseReference newEventRef = databaseEvents.child(event.getType());
                    newEventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for(DataSnapshot sh:children){
                                Event temp = sh.getValue(Event.class);
                                if(temp.getTitle().equals(title) && temp.getDescription().equals(desc)){
                                    sh.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Do nothing...
                        }
                    });
                    DatabaseReference deletedRef = userRef.child(dummyUser.getName()).child("created_events");
                    deletedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for(DataSnapshot sh : children){
                                Event temp = sh.getValue(Event.class);
                                if(temp.getTitle().equals(title) && temp.getDescription().equals(desc)){
                                    sh.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Do nothing...
                        }
                    });
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                            for(DataSnapshot user : users){
                                Iterable<DataSnapshot> events = user.child("attending_events").getChildren();
                                for(DataSnapshot event : events){
                                    Event temp = event.getValue(Event.class);
                                    if(temp.getTitle().equals(title) && temp.getDescription().equals(desc)){
                                        event.getRef().removeValue();
                                    }
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    databaseEvents.child(event.getType()).child(event.getTitle()).setValue(event);
                    databaseEvents.child(event.getType()).child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                    databaseEvents.child(event.getType()).child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                    DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");
                    databaseUsers.child(nameOfUser).child("created_events").push().setValue(event);
                    userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Event Created, Now GETOUT!", Toast.LENGTH_SHORT).show();
                    eventNamePlaceHolder.setText("");
                    placePlaceHolder.setText("");
                    date.setText("");
                    time.setText("");
                    numberOfParticipants.setText("");
                    descriptionPlaceHolder.setText("");
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Entries shouldn't be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Discard button listener
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventNamePlaceHolder.setText("");
                placePlaceHolder.setText("");
                date.setText("");
                time.setText("");
                numberOfParticipants.setText("");
                descriptionPlaceHolder.setText("");
                Toast.makeText(getApplicationContext(), "Event Discarded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Date formatter for dialog
     */
    private void updateDate() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Time formatter for dialog
     */
    private void updateTime(){
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        time.setText(sdf.format(calendar.getTime()));
    }
}
