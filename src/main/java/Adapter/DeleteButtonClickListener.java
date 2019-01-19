package Adapter;

import android.content.Intent;
import android.view.View;
import com.bilkent.subfly.getout.MainActivity;

import Model.EventManager;

/**
 * The click listener for delete button
 * that sets the view according to changes
 * @author Çağlar Çankaya
 */
public class DeleteButtonClickListener implements View.OnClickListener {


    private EventManager eventManager;

    public DeleteButtonClickListener( EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void onClick(View view) {
        eventManager.deleteEvent();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        view.getContext().startActivity(intent);
    }
}
