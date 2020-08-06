package com.example.phase2.phase2;

import android.content.Intent;
import android.os.Bundle;

import com.example.phase2.LoginActivity;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConfigGateway {
    private final String ADMINPATH = "admins.ser";
    private final String ITEMPATH = "items.ser";
    private final String MEETINGPATH = "meetings.ser";
    private final String TRADERPATH = "traders.ser";
    private final String TRADEPATH = "trade.ser";

    private AdminActions adminActions;
    private ItemManager itemManager;
    private MeetingManager meetingManager;
    private TraderManager traderManager;
    private TradeManager tradeManager;
    private File contextFilesDir;

    public ConfigGateway(File contextFilesDir){
        this.contextFilesDir = contextFilesDir;
    }

    public ConfigGateway(){

    }
    /**
     * Reads objects from the given path
     * @param path the string path of the file
     * @return a Serializable object containing information for the program
     * @throws IOException from the InputStream
     * @throws ClassNotFoundException from ObjectInputStream
     */

    public Serializable readInfo(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            InputStream fileInput = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(fileInput);
            ObjectInput input = new ObjectInputStream(buffer);

            Serializable serializable = (Serializable) input.readObject();
            input.close();
            return serializable;
        } else {
            if (file.createNewFile()) {
                return readInfo(path);
            }
            else{
                return null;
            }
        }
    }

    private void loadClasses(){
        try {
            adminActions = (AdminActions) readInfo(contextFilesDir + "admins.ser");
            meetingManager = (MeetingManager) readInfo(contextFilesDir + "meetings.ser");
            itemManager = (ItemManager) readInfo(contextFilesDir + "items.ser");
            tradeManager = (TradeManager) readInfo(contextFilesDir + "trades.ser");
            traderManager = (TraderManager) readInfo(contextFilesDir + "traders.ser");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            downloadInitial();
        }
    }
    public Bundle getBundle(){
        loadClasses();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TraderManager", traderManager);
        bundle.putSerializable("ItemManager", itemManager);
        bundle.putSerializable("TradeManager", tradeManager);
        bundle.putSerializable("MeetingManager", meetingManager);
        bundle.putSerializable("AdminActions", adminActions);

        return bundle;
    }

    /**
     * Saves the information to the file
     * @param path the string path to the file
     * @param manager the Serializable object containing information about the file
     * @throws IOException from the OutputStream
     */
    public void saveInfo(String path, Serializable manager) throws IOException{
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(manager);
        output.close();
    }

    private void downloadInitial(){
        HashMap<String, Admin> admins = new HashMap<>();
        admins.put("Admin", new Admin("Admin", "Wordpass", "2020-07-27", true));
        adminActions = new AdminActions(admins);
        Item item1 = new Item(1, "Bruh", "Arjun");
        item1.setStatus(ItemStatus.AVAILABLE);
        item1.setCategory("What do you think?");
        item1.setDescription("bruhbruhbruh");
        HashMap<Integer, Item> tempMap = new HashMap<>();
        tempMap.put(1, item1);
        itemManager = new ItemManager(tempMap);
        meetingManager = new MeetingManager(new HashMap<Integer, Meeting>());
        tradeManager = new TradeManager(new HashMap<Integer, Trade>());
        traderManager = new TraderManager(new HashMap<String, Trader>(), 3, 1, 0);
        traderManager.addTrader(new Trader("Trader1", "Password"));
        traderManager.addTrader(new Trader("Trader2", "Password2"));
        Trader traderFlagged = new Trader("Arjun", "Password3");
        traderFlagged.setFlagged(true);
        traderManager.addTrader(traderFlagged);
        List<Integer> tempTradeItems = new ArrayList<>();
        tempTradeItems.add(itemManager.addItem("Bike", "Arjun"));
        itemManager.addItemDetails(tempTradeItems.get(0), "Transportation", "Its a bike", 10);
        itemManager.changeStatusToRemoved(tempTradeItems.get(0));

        //CREATES THE TRADE AND ADDS IT TO TRADE INVENTORY
        Integer tradeId = tradeManager.createTrade("Arjun", "Trader1", "ONEWAY", true, tempTradeItems);
        LocalDate tempDate = LocalDate.now();
        traderManager.addNewTrade("Arjun", tradeId,tempDate);
        traderManager.addNewTrade("Trader1", tradeId, tempDate);
        //CREATES THE MEETING
        meetingManager.createMeeting(tradeId, "Arjun", "Trader1", true);
        meetingManager.setMeetingInfo(tradeId, LocalDate.now(), LocalDate.now(),
                "Toronto", "Toronto");
        adminActions.newAdmin("Admin2", "Wordpass");
        adminActions.newAdmin("Sup", "nothing");
        Trader traderUnfreeze = new Trader("Jeffrey", "Password4");
        traderUnfreeze.setFrozen(true);
        traderUnfreeze.setRequestToUnfreeze(true);
        traderManager.addTrader(traderUnfreeze);

        //Adds two way trade
        List<Integer> tempTradeItems2 = new ArrayList<>();
        Integer temp2 = itemManager.addItem("Jacket", "Trader1");
        Integer temp3 = itemManager.addItem("Watch", "Arjun");
        tempTradeItems2.add(temp2);
        tempTradeItems2.add(temp3);
        itemManager.addItemDetails(temp2, "Clothing", "Its a jacket", 7);
        itemManager.addItemDetails(temp3, "Accessories", "Its a watch", 5);
        itemManager.changeStatusToUnavailable(temp2);
        itemManager.changeStatusToUnavailable(temp3);

        Integer tradeId2 = tradeManager.createTrade("Arjun", "Trader1", "TWOWAY", true, tempTradeItems2);

        LocalDate tempDate2 = LocalDate.now();
        traderManager.addNewTrade("Arjun", tradeId2,tempDate2);
        traderManager.addNewTrade("Trader1", tradeId2, tempDate2);

        meetingManager.createMeeting(tradeId2, "Arjun", "Trader1", true);
        meetingManager.setMeetingInfo(tradeId2, LocalDate.now(), LocalDate.now(),
                "Toronto", "Toronto");

        //Adds temporary one way trade

        //Adds temporary two-way trade
        List<Integer> tempTradeItems4 = new ArrayList<>();
        Integer temp6 = itemManager.addItem("Laptop", "Trader1");
        Integer temp7 = itemManager.addItem("Phone", "Arjun");
        tempTradeItems4.add(temp6);
        tempTradeItems4.add(temp7);
        itemManager.addItemDetails(temp6, "Technology", "Its a laptop", 7);
        itemManager.addItemDetails(temp7, "Technology", "Its a Phone", 5);
        itemManager.changeStatusToUnavailable(temp6);
        itemManager.changeStatusToUnavailable(temp7);

        Integer tradeId4 = tradeManager.createTrade("Arjun", "Trader1", "TWOWAY", false, tempTradeItems4);

        LocalDate tempDate4 = LocalDate.now();
        traderManager.addNewTrade("Arjun", tradeId4,tempDate4);
        traderManager.addNewTrade("Trader1", tradeId4, tempDate4);

        meetingManager.createMeeting(tradeId4, "Arjun", "Trader1", false);
        meetingManager.setMeetingInfo(tradeId4, LocalDate.now(), LocalDate.now(),
                "Toronto", "N/A");

        try {
            saveInfo(contextFilesDir + ADMINPATH, adminActions);
            saveInfo(contextFilesDir + MEETINGPATH, meetingManager);
            saveInfo(contextFilesDir + TRADERPATH, traderManager);
            saveInfo(contextFilesDir + TRADEPATH, tradeManager);
            saveInfo(contextFilesDir + ITEMPATH, itemManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBundle(Bundle bundle){

    }
}
