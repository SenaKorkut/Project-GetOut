package Model;

/**
 * @author Muhammed Naci Dalkıran and Sena Korkut
 * @version  18.12.2018
 * @version 23.12.2018 (Last Version)
 */
public class User {

    //Properties
    private String name;
    private String email;
    private Event event;
    private EventList attendingEvents;
    private EventList createdEvent;
    private String password;

    /**
     * This is a empty constructor
     */
    public User(){

    }

    /**
     * This is a constructor of User class
     * @param email
     */
    public User(String email){
        setName(email);
        this.email = email;
        attendingEvents = new EventList();
        createdEvent = new EventList();
    }

    /**
     * This is contractor method of this class
     * @param email is a email address of user.
     */
    public User( String email, String password ) {
        this.email = email;
        setName( email );
        attendingEvents = new EventList();
        createdEvent = new EventList();
        this.password = password;
    }

    /**
     * This method adds event to attending Event.
     * @param event is a event user went.
     */
    public void addAttendingEvent( String id, Event event ) {
        this.attendingEvents.add( event );
    }

    /**
     * This method remove event from attending event when user want to drop event.
     * @param event is dropped event by user.
     */
    public void removeAttendingEvent( Event event ){
        this.attendingEvents.remove( event );
    }
    /**
     * This method calculates and returns rateGame for pieChart.
     * @return rateGame
     */
    public float pieChart( String type ){
        //Properties
        EventList eventList;
        float counter;
        //Program Code
        eventList = new EventList();
        eventList.addAll(attendingEvents);
        eventList.addAll(attendingEvents.size(),createdEvent);
        counter = 0;
        for( int i = 0 ; i < eventList.size(); i ++){
            System.out.println("--------------------------------------------------");

            if ( type.equals(eventList.get(i).getType())){
                counter = 10f + counter;
            }
        }
        System.out.println("-------------------------------" + "  " + counter);

        return counter;
    }


    /**
     * This is getter method of name
     * @return name of user.
     */
    public String getName( ) {
        return name;
    }

    /**
     * This is getter method of Email.
     * @return email of user.
     */
    public String getEmail( ) {
        return email;
    }

    /**
     * This is setter method of name
     * @param email is a email address of user.
     */
    private void setName( String email ){
        name = "";
        name += email.substring( 0 , 1 ).toUpperCase( ) + email.substring( 1 , email.indexOf( '.' )) + " " + email.substring(email.indexOf( '.' )+ 1 , email.indexOf( '.' ) + 2 ).toUpperCase( ) + email.substring( email.indexOf( '.' ) + 2 , email.indexOf( '@' ) );
    }

    /**
     * This is getter method of event.
     * @return even of user.
     */
    public Event getEvent( ) {
        return event;
    }

    /**
     * This is setter method of created event.
     * @param createdEvent is created by user.
     */
    public void setCreatedEvent( EventList createdEvent ) {
        this.createdEvent = createdEvent;
    }

    /**
     * This is getter method of attending events
     * @return attending event of user.
     */
    public EventList getAttendingEvents( ) {
        return attendingEvents;
    }

    /**
     * This is getter method of createdEvent.
     * @return event created by user.
     */
    public EventList getCreatedEvent( ) {
        return createdEvent;
    }




    /**
     * This is setter event of attending events.
     * @param attendingEvents is list of attending event.
     */
    public void setAttendingEvents(EventList attendingEvents) {
        this.attendingEvents = attendingEvents;
    }

    /**
     * This is to String method of user.
     * @return name of user.
     */
    public String toString( ){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
