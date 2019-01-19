package Model;


/**
 * @author Muhammed Naci Dalkıran
 * @version 12.12.2018
 * @version 23.12.2018 (Last Version)
 */
public class GameEvent extends Event{

    /**
     * Empty Constructor
     */
    public GameEvent(){}
    /**
     * This class is constructor class of abstract class Event
     * @param title is a title of event.
     * @param place is a place of event.
     * @param date is a date of event.
     * @param deadline is a deadline of event.
     * @param numberOfParticipants is a number of participants of event.
     * @param description is a description of event.
     */
    public GameEvent(String title, String place, String date, String deadline, int numberOfParticipants, String description, String username ) {
        super( title, place, date, deadline, numberOfParticipants, description, username );
        super.setType( "game_events" );
    }

    /**
     * This is getter method of type.
     * @return type of event
     */
    public String getType( ) {
        return super.getType();
    }
}
