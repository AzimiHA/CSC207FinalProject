package com.example.phase2.phase2;

import java.io.Serializable;

public class Item implements Serializable {
    private int qualityRating;
    private String name, category, description, owner;
    private final int id;
    private ItemStatus status;

    /**
     * Constructor for an item
     * @param id The id for the item
     * @param name The name of the item
     * @param owner The username of the trader who owns the item
     */
    public Item(Integer id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        status = ItemStatus.REQUESTED;
    }

    /**
     * Gets the item's name.
     * @return the string the item is called
     */
    public String getName(){
           return name;
        }

    /**
     * Sets the item's name.
      * @param name the string the item is called
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the item's category.
     * @return the string the item is categorized as
     */
    public String getCategory(){
        return category;
    }

    /**
     * Sets the item's category.
     * @param category the string the item is categorized as
     */
    public void setCategory(String category){
        this.category = category;
    }

    /**
     * Gets the item's description.
     * @return the string the item is categorized as
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the item's description.
     * @param description the string the item is categorized as
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Gets the item's quality rating.
     * @return the int value that represents the item's quality rating
     */
    public int getQualityRating(){
        return qualityRating;
    }

    /**
     * Getter for status
     * @return Return the status
     */
    public ItemStatus getStatus(){
        return this.status;
    }

    /**
     * Setter for status
     * @param status The status of the item
     */
    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    /**
     * Alternative setter for status
     * @param status The status of the item in string form
     */
    public void setStatus(String status){
        setStatus(convertToItemStatus(status));
    }

    /**
     * Setter for owner
     * @param owner Owner's username
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter for owner
     * @return Return owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the item's id.
     * @return an integer that represents this item's id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the item's qualityRating. Throws IllegalArgumentException if !(1 <= qualityRating <= 10).
     * @param qualityRating the item's quality rating that you want to set to.
     */
    public void setQualityRating(int qualityRating){
        if (qualityRating < 1 || qualityRating > 10){
            throw new IllegalArgumentException("The quality rating of this item must be an integer between 1-10");
        }
        this.qualityRating = qualityRating;
    }

    /**
     * Return a string representation of this Item.
     * @return a String containing the item's qualityRating, name, category, owner and ID.
     */
    @Override
    public String toString() {
        return "| " + name + " |" +
                "\nqualityRating: " + qualityRating +
                ",\ncategory: '" + category +
                "',\ndescription: '" + description +
//                "',\nowner: " + owner.getUsername() +
                ",\nID: " + id;
    }

    /**
     * Converts the status of the item in String to Enum
     * @param status The status of the item
     * @return Return the enum equivalent of status
     */
    private ItemStatus convertToItemStatus(String status){

        ItemStatus itemStatus = null;
        switch (status){
            case "available":
                itemStatus = ItemStatus.AVAILABLE;
                break;
            case "unavailable":
                itemStatus = ItemStatus.UNAVAILABLE;
                break;
            case "requested":
                itemStatus = ItemStatus.REQUESTED;
                break;
            case "inactive":
                itemStatus = ItemStatus.INACTIVE;
        }

        return itemStatus;
    }

    /**
     * Converts the status of the item in Enum to String
     * @param status The status of the item
     * @return Return the string equivalent of status
     */
    public String convertToString(ItemStatus status){

        switch (status){
            case AVAILABLE:
                return "available";
            case UNAVAILABLE:
                return "unavailable";
            case REQUESTED:
                return "requested";
        }
        return "";
    }
}
