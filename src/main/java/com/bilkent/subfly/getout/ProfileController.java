package com.bilkent.subfly.getout;
/*
 * Class that controls profile
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Adapter.SeeEventsAdapter;
import Model.Event;
import Model.EventList;
import Model.User;

public class ProfileController extends AppCompatActivity {

    //Variables
    private ImageView userPhoto;
    private RecyclerView attendingEvents;
    private RecyclerView createdEvents;
    private TextView attendingText;
    private TextView createdText;
    private EventList eventList;
    private  EventList allEvent;
    private PieChart pieChart;
    private Toolbar toolbar;
    private StorageReference mStorage;
    private DatabaseReference databaseReference;
    private Button logout;

    private EventList createdList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        attendingEvents = findViewById(R.id.attendingEventsRecyclerID);
        createdEvents = findViewById(R.id.createdEventsRecyclerID);
        userPhoto = findViewById(R.id.userPhotoID);
        attendingText = findViewById(R.id.attendingTextID);
        createdText = findViewById(R.id.createdEventsTextID);
        pieChart = findViewById(R.id.pieChart);
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logoutbutton);
        userPhoto = findViewById(R.id.userPhotoID);
        mStorage = FirebaseStorage.getInstance().getReference();
        eventList = new EventList();
        allEvent = new EventList();
        createdList = new EventList();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates the pie chart graphics to show user the rate of event type
     * @param pieChart        the pie chart that shows the data
     */
    private void createPieChart(PieChart pieChart, User user) {
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (user.pieChart("sport_events") != 0)
            entries.add(new PieEntry((float) user.pieChart("sport_events"), "Sports"));
        if (user.pieChart("meal_events") != 0)
            entries.add(new PieEntry((float) user.pieChart("meal_events"), "Meals"));
        if (user.pieChart("transportation_events") != 0)
            entries.add(new PieEntry((float) user.pieChart("transportation_events"), "Transportation"));
        if (user.pieChart("game_events") != 0)
            entries.add(new PieEntry((float) user.pieChart("game_events"), "Games"));
        if (user.pieChart("group_work_events") != 0)
            entries.add(new PieEntry((float) user.pieChart("group_work_events"), "Group Work"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterTextSize(20);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setDescription(null);
        pieChart.setHoleRadius(20f);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.DKGRAY);
        pieChart.invalidate();
        pieChart.animateXY(1200, 1200);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        final User user1 = new User(email);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user1.getName()).child("attending_events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                for (DataSnapshot sh : events) {
                    eventList.add(sh.getValue(Event.class));
                    //System.out.println(sh.getValue(Event.class).getTitle());
                }
                attendingEvents.setHasFixedSize(true);
                attendingEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                SeeEventsAdapter attendingAdapter = new SeeEventsAdapter(getApplicationContext(), eventList);
                attendingEvents.setAdapter(attendingAdapter);
                user1.setAttendingEvents(eventList);
                createPieChart(pieChart,user1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Adding adapters to recycler view that shows users attending events
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user1.getName()).child("created_events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allEvent.clear();
                Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                for (DataSnapshot sh : events) {
                    allEvent.add(sh.getValue(Event.class));
                    //System.out.println(sh.getValue(Event.class).getTitle());
                }
                createdEvents.setHasFixedSize(true);
                createdEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                SeeEventsAdapter createdAdapter = new SeeEventsAdapter(getApplicationContext(), allEvent);
                createdEvents.setAdapter(createdAdapter);
                user1.setCreatedEvent(allEvent);
                createPieChart(pieChart,user1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing...
            }

        });
    }
}
