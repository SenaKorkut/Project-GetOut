package com.bilkent.subfly.getout;

/*
 * Fragment class of categories
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class FragmentCategories extends Fragment {

    //Tag for startup reference in terminal
    private static final String TAG = "Categories Created...";

    //Variables
    private ImageButton transportation;
    private ImageButton meal;
    private ImageButton sport;
    private ImageButton groupWork;
    private ImageButton game;
    private ImageButton allEvent;
    private EditText editText;
    private Button searchButton;
    private String text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialization
        View view = inflater.inflate(R.layout.fragment_layout_categories,container,false);
        transportation = (ImageButton) view.findViewById(R.id.transportation);
        meal = (ImageButton) view.findViewById(R.id.meal);
        sport = (ImageButton) view.findViewById(R.id.sport);
        groupWork = (ImageButton) view.findViewById(R.id.groupWork);
        game = (ImageButton) view.findViewById(R.id.game);
        allEvent = (ImageButton) view.findViewById(R.id.allEvents);
        editText = (EditText) view.findViewById(R.id.editText);
        searchButton = (Button) view.findViewById(R.id.searchButton);



        //Terminal output
        Log.d(TAG , "Categories Created...");


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = editText.getText().toString();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("search",text);
                startActivity(intent);

            }
        });


        //Categories functionality
        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","transportation_events");
                startActivity(intent);

            }
        });
        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","meal_events");
                startActivity(intent);
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","sport_events");
                startActivity(intent);
            }
        });
        groupWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","group_work_events");
                startActivity(intent);
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","game_events");
                startActivity(intent);
            }
        });
        allEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventViewerActivity.class);
                intent.putExtra("type","AllEvents");
                startActivity(intent);
            }
        });


        return view;

    }

    /**
     * Method for using fragment on viewpager
     * @return fragment type of this class
     */
    public Fragment newInstance(){
        return this;
    }
}
