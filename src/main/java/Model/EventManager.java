package Model;

import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;


/**
 * This class has methods.
 * This methods provide to work on events.
 * Also, events' and users' knowledge is edited.
 * @author Sena Korkut and Muhammed Naci dalkıran
 * @version : 18.12.2018
 * @version 23.12.2018 (Last Version)
 */
public class EventManager {

    //Properties
    private EventList topFive;
    private EventList runOutOfParticipant;
    private EventList allEvents;
    private User user;
    private Event event;

    /**
     * This is constructor method.
     * @param event is specific event.
     * @param user is user who is currently using application
     * @param allEvents is list of all events.
     */
    public EventManager(Event event, User user, EventList allEvents) {
        this.event = event;
        this.user = user;
        this.allEvents = allEvents;
    }

    /**
     * This is constructor method.
     * @param user is user who is currently using application.
     */
    public EventManager(User user){
        this.user = user;
    }

    /**
     * This is constructor method.
     * @param eventList is list of all events.
     */
    public EventManager(EventList eventList){
        this.allEvents = eventList;
    }

    /**
     * This method sorts all events according to their number of participants.
     * @return sorted list with top 5 events.
     */
    public EventList getTopFive( ) {

        //Properties
        int counter;
        int numberOfEvent;

        //Program Code
        counter = 0;
        EventList sortedList = new EventList();

        if (allEvents.size() < 5){
            numberOfEvent = allEvents.size();
        }
        else
            numberOfEvent = 5;

        int[] numberOfParticipants = new int[allEvents.size()];
        for (int i = 0; i < allEvents.size(); i ++){
            numberOfParticipants[i] = allEvents.get(i).getNumberOfParticipants();
        }
        Arrays.sort(numberOfParticipants);
        for (int i = numberOfParticipants.length - 1 ; i >  numberOfParticipants.length - numberOfEvent - 1 ; i--){
            for (int j = 0; j < allEvents.size(); j++){
                if (counter < 5){
                    if (numberOfParticipants[i] == allEvents.get(j).getNumberOfParticipants()){
                        sortedList.add(allEvents.get(j));
                        counter++;
                    }
                }

            }

        }
        return sortedList;

    }

    /**
     * This method sorts all events according to rate of current participants to allowed participants
     * @return sorted list with top 3 events.
     */
    public EventList getRunOutOfParticipant() {
        //Properties
        int counter;
        int numberOfEvent;

        //Program Code
        if (allEvents.size() < 5){
            numberOfEvent = allEvents.size();
        }
        else
            numberOfEvent = 5;
        counter = 0;
        EventList sortedList = new EventList();
        double[] rateOfParticipants = new double[allEvents.size()];
        Collections.shuffle(allEvents);
        for (int i = 0; i < allEvents.size(); i ++){
            rateOfParticipants[i] = allEvents.get(i).getRateOfParticipants();
        }

        Arrays.sort(rateOfParticipants);

        for (int i = rateOfParticipants.length - 1 ; i > rateOfParticipants.length - numberOfEvent - 1; i--){

            for (int j = 0; j < allEvents.size(); j++){
                if (counter < 5) {
                    if (rateOfParticipants[i] == allEvents.get(j).getRateOfParticipants()){

                        sortedList.add(getAllEvents().get(j));
                        counter ++;
                    }
                }
            }

        }
        return sortedList;
    }


