package Adapter;

import android.content.Intent;
import android.view.View;

import com.bilkent.subfly.getout.EditActivity;

import Model.Event;


/**
 * The click listener for edit button
 * that sets the view according to changes
 * @author Çağlar Çankaya
 */

public class EditButtonClickListener implements View.OnClickListener {

    private Event event;

    public EditButtonClickListener (Event event){
        this.event = event;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(),EditActivity.class);
        intent.putExtra("title",event.getTitle());
        intent.putExtra("hour", event.getDeadline());
        intent.putExtra("description",event.getDescription());
        intent.putExtra("date",event.getDate());
        intent.putExtra("personNumber",event.getNumberOfParticipants());
        intent.putExtra("location",event.getPlace());
        view.getContext().startActivity(intent);
    }
}
