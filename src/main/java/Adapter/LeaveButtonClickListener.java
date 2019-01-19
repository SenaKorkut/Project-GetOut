package Adapter;


import android.view.View;

import android.widget.TextView;


import Model.EventManager;

/**
 * The click listener for leave button
 * that sets the view according to changes
 * @author Çağlar Çankaya
 */
public class LeaveButtonClickListener implements View.OnClickListener {

    private EventManager eventManager;
    private TextView participants;


    public LeaveButtonClickListener(EventManager eventManager, TextView participants) {
        this.eventManager = eventManager;
        this.participants = participants;
    }

    @Override
    public void onClick(View view) {
        if (eventManager.dropEvent()) {
            String text = participants.getText().toString();
            String current = text.substring(0, text.indexOf('/'));
            String all = text.substring(text.indexOf('/') + 1, text.length());
            participants.setText((Integer.parseInt(current) - 1 ) + "/" + all);
        }
    }
}
