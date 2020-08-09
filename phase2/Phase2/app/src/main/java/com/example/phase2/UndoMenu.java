package com.example.phase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.phase2.phase2.ItemManager;
import com.example.phase2.phase2.MeetingManager;
import com.example.phase2.phase2.TradeManager;

public class UndoMenu extends BundleActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undo_menu);
    }


    public void undoEditMeeting(View view){
        Intent intent = new Intent(this, UndoEditMeeting.class);
        intent.putExtra("chosenTrader", getIntent().getStringExtra("chosenTrader"));
        putBundle(intent);
        startActivity(intent);
    }

    public void undoAgreeTrade(View view){
        Intent intent = new Intent(this, UndoAgreeTrade.class);
        intent.putExtra("chosenTrader", getIntent().getStringExtra("chosenTrader"));
        putBundle(intent);
        startActivity(intent);
    }

    public void undoConfirmTrade(View view){
        Intent intent = new Intent(this, UndoConfirmTrade.class);
        intent.putExtra("chosenTrader", getIntent().getStringExtra("chosenTrader"));
        putBundle(intent);
        startActivity(intent);
    }

    public void undoProposeTrade(View view){
        Intent intent = new Intent(this, UndoProposeTrade.class);
        intent.putExtra("chosenTrader", getIntent().getStringExtra("chosenTrader"));
        putBundle(intent);
        startActivity(intent);
    }

    public void undoRemoveItem(View view){
        Intent intent = new Intent(this, UndoRemoveItem.class);
        intent.putExtra("chosenTrader", getIntent().getStringExtra("chosenTrader"));
        putBundle(intent);
        startActivity(intent);
    }
}