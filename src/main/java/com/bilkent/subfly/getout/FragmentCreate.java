package com.bilkent.subfly.getout;

/*
 * Fragment class of creating events
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

public class FragmentCreate extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialization
        View view = inflater.inflate(R.layout.fragment_layout_create, container, false);
        eventNamePlaceHolder = view.findViewById(R.id.eventTitleEditTextID);
        type = (Spinner) view.findViewById(R.id.type);
        placePlaceHolder = view.findViewById(R.id.place);
        date = (EditText) view.findViewById(R.id.date);
        time = (EditText) view.findViewById(R.id.time);
        numberOfParticipants = (EditText) view.findViewById(R.id.numberOfPart);
        descriptionPlaceHolder = (EditText) view.findViewById(R.id.descriptionPlaceHolder);
        create = (Button) view.findViewById(R.id.save);
        discard = (Button) view.findViewById(R.id.discard);

        //Firebase init for user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String dummyMail = user.getEmail();
        final User dummyUser = new User(dummyMail);

        //For spinner (dropdown menu) on create part
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.event_types));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        type.setAdapter(adapter);

        //Printing activity on terminal
        Log.d(TAG,"Creator started...");

        //Time picker dialog in date selection
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), newDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Time picker dialog for time selection
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), newTime, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //Create button listener
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                String nameOfUser = email.substring( 0 , 1 ).toUpperCase( ) + email.substring( 1 , email.indexOf( '.' )) + " " + email.substring(email.indexOf( '.' )+ 1 , email.indexOf( '.' ) + 2 ).toUpperCase( ) + email.substring( email.indexOf( '.' ) + 2 , email.indexOf( '@' ) );
                try{
                    String eventType = type.getSelectedItem().toString();
                    switch (eventType){
                        case "Sport":
                            event = new SportEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            sportEvents.child(event.getTitle()).setValue(event);
                            sportEvents.child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                            sportEvents.child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                            userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                            break;
                        case "Transportation":
                            event = new TransportationEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            transportationEvents.child(event.getTitle()).setValue(event);
                            transportationEvents.child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                            transportationEvents.child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                            userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                            break;
                        case "Game":
                            event = new GameEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            gameEvents.child(event.getTitle()).setValue(event);
                            gameEvents.child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                            gameEvents.child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                            userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                            break;
                        case "Group Work":
                            event = new GroupWorkEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            groupWorkEvents.child(event.getTitle()).setValue(event);
                            groupWorkEvents.child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                            groupWorkEvents.child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                            userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                            break;
                        case "Meal":
                            event = new MealEvent(eventNamePlaceHolder.getText().toString(), placePlaceHolder.getText().toString(), date.getText().toString(), time.getText().toString(), Integer.parseInt(numberOfParticipants.getText().toString()), descriptionPlaceHolder.getText().toString(), nameOfUser);
                            mealEvents.child(event.getTitle()).setValue(event);
                            mealEvents.child(event.getTitle()).child("user_list").push().setValue(new User(email).getEmail());
                            mealEvents.child(event.getTitle()).child("rateOfParticipants").setValue(event.getRateOfParticipants());
                            userRef.child(dummyUser.getName()).child("created_events").push().setValue(event);
                            break;
                    }
                    ((MainActivity)getActivity()).getmViewPager().setCurrentItem(1);
                    Toast.makeText(getActivity(), "Event Created, Now GETOUT!", Toast.LENGTH_SHORT).show();
                    eventNamePlaceHolder.setText("");
                    placePlaceHolder.setText("");
                    date.setText("");
                    time.setText("");
                    numberOfParticipants.setText("");
                    descriptionPlaceHolder.setText("");
                }catch (Exception e){
                    Toast.makeText(getContext(), "Entries shouldn't be empty!", Toast.LENGTH_LONG).show();
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
                ((MainActivity)getActivity()).getmViewPager().setCurrentItem(1);
                Toast.makeText(getActivity(), "Event Discarded", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    /**
     * Method for using fragment on viewpager
     * @return fragment type of this class
     */
    public Fragment newInstance(){
        return this;
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
