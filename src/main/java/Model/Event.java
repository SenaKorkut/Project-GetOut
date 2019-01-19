package Model;
/**
 * This class is Event class containing knowledge of events.
 * @author Sena Korkut and Muhammed Naci Dalkıran
 * @version : 18.12.2018
 * @version 23.12.2018
 * @version 24.12.2918 (Last Version)
 */
public class Event  {

    //Properties
    private String title;
    private String place;
    private String date;
    private String deadline;
    private int numberOfCurrentParticipants;
    private int numberOfParticipants;
    private String description;
    private String type;
    private String userName;
    private double rateOfParticipant;

    /**
     * Empty Constructor
     */
    public Event(){
        //Empty Constructor...
    }

    /**
     * This class is constructor class of abstract class Event
     * @param title is a title of event.
     * @param place is a place of event.
     * @param date is a date of event.
     * @param deadline is a deadline of event.
     * @param numberOfParticipants is a number of participants of event.
     * @param description is a description of event.
     */
    public Event( String title, String place, String date, String deadline, int numberOfParticipants, String description, String userName) {
        this.title = title;
        this.place = place;
        this.date = date;
        this.deadline = deadline;
        this.numberOfParticipants = numberOfParticipants;
        this.description = description;
        this.userName = userName;
        numberOfCurrentParticipants = 1;
        type = null;
        rateOfParticipant = (double) numberOfCurrentParticipants/numberOfParticipants;
    }


    /**
     * This method is a getter method of Number Of current participants.
     * @return Number Of current participants.
     */
    public int getNumberOfCurrentParticipants( ) {
        return numberOfCurrentParticipants;
    }

    /**
     *  This method is setter method of Number Of current participants.
     * @param numberOfCurrentParticipants is a total number of how many people participate the event.
     */
    public void setNumberOfCurrentParticipants( int numberOfCurrentParticipants ) {
        this.numberOfCurrentParticipants = numberOfCurrentParticipants;
    }

    /**
     * This is a getter method of title.
     * @return title of event.
     */
    public String getTitle( ) {
        return title;
    }

    /**
     * This method is a setter method of title.
     * @param title is a title of event.
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * This method is getter method of place.
     * @return place of event
     */
    public String getPlace( ) {
        return place;
    }

    /**
     * This method of setter method of place.
     * @param place is place of event
     */
    public void setPlace( String place ) {
        this.place = place;
    }

    /**
     * This method is getter method of date.
     * @return date of event.
     */
    public String  getDate( ) {
        return date;
    }

    /**
     * This method is setter method of date
     * @param date is date of event
     */
    public void setDate( String date ) {
        this.date = date;
    }

    /**
     * This method is getter method of deadline
     * @return deadline of event.
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * This method is setter method of deadline
     * @param deadline is deadline of event.
     */
    public void setDeadline( String deadline ) {
        this.deadline = deadline;
    }

    /**
     * This method is getter method of number of participants
     * @return allowed number of participants of event.
     */
    public int getNumberOfParticipants( ) {
        return numberOfParticipants;
    }

    /**
     * This method is setter method of number of participants.
     * @param numberOfParticipants is allowed number of participants of event.
     */
    public void setNumberOfParticipants( int numberOfParticipants ) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * This method is getter description method.
     * @return description of event.
     */
    public String getDescription( ) {
        return description;
    }

    /**
     * This method is setter method of description.
     * @param description is a description of event.
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * This is getter method of getType.
     * @return type of event.
     */
    public String getType() {
        return type;
    }

    /**
     * This is a setter method of type
     * @param type is type of event.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This is a getter method of User Name.
     * it is for showing author of event.
     * @return UserName is author name of event.
     */
    public String getUserName() {
        return userName;
    }

    public double getRateOfParticipants() {
        return rateOfParticipant;
    }

    /**
     * This is a setter method of rate of participants
     * @param rateOfParticipant is rate which is of participants.
     */
    public void setRateOfParticipants(double rateOfParticipant) {
        this.rateOfParticipant = rateOfParticipant;
    }

    /**
     * This is setter method of User Name.
     * @param userName is name of author of Event.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
