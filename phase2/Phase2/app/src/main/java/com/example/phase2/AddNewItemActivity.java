package com.example.phase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phase2.phase2.ItemManager;

/**
 * An activity class responsible for adding new items in an inventory in the Trading System.
 */
public class AddNewItemActivity extends BundleActivity{
    private ItemManager itemManager;
    private String currentTrader;

    /**
     * Sets up the activity
     * @param savedInstanceState A bundle storing all the necessary objects
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemManager = (ItemManager) getUseCase(ITEMKEY);
        currentTrader = (String) getUsername();
        setContentView(R.layout.activity_add_new_item);
    }

    /**
     * Called when the back button is pressed
     */
    @Override
    public void onBackPressed(){
        replaceUseCase(itemManager);
        Intent intent = new Intent(this, EditInventoryActivity.class);
        putBundle(intent);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    /**
     * This method is called when the user clicks on the Add Item button. It adds the item to list
     * of items needing approval and prompts the user to check back later or fill out all
     * sections if they haven't.
     * @param view A view
     */
    public void addItemButton(View view){

        EditText nameText = findViewById(R.id.enterActualName);
        EditText ratingText = findViewById(R.id.enterActualRating);
        EditText descriptionText = findViewById(R.id.enterActualDescription);
        EditText categoryText = findViewById(R.id.enterActualCategory);

        String name = nameText.getText().toString();
        String rating = (ratingText.getText().toString());
        String description = descriptionText.getText().toString();
        String category = categoryText.getText().toString();


        if (name.equals("") || rating.equals("") || description.equals("") || category.equals(""))
        {
            Toast.makeText(this, "Please fill in all sections.",
                    Toast.LENGTH_LONG).show();
        }else{
            int ratingNumber = Integer.parseInt(rating);
            int itemID = itemManager.addItem(name, currentTrader);
            itemManager.addItemDetails(itemID, category, description, ratingNumber);
            itemManager.changeStatusToRequested(itemID);
            Toast.makeText(this, "Item requested. Check back later.",
                    Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}