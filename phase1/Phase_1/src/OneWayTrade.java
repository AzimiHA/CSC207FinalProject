import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class OneWayTrade extends Trade{
    private Item item;


    /**
     * Default constructor of OneWayTrade
     * @param initiator the user who started this trade.
     * @param receiver the user who will receive the item in the trade.
     * @param location the location of the Trade
     * @param tradeDate the date the Trade will occur
     * @param item the item that initiator wants to trade
     */
    public OneWayTrade(Trader initiator, Trader receiver, String location,
                       LocalDate tradeDate, Item item){
        super(initiator, receiver, location, tradeDate);
        this.item = item;
    }

    public OneWayTrade(Trader initiator, Trader receiver, String location, LocalDate tradeDate, boolean isPermanent, boolean isCompleted,
                 LocalDate returnDate, HashMap<String, Boolean> isConfirmed, HashMap<String, Integer> numberOfEdits,
                 HashMap<String, Boolean> isAgreed, String tradeStatus, Item item){
        super(initiator, receiver, location, tradeDate, isPermanent, isCompleted,
        returnDate, isConfirmed, numberOfEdits, isAgreed, tradeStatus);
        this.item = item;
    }



    /**
     * @return Returns the String representation of OneWayTrade
     */
    @Override
    public String toString() {
        return "OneWayTrade{" +
                "initiator='" + super.getInitiator().getUsername() + '\'' +
                ", receiver='" + super.getReceiver().getUsername() + '\'' +
                ", tradedItem='" + item.toString() + '\''+
                ", tradeDate=" + super.getTradeDate().toString() + '\'' +
                ", location=" + super.getLocation()  + '\'' +
                ", permanent=" + super.isPermanent() + '\'' +
                ", isCompleted=" + super.isCompleted()  + '\'' +
                ", tradeStatus=" + super.getTradeStatus() + '\'' +
                ", returnDate=" + super.returnDateString() + '\'' +
                '}';
    }

    /**
     * Compares the date of this Trade to another Trade.
     * @param trade The other Trade whose date is going to be compared.
     * @return An integer representing the result of the comparison.
     */
    @Override
    public int compareTo(Trade trade) {
        return super.compareTo(trade);
    }

    /**
     * getter for the traded item
     * @return the traded item
     */
    public Item getItem(){ return item; }

    @Override
    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        return items;
    }
}