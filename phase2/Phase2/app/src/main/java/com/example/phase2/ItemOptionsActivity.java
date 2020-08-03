package com.example.phase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.phase2.phase2.ItemManager;
import com.example.phase2.phase2.MeetingManager;
import com.example.phase2.phase2.TradeManager;
import com.example.phase2.phase2.TraderManager;

public class ItemOptionsActivity extends AppCompatActivity {

    private ItemManager itemManager;
    private TraderManager traderManager;
    private TradeManager tradeManager;
    private MeetingManager meetingManager;
    private String currentTrader;
    private Integer chosenItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        itemManager = (ItemManager) bundle.getSerializable("ItemManager");
        traderManager = (TraderManager) bundle.getSerializable("TraderManager");
        tradeManager = (TradeManager) bundle.getSerializable("TradeManager");
        meetingManager = (MeetingManager) bundle.getSerializable("MeetingManager");
        currentTrader = (String) bundle.getString("CurrentTrader");
        chosenItem = (Integer) bundle.getInt("ChosenItem");
        viewStart();
    }

    public void viewStart() { setContentView(R.layout.activity_item_options); }

    public void addToWishList(View view) {
        if(!traderManager.getWishlistIds(currentTrader).contains(chosenItem)) {
            traderManager.addToWishlist(currentTrader, chosenItem);
            Toast.makeText(this, "Item added to your wishlist!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "This item is already in your wishlist.", Toast.LENGTH_LONG).show();
        }
    }

    public void proposeTrade1(View view) {
        if (!traderManager.getIsFrozen(currentTrader)) {
            displayTradeOptions1(view);
        }
        else if (traderManager.isInactive(currentTrader)) {
            Toast.makeText(this, "Your account is inactive," +
                    " you cannot trade.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Your account is frozen," +
                    " you cannot trade.", Toast.LENGTH_LONG).show();
        }
    }

    public void displayTradeOptions1(View view) {
        Intent intent = new Intent(this, DisplayTradeOptions1Activity.class);
        intent.putExtra("ItemManager", itemManager);
        intent.putExtra("TraderManager", traderManager);
        intent.putExtra("TradeManager", tradeManager);
        intent.putExtra("MeetingManager", meetingManager);
        intent.putExtra("CurrentTrader", currentTrader);
        intent.putExtra("ChosenItem", chosenItem);
        startActivity(intent);
        finish();
    }
}