package com.example.phase2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phase2.phase2.TraderManager;

import java.util.ArrayList;
import java.util.List;

public class FlaggedAccountsMenu extends AppCompatActivity {
    private TraderManager traderManager;
    private String frozenTrader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        traderManager = (TraderManager) bundle.getSerializable("TraderManager");
        viewList();
    }

    public void onClickFreeze(View view) {
        if (traderManager.freezeAccount(frozenTrader)) {
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,
                    "Fail: the account is already frozen", Toast.LENGTH_SHORT).show();
        }
        viewList();
    }

    public void onClickCancel(View view) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        viewList();
    }

    public void viewList() {
        final List<String> allFlaggedTraders = traderManager.getListOfFlagged();
        setContentView(R.layout.activity_flagged_accounts_menu);
        ListView listView = findViewById(R.id.flagged);
        ArrayAdapter<String> allTraderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, allFlaggedTraders);
        listView.setAdapter(allTraderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayFragment();
                frozenTrader = allFlaggedTraders.get(i);
            }
        });
    }

    public void displayFragment() {
    FreezeFlagged freezeFragment = new FreezeFlagged();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager
    .beginTransaction();
    fragmentTransaction.add(R.id.fragment_freezeFlagged_container, freezeFragment).commit();
    }

    //public void displayFragment() {
        //ApprovalFragment approvalFragment = new ApprovalFragment();
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager
                //.beginTransaction();
        //TextView textView = findViewById(R.id.question);
        //textView.setText(R.string.confirmFreeze);
        //Button freeze = findViewById(R.id.approve);
        //Button cancel = findViewById(R.id.reject);
        //freeze.setText(R.string.freezeAction);
        //cancel.setText(R.string.cancelAction);
        //fragmentTransaction.add(R.id.fragment_freezeFlagged_container, approvalFragment).commit();
        //this doesn't work, now in order not to destroy the whole program, I keep the container contains
        //flagged_freeze_fragment, if you want to test, you can change into approve_fragment
    //}
}

