import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ConfigReader {
    //TODO: make the variables public or make getter so the constructor does something
    ArrayList<Trader> traders;
    ArrayList<Admin> admins;
    ArrayList<Item> items;

    TraderActions traderActions;
    AdminActions adminActions;
    ItemManager itemManager;
    TradeManager tradeManager;

    ArrayList<User> users;

    public ConfigReader (String fileIn) throws IOException {
        BufferedReader fileInput = new BufferedReader(new FileReader(fileIn));
        traders = new ArrayList<>();
        admins = new ArrayList<>();
        users = new ArrayList<>();
        items = new ArrayList<>();
        String[] input;
        String line = fileInput.readLine();
        while(!line.equals("end")) {
            line = fileInput.readLine();
            input = line.split(",");
                Trader tempTrader = new Trader(input[0], input[1], input[2], new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>(), Boolean.parseBoolean(input[3]), Boolean.parseBoolean(input[4]),Boolean.parseBoolean(input[5]),
                    Integer.parseInt(input[6]), Integer.parseInt(input[7]));

            //Goes through the trader's wantToLend items.
            //Entry format for trader's wantToLend:
            //itemName, category,description,rating, itemID
            line = fileInput.readLine();

            if(line.equals("WantToLend:")){
                line = fileInput.readLine();
                Item tempItem;
                while(!line.equals("ProposedItems:")){
                    input = line.split(",");
                    tempItem = new Item(input[0], input[1], input[2], tempTrader, Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                    items.add(tempItem);
                    tempTrader.addToWantToLend(tempItem);
                    line=fileInput.readLine();
                }
                //Goes through the trader's proposedItems. The entry format is:
                //itemName, category,description,rating, itemID
                line = fileInput.readLine();

                while(!line.equals("Trader:") && !line.equals("end")){
                    input = line.split(",");
                    tempItem = new Item(input[0], input[1], input[2], tempTrader, Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                    tempTrader.addToProposedItems(tempItem);
                    line=fileInput.readLine();
                }
            }
            traders.add(tempTrader);
        }

        line=fileInput.readLine();

        Trader tempTrader;
        Trader tempOwner;
        Item tempItem;
        //This part reads in the all the items in user's wishlists and adds them using their reference created before
        //Entry format for this part is:
        //Users who's wishlist we want to add items to, item1's ID, item1's owner's username, item2's id, item2's user's username, ....
        if(line.equals("Wishlists:")){
            line = fileInput.readLine();
            while(!line.equals("BorrowedItems:")){
                input = line.split(",");

                tempTrader = findTrader(input[0]);
                for(int i = 1; i < input.length-1;i+=2) {
                    tempOwner = findTrader(input[i+1]);
                    assert tempOwner != null;
                    tempItem = findItem(tempOwner, Integer.parseInt(input[i]));
                    assert tempTrader != null;
                    tempTrader.addToWantToBorrow(tempItem);
                }
                line = fileInput.readLine();
            }
        }
        //This part reads in all of the borrowed items this user has, as of now we assume even permanent trades's items get added to the borrowed items list
        //The reason this part is not treated like the wishlist and the wantToLend list is because we need to have all the traders' references
        //Otherwise we won't be able to set these items' owners
        //Entry format for this part is:
        //User's username who's borrowedItems we're adding to, Item name, Category, Description, rating, itemID,ownner's username
        line = fileInput.readLine();
        while(!line.equals("Trades:")){
            input = line.split(",");
            tempTrader = findTrader(input[0]);
            tempOwner = findTrader(input[6]);
            tempItem = new Item(input[1],input[2],input[3], tempOwner, Integer.parseInt(input[4]));
            assert tempTrader != null;
            tempTrader.addToBorrowedItems(tempItem);
            items.add(tempItem);
            line = fileInput.readLine();
        }
        line = fileInput.readLine();
        //This is the part we add in trades.
        //The entry format for trades would be the following:
        //0 TradeType(OneWay or TwoWay), 1initator's username, 2receiver's username, 3location,
        // 4the date the trade will occur, 5isPermanent, 6isCompleted,
        // 7returnDate(note that if a trade is permanent the date here is recorded as 0000-00-00),
        // 8Initiator's username, 9isConfirmed(for initiator), 10numberOfEdits(for initiator),
        // 11isAgreed(for initiator), 12receiver's username, 13isConfirmed(for reciever),
        // 14numberOfEdits(for receiver), 15isAgreed(for receiver), 16TradeStatus.
        while(!line.equals("end")){
        //Note that the format for reading in items is the following: If there is a oneway trade, the item is the receiver's item
        //If its a twoway trade, the first item is item1 of the trade, and the second item is item2 of the trade.
        // Therefore the format should be (oneway trade):
        // Initiator's username, ItemName, Category, Description, rating, itemID, owner's Username
        // TowWayTrade:
        // Initiator's username, ItemName, Category, Description, rating, itemID, owner's Username
        // Receiver's username, itemName, category, description, rating, itemID, owner's username
        //ItemName, Category, description, rating, itemID, owner's username
            input = line.split(",");
            HashMap<String, Boolean> isConfirmed, isAgreed;
            HashMap<String, Integer> numberOfEdits;
            isConfirmed = new HashMap<>();
            String tradeType = input[0];
            Trader initiator = findTrader(input[1]);
            Trader receiver = findTrader(input[2]);
            String location = input[3];
            LocalDate tradeDate = LocalDate.parse(input[4]);
            boolean isPermanent = Boolean.parseBoolean(input[5]);
            boolean isCompleted = Boolean.parseBoolean(input[6]);
            LocalDate returnDate = LocalDate.parse(input[7]);
            String tradeStatus = input[16];
            isAgreed = new HashMap<>();
            numberOfEdits = new HashMap<>();
            //InitiatorUsername
            isConfirmed.put(input[8], Boolean.parseBoolean(input[9]));
            numberOfEdits.put(input[8], Integer.parseInt(input[10]));
            isAgreed.put(input[8], Boolean.parseBoolean(input[11]));
            //ReceiverUsername
            isConfirmed.put(input[12], Boolean.parseBoolean(input[13]));
            numberOfEdits.put(input[12], Integer.parseInt(input[14]));
            isAgreed.put(input[12], Boolean.parseBoolean(input[15]));

            Item item1;
            Item item2;
            OneWayTrade oneWayTrade;
            TwoWayTrade twoWayTrade;
            line = fileInput.readLine();
            input = line.split(",");
            if (isInItems(Integer.parseInt(input[5]))) {
                item1 = getInItems(Integer.parseInt(input[5]));
            } else {
                item1 = new Item(input[1], input[2], input[3], findTrader(input[6]), Integer.parseInt(input[4]), Integer.parseInt(input[5]));
            }
            if (tradeType.equals("OneWayTrade")) {
                oneWayTrade = new OneWayTrade(initiator, receiver, location, tradeDate, isPermanent,
                        isCompleted, returnDate, isConfirmed, numberOfEdits, isAgreed, tradeStatus, item1);
                assert initiator != null;
                initiator.addToTrades(oneWayTrade);
                assert receiver != null;
                receiver.addToTrades(oneWayTrade);
            } else {
                line = fileInput.readLine();
                input = line.split(",");
                if (isInItems(Integer.parseInt(input[5]))) {
                    item2 = getInItems(Integer.parseInt(input[5]));
                } else {
                    item2 = new Item(input[1], input[2], input[3], findTrader(input[6]), Integer.parseInt(input[4]), Integer.parseInt(input[5]));
                }
                twoWayTrade = new TwoWayTrade(initiator, receiver, location, tradeDate, isPermanent,
                        isCompleted, returnDate, isConfirmed, numberOfEdits, isAgreed, tradeStatus, item1, item2);
                assert initiator != null;
                initiator.addToTrades(twoWayTrade);
                assert receiver != null;
                receiver.addToTrades(twoWayTrade);
            }
            Collections.sort(initiator.getTrades());
            Collections.sort(receiver.getTrades());

            line = fileInput.readLine();
        }

        traderActions = new TraderActions(this.traders);
            line = fileInput.readLine();
            while(!line.equals("end")) {
            input = line.split(",");
            admins.add(new Admin(input[0], input[1], input[2]));
            line = fileInput.readLine();
        }
            line = fileInput.readLine();
            ArrayList<Admin> adminRequest = new ArrayList<>();
            while(!line.equals("end")) {
                input = line.split(",");
                adminRequest.add(new Admin(input[0], input[1]));
                line = fileInput.readLine();
            }
            adminActions = new AdminActions(admins, adminRequest);
            users.addAll(admins);
            users.addAll(traders);

             line = fileInput.readLine();
             while(!line.equals("end")){
                 input = line.split(",");
                 int limitOfTradesPerWeek = Integer.parseInt(input[1]);
                 int moreLendNeeded = Integer.parseInt(input[3]);
                 int maxIncomplete = Integer.parseInt(input[5]);
                 tradeManager = new TradeManager(limitOfTradesPerWeek, moreLendNeeded, maxIncomplete);
                 line = fileInput.readLine();
             }

            }

    /**
     * given the trader's username returns that trader
     * @param username the string representing the trader
     * @return the Trader with the given username
     */
    private Trader findTrader(String username){
        for (Trader t: traders){
            if(username.equals(t.getUsername())){
                return t;
            }
        }
        return null;
    }

    /**
     * given the trader and a item ID returns that item
     * @param trader the Trader with the item
     * @param ID the item's id
     * @return the Item with the given id and owner
     */
    private Item findItem(Trader trader, int ID){
        for(Item t: trader.getWantToLend()){
            if(t.getId() == ID){
                return t;
            }
        }
        return null;
    }

    /**
     * checks to see if a given item id is in the config's items arraylist
     * @param id the item's id
     * @return a boolean of if the item is in the config's items arraylist
     */
    private boolean isInItems(int id){
        for(Item i: items){
            if(i.getId()==id){
                return true;
            }
        }
        return false;
    }

    /**
     * gets the item in the  config's items arraylist with the given item id
     * @param id the item's id
     * @return the Item with the given id
     */
    private Item getInItems(int id){
        for(Item i: items){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    /**
     * a getter for traderActions
     * @return the TraderActions class made by the config constructor
     */
    public TraderActions getTraderActions(){
        return traderActions;
    }

    /**
     * a getter for adminActions
     * @return the AdminActions class made by the config constructor
     */
    public AdminActions getAdminActions(){
        return adminActions;
    }

    /**
     * a getter for itemManager
     * @return the ItemManager class made by the config constructor
     */
    public ItemManager getItemManager(){
        return itemManager;
    }

    /**
     * a getter for tradeManager
     * @return the TradeManager class made by the config constructor
     */
    public TradeManager getTradeManager(){
        return tradeManager;
    }

}

