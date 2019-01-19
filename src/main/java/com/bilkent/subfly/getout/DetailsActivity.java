/**
 * The activity that shows the event user touched
 * @author Çağlar Çankaya
 *
 */
package com.bilkent.subfly.getout;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.CurrentParticipantsAdapter;
import Adapter.DeleteButtonClickListener;
import Adapter.EditButtonClickListener;
import Adapter.JoinButtonClickListener;
import Adapter.LeaveButtonClickListener;
import Model.Event;
import Model.EventList;
import Model.EventManager;
import Model.User;

public class DetailsActivity extends AppCompatActivity {

    //View properties
    private TextView title;
    private TextView hour;
    private TextView date;
    private TextView description;
    private TextView location;
    private TextView participants;
    private TextView creatorName;
    private Bundle extras;
    private RecyclerView recyclerView2;
    private CurrentParticipantsAdapter adapter2;
    private EventList eventList;
    private ArrayList<User> userList;
    private Button edit;
    private Button join;
    private Button delete;
    private Button leave;

    //Added by Ali Taha Dinçer
    private FirebaseUser mUser;
    private String currentUserMail;
    private String currentUserName;
    private EventManager eventManager;
    private Event event;
    private User dummyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        extras = getIntent().getExtras();

        //Initializing all view properties
        title = findViewById(R.id.dTitle);
        hour = findViewById(R.id.dHour);
        date = findViewById(R.id.dDate);
        description = findViewById(R.id.dDescription);
        location = findViewById(R.id.dLocation);
        participants = findViewById(R.id.currentNumber);
        creatorName = findViewById(R.id.creatorName);
        eventList = new EventList();
        edit = findViewById(R.id.edit);
        join = findViewById(R.id.join);
        delete = findViewById(R.id.delete);
        leave = findViewById(R.id.leave);

        //Taking data from database
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserMail = mUser.getEmail();
        currentUserName = currentUserMail.substring(0, 1).toUpperCase() + currentUserMail.substring(1, currentUserMail.indexOf('.')) + " " + currentUserMail.substring(currentUserMail.indexOf('.') + 1, currentUserMail.indexOf('.') + 2).toUpperCase() + currentUserMail.substring(currentUserMail.indexOf('.') + 2, currentUserMail.indexOf('@'));

        dummyUser = new User(currentUserMail);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : children) {
                    Iterable<DataSnapshot> childrenOfChildren = snapshot.getChildren();
                    for (DataSnapshot snapshot1 : childrenOfChildren) {
                        eventList.add(snapshot1.getValue(Event.class));
                    }
                }
                for (int i = 0; i < eventList.size(); i++) {
                    if (eventList.get(i).getTitle().equals(title.getText().toString()) &&
                            eventList.get(i).getDescription().equals(description.getText().toString())) {
                        event = eventList.get(i);
                    }
                }
                final DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference("events").child(event.getType()).child(event.getTitle()).child("user_list");
                userListRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList = new ArrayList<User>();
                        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                        for(DataSnapshot sh : snapshots){
                            userList.add(new User(sh.getValue(String.class)));
                        }
                        System.out.println(userList);
                        //Setting current participants of current event
                        recyclerView2 = findViewById(R.id.participantRecycler);
                        recyclerView2.setHasFixedSize(true);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter2 = new CurrentParticipantsAdapter(getApplicationContext(), userList);
                        recyclerView2.setAdapter(adapter2);

                        participants.setText(event.getNumberOfCurrentParticipants() + "/" + event.getNumberOfParticipants());
                        eventManager = new EventManager(event, dummyUser, eventList);
                        System.out.println("Creator name: " + creatorName.getText().toString());

                        //If user is author of this event showing the buttons
                        if (creatorName.getText().toString().compareTo(currentUserName) == 0) {
                            edit.setOnClickListener(new EditButtonClickListener(event));
                            delete.setOnClickListener(new DeleteButtonClickListener(eventManager));
                            join.setVisibility(View.INVISIBLE);
                            leave.setVisibility(View.INVISIBLE);
                        }
                        else {
                            boolean flag = false;
                            for ( int i = 0; i < userList.size(); i++)
                            {
                                if (userList.get(i).getName().equals(currentUserName)){
                                    flag = true;
                                }
                            }
                            if (flag) {
                                leave.setOnClickListener(new LeaveButtonClickListener(eventManager, participants));
                                join.setVisibility(View.INVISIBLE);
                                leave.setVisibility(View.VISIBLE);
                            } else {
                                join.setOnClickListener(new JoinButtonClickListener(eventManager, participants));
                                leave.setVisibility(View.INVISIBLE);
                                join.setVisibility(View.VISIBLE);
                            }
                            edit.setVisibility(View.INVISIBLE);
                            delete.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing...
            }
        });

        if (extras != null) {
            title.setText(extras.getString("title"));
            hour.setText(extras.getString("hour"));
            date.setText(extras.getString("date"));
            description.setText(extras.getString("description"));
            location.setText(extras.getString("location"));
            creatorName.setText(extras.getString("author"));
        }
    }
}
