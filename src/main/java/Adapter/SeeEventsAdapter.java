package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilkent.subfly.getout.DetailsActivity;
import com.bilkent.subfly.getout.R;


import Model.Event;
import Model.EventList;

public class SeeEventsAdapter extends RecyclerView.Adapter<SeeEventsAdapter.SeeEventsViewHolder> {

    private Context context;
    private EventList eventsLists;

    public SeeEventsAdapter(Context context, EventList eventsLists){
        this.context = context;
        this.eventsLists = eventsLists;
    }

    @Override
    public SeeEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);
        return new SeeEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeeEventsViewHolder seeEventsViewHolder, int i) {
        Event events = eventsLists.get(i);
        seeEventsViewHolder.title.setText(events.getTitle());
        seeEventsViewHolder.date.setText("Date : " + events.getDate());
        seeEventsViewHolder.hour.setText("Time: " + events.getDeadline());
        seeEventsViewHolder.personNumber.setText("Participants : " + events.getNumberOfCurrentParticipants() + "/" + events.getNumberOfParticipants());
        seeEventsViewHolder.location.setText("Place : " + events.getPlace());
        seeEventsViewHolder.author.setText(events.getUserName());
        if (events.getType().equals("game_events")){
            seeEventsViewHolder.eventImage.setImageResource(R.drawable.games1);
        }else if(events.getType().equals("transportation_events")){
            seeEventsViewHolder.eventImage.setImageResource(R.drawable.transport);
        }else if(events.getType().equals("meal_events")){
            seeEventsViewHolder.eventImage.setImageResource(R.drawable.meals);
        }else if(events.getType().equals("group_work_events")){
            seeEventsViewHolder.eventImage.setImageResource(R.drawable.group_work);
        }else if(events.getType().equals("sport_events")){
            seeEventsViewHolder.eventImage.setImageResource(R.drawable.sport);
        }
    }

    @Override
    public int getItemCount() {
        return eventsLists.size();
    }


    public class SeeEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView hour;
        private TextView date;
        private TextView location;
        private TextView personNumber;
        private ImageView eventImage;
        private TextView author;

        public SeeEventsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            hour =  itemView.findViewById(R.id.hour);
            date = itemView.findViewById(R.id.date);
            personNumber = itemView.findViewById(R.id.attenders);
            location = itemView.findViewById(R.id.location);
            eventImage = itemView.findViewById(R.id.eventImage);
            author = itemView.findViewById(R.id.author);
        }

        @Override
        public void onClick(View view) {
            //Get position of the row clicked
            int position = getAdapterPosition();
            Event events = eventsLists.get(position);
            Intent intent = new Intent(context,DetailsActivity.class);
            intent.putExtra("title",events.getTitle());
            intent.putExtra("hour",events.getDeadline());
            intent.putExtra("description",events.getDescription());
            intent.putExtra("date",events.getDate());
            intent.putExtra("personNumber",events.getNumberOfParticipants());
            intent.putExtra("location",events.getPlace());
            intent.putExtra("currentParticipantNumber", events.getNumberOfCurrentParticipants());
            intent.putExtra("author", events.getUserName());
            context.startActivity(intent);
        }
    }
}