    /**
     * this is a getter method of all events
     * @return list of all events
     */
    public EventList getAllEvents( ) {
        return allEvents;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    /**
     * This method provides user to join event.
     //* @param event is created by other users
     * @return boolean value
     */
    public boolean joinEvent(){
        if ( event.getNumberOfCurrentParticipants() == event.getNumberOfParticipants() ){
            return false;
        }
        else {
            event.setNumberOfCurrentParticipants(event.getNumberOfCurrentParticipants() + 1);
            //Update participant number
            event.setRateOfParticipants(event.getNumberOfCurrentParticipants() / event.getNumberOfParticipants());
            //Push to event firebase
            DatabaseReference databaseEvents = FirebaseDatabase.getInstance().getReference("events").child(event.getType());
            databaseEvents.child(event.getTitle()).setValue(event);
            databaseEvents.child(event.getTitle()).child("user_list").push().setValue(user.getEmail());
            //Push to user firebase
            DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");

            databaseUsers.child(event.getUserName()).child("created_events").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                    for(DataSnapshot snapshot : events){
                        Event temp = snapshot.getValue(Event.class);
                        if(temp.getTitle().equals(event.getTitle()) && temp.getDescription().equals(event.getDescription())){
                            temp.setNumberOfCurrentParticipants(event.getNumberOfCurrentParticipants());
                            snapshot.getRef().setValue(temp);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Do nothing...
                }
            });
            // For attending events
            databaseUsers.child(event.getUserName()).child("attending_events").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                    for(DataSnapshot snapshot : events){
                        Event temp = snapshot.getValue(Event.class);
                        if(temp.getTitle().equals(event.getTitle()) && temp.getDescription().equals(event.getDescription())){
                            temp.setNumberOfCurrentParticipants(event.getNumberOfCurrentParticipants());
                            snapshot.getRef().setValue(temp);
                            snapshot.getRef().push().setValue(user.getEmail());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Do nothing...
                }
            });
            databaseUsers.child(user.getName()).child("attending_events").push().setValue(event);
            return true;
        }
    }

    /**
     * This methods remove the user from the event
    //* @param event is an event.
     //* @param user is user who wants to drop the event.
     * @return boolean variable
     */
    public boolean dropEvent(){
        user.removeAttendingEvent(event);
        event.setNumberOfCurrentParticipants(event.getNumberOfCurrentParticipants() - 1);
        DatabaseReference databaseEvents = FirebaseDatabase.getInstance().getReference("events").child(event.getType()).child(event.getTitle()).child("user_list");
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                for(DataSnapshot sh : childs){
                    if(sh.getValue(String.class).equals(user.getEmail())){
                        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(user.getName()).child("attending_event");
                        databaseUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for(DataSnapshot sh : children){
                                    Event temp = sh.getValue(Event.class);
                                    if(temp.getTitle().equals(event.getTitle()) && temp.getDescription().equals(event.getDescription())){
                                        sh.getRef().removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //Do nothing
                            }
                        });
                        sh.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing...
            }
        });
        return true;
    }

    /**
     * This method deletes event.
     //* @param event is event created by user in the parameter.
     //* @param user is a user who creates this event.
     * @return is a boolean value.
     */
    public boolean deleteEvent(){
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(event.getType());
        eventRef.child(event.getTitle()).removeValue();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for(DataSnapshot sh:allUsers){
                    Iterable<DataSnapshot> events = sh.child("attending_events").getChildren();
                    for(DataSnapshot cc:events){
                        Event temp = cc.getValue(Event.class);
                        if(temp.getTitle().equals(event.getTitle()) && temp.getDescription().equals(event.getDescription())){
                            cc.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
        DatabaseReference createdEventsRef = FirebaseDatabase.getInstance().getReference("users").child(user.getName()).child("created_events");
        createdEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                for(DataSnapshot e:events){
                    Event temp = e.getValue(Event.class);
                    if(temp.getTitle().equals(event.getTitle()) && temp.getDescription().equals(event.getDescription())){
                        e.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    public void setAllEvents(EventList allEvents) {
        this.allEvents = allEvents;
    }

    /**
     * This method show type events which is wanted by user.
     * @param type is type of events.
     * @return list of determined type of events.
     */
    public EventList getEventByType( String type ){

        //Properties
        EventList typeEvent;

        //Program Code
        typeEvent = new EventList( );
        if (type.equals("AllEvents")){
            typeEvent = allEvents;
        }
        else{
            for ( int i = 0; i < allEvents.size( ); i++ ){
                if ( type.equals( allEvents.get(i).getType( ) ) ) {
                    typeEvent.add( allEvents.get(i) );
                }
            }
        }

        return typeEvent;
    }

    /**
     * This method provides user to edit its event.
     * @param event is a event created by this user.
     * @param title is a new title of event.
     * @param place is a new place of events.
     * @param date is a new date of events.
     * @param deadline is a new deadline of events.
     * @param numberOfParticipants is a new number of participants of events.
     * @param description is a new description of events.
     * @return boolean value.
     */
    public boolean editEvent(Event event, String title, String place, String date, String deadline, int numberOfParticipants, String description){
        event.setDate(date);
        event.setDeadline(deadline);
        event.setNumberOfParticipants(numberOfParticipants);
        event.setDescription(description);
        event.setTitle(title);
        event.setPlace(place);
        return true;
    }
}
