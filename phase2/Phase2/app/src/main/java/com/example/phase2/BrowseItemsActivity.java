package com.example.phase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.phase2.phase2.ItemManager;
import com.example.phase2.phase2.MeetingManager;
import com.example.phase2.phase2.TradeManager;
import com.example.phase2.phase2.TraderManager;

import java.util.ArrayList;
import java.util.List;

public class BrowseItemsActivity extends BundleActivity implements LocationChoiceFragment.LocationChoiceListener{

    private ItemManager itemManager;
    private TraderManager traderManager;
    private TradeManager tradeManager;
    private MeetingManager meetingManager;
    private String currentTrader;
    private int chosenItem;
    private boolean useLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        itemManager = (ItemManager) getUseCase(ITEMKEY);
        traderManager = (TraderManager) getUseCase(TRADERKEY);
        tradeManager = (TradeManager) getUseCase(TRADEKEY);
        meetingManager = (MeetingManager) getUseCase(MEETINGKEY);
        currentTrader = getUsername();
        useLocation = (Boolean) bundle.getBoolean("LocationChoice");

        if (traderManager.getHomeCity(currentTrader).equals(R.string.notApplicable)) {
            useLocation = false;
            viewList();
        } else {
            createDialogLocationChoice();
        }
    }

    public void createDialogLocationChoice(){
        LocationChoiceFragment lcFragment = new LocationChoiceFragment();
        lcFragment.show(getSupportFragmentManager(), "locationChoice");
    }

    public void viewList(){
        List<Integer> tempItemList = itemManager.getAllApprovedItemsIDs(currentTrader);
        List<Integer> removeList = new ArrayList<>();
        if (useLocation){
            String location = traderManager.getHomeCity(currentTrader);
            for (Integer i: tempItemList){
                if(!traderManager.getHomeCity(itemManager.getOwner(i)).equals(location)){
                    removeList.add(i);
                }
            }
            for (Integer i: removeList){
                tempItemList.remove(i);
            }
        }
        final List<Integer> itemList = tempItemList;
        List<String> itemNameList = new ArrayList<>();
        List<String> itemDescription = new ArrayList<>();
        for (Integer item : itemList) {
            itemNameList.add(itemManager.getItemName(item));
        }
        for (Integer item : itemList) {
            itemDescription.add(itemManager.getItemDescription(item));
        }
        setContentView(R.layout.activity_browse_items);
        ListView listView = findViewById(R.id.selectItem);
        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itemNameList);
        listView.setAdapter(allItemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenItem = itemList.get(i);
                displayItemOptions();
            }
        });
    }

    public void displayItemOptions(){
        Intent intent = new Intent(this, ItemOptionsActivity.class);
        intent.putExtra("ItemManager", itemManager);
        intent.putExtra("TraderManager", traderManager);
        intent.putExtra("TradeManager", tradeManager);
        intent.putExtra("MeetingManager", meetingManager);
        intent.putExtra("CurrentTrader", currentTrader);
        intent.putExtra("LocationChoice", useLocation);
        intent.putExtra("ChosenItem", chosenItem);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        useLocation = true;
        viewList();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        useLocation = false;
        viewList();
    }
}
