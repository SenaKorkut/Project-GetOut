package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bilkent.subfly.getout.R;

import java.util.ArrayList;

import Model.User;


public class CurrentParticipantsAdapter extends RecyclerView.Adapter<CurrentParticipantsAdapter.CurrentParticipantsViewHolder>{

    private Context context;
    private ArrayList<User> userList;


    public CurrentParticipantsAdapter(Context context, ArrayList<User> userList){
        this.context = context;
        this.userList = userList;
    }

    public CurrentParticipantsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.participants_list,viewGroup,false);
        return new CurrentParticipantsViewHolder(view);
    }


    public void onBindViewHolder(CurrentParticipantsViewHolder currentParticipantsViewHolder, int i) {


        currentParticipantsViewHolder.name1.setText(userList.get(i).getName());
            // Picasso.get().load(user1.getPhoto()).into(currentParticipantsViewHolder.photo);
            // Picasso.get().load(user2.getPhoto()).into(currentParticipantsViewHolder.pic2);
        }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class CurrentParticipantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView photo;
        private TextView name1;

        public CurrentParticipantsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name1 = itemView.findViewById(R.id.userName);
            photo = itemView.findViewById(R.id.userPhotoID);
        }

        @Override
        public void onClick(View view) {
            //Do nothing...
        }
    }
}