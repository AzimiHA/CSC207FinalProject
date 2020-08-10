package com.example.phase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class BrowseTradesActivity extends UpdatableBundleActivity implements ClickableList {

    private MeetingManager meetingManager;
    private TraderManager traderManager;
    private String currentTrader;
    private Integer trade;
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trades);
        currentTrader = (String) getUseCase(USERNAMEKEY);
        updateUseCases();
    }
    /**
     * Updates the Manager classes in the bundle
     */
    protected void updateUseCases(){
        meetingManager = (MeetingManager) getUseCase(MEETINGKEY);
        traderManager = (TraderManager) getUseCase(TRADERKEY);
        viewList();
    }
    /**
     * Updates the ListView object in the XML file
     */
    public void viewList(){
        final List<Integer> onGoingTrades = meetingManager.getOnGoingMeetings(traderManager.getTrades(currentTrader));
        ListView listView = findViewById(R.id.tradesList1);
        ArrayAdapter<Integer> allTradesAdapter = new ArrayAdapter<Integer>(this,
               android.R.layout.simple_list_item_1, onGoingTrades);
        listView.setAdapter(allTradesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               trade = onGoingTrades.get(i);
               displayEditTrade();
            }
       });
    }

    /**
     * Displays the editTrade dialog.
     */
    public void displayEditTrade(){
        Intent intent = new Intent(this, EditTradeActivity.class);
        intent.putExtra("Trade", trade);
        putBundle(intent);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    /**
     * Returns to the previous activity.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}


