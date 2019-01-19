package com.bilkent.subfly.getout;

/*
 * Fragment class for main menu
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapter.SeeEventsAdapter;
import Adapter.SeeHighlightsAdapter;
import Model.Event;
import Model.EventManager;
import Model.EventList;

public class FragmentMain extends Fragment {

    // Tag for alert on terminal
    private static final String TAG = "Fragment Main";


    //Variables
    private RecyclerView highlight;
    private RecyclerView recyclerView;
    private Button create;
    private Button profile;
    private Button categories;
    private EventList eventList;
    private SeeEventsAdapter adapter1;
    private SeeHighlightsAdapter adapter2;
    private EventManager topFiveManager;
    private EventManager runOutOfParticipantsManager;

    //Firebase Data Sync
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialization
        View view = inflater.inflate(R.layout.fragment_layout_main, container, false);
        highlight = (RecyclerView) view.findViewById(R.id.highlight);
        eventList = new EventList();

        //Firebase init...
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        //Shows events that close to finish on number of participants
        recyclerView = (RecyclerView) view.findViewById(R.id.closeToFull);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Button initialization
        create = (Button) view.findViewById(R.id.createMain);
        profile = (Button) view.findViewById(R.id.profile);
        categories = (Button) view.findViewById(R.id.categories);

        //Terminal dialog
        Log.d(TAG, "mCreateView Created...");

        /**
         * Highlights click listener
         */
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Image source'a göre iş yapılacak
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Recycler view click listener
         */
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Recycler Clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Create button click listener
         * Opens create fragment
         */
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getmViewPager().setCurrentItem(0);
            }
        });

        /**
         * Profile button click listener
         * Opens profile layout
         */
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileController.class);
                startActivity(intent);
            }
        });

        /**
         * Categories button click listener
         * Opens categories fragment
         */
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getmViewPager().setCurrentItem(2);
            }
        });

        return view;
    }

    /**
     * Time formatter for dialog
     */
    public Fragment newInstance() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : children){
                    Iterable<DataSnapshot> childrenOfChildren = snapshot.getChildren();
                    for(DataSnapshot snapshot1 : childrenOfChildren){
                        eventList.add(snapshot1.getValue(Event.class));
                    }
                }
                topFiveManager = new EventManager(eventList);
                runOutOfParticipantsManager = new EventManager(eventList);
                EventList runOutOfParticipants = runOutOfParticipantsManager.getRunOutOfParticipant();
                adapter1 = new SeeEventsAdapter(getActivity(), runOutOfParticipants);
                recyclerView.setAdapter(adapter1);
                highlightAnimation();
                EventList topFive = topFiveManager.getTopFive();
                adapter2 = new SeeHighlightsAdapter(getActivity(), topFive);
                highlight.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                highlight.setAdapter(adapter2);
                highlight.setNestedScrollingEnabled(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing...
            }
        });
    }

    /**
     *
     *
     */
    private void highlightAnimation(){
        final int speedScroll = 3000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if(count < adapter2.getItemCount()){
                    if(count==adapter2.getItemCount()-1){
                        flag = false;
                    }else if(count == 0){
                        flag = true;
                    }
                    if(flag) count++;
                    else count = 0;

                    highlight.smoothScrollToPosition(count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };

        handler.postDelayed(runnable,speedScroll);
    }
}