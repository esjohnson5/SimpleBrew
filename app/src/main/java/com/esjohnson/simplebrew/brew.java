package com.esjohnson.simplebrew;

/**
 * Created by esjoh on 1/29/2016.
 * brew object that defines a specific default or custom brew to
 * be set by the user
 * <p>
 * consists of grind, weight of beans, brew time, bloom time, and stir time.  Although all are not
 * needed for the class to function
 *
 */
public class brew {
    private long _id;
    private String name;
    private String grind;
    private int amount;
    private long bloomTimeMillis;
    private long brewTimeMillis;
    private long stirTimeMillis;



    /**
     * constructor for brew object - call be passed NULL references when a brew does not contain
     * one of the following methods
     *
     * @param grind descriptor between course and fine
     * @param amount weight of beans to be used for brew
     * @param brewTimeMillis time of brew
     * @param bloomTimeMillis time of bloom
     * @param stirTimeMillis time of stir
     */

    public brew(String name, String grind, int amount, long brewTimeMillis, long bloomTimeMillis, long stirTimeMillis){
        this.name = name;
        this.grind = grind;
        this.amount = amount;
        this.bloomTimeMillis = bloomTimeMillis;
        this.brewTimeMillis = brewTimeMillis;
        this.stirTimeMillis = stirTimeMillis;
    }

    public brew(){
        //empty constructor
    }

    //setters

    public void setId(Long id) {
        this._id =id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGrind(String grind) {
        this.grind = grind;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public void setBloomTimeMillis(long bloomTimeMillis) {
        this.bloomTimeMillis = bloomTimeMillis;
    }
    public void setBrewTimeMillis(long brewTimeMillis){
        this.brewTimeMillis = brewTimeMillis;
    }
    public void setStirTimeMillis(long stirTimeMillis){
        this.stirTimeMillis = stirTimeMillis;
    }
    //getters

    public long getId() {
        return this._id;
    }
    public String getName() {
        return this.name;
    }
    public String getGrind() {
        return this.grind;
    }
    public int getAmount() {
        return this.amount;
    }
    public long getBloomTime(){
        return this.bloomTimeMillis;
    }
    public long getBrewTime() {
        return this.brewTimeMillis;
    }
    public long getStirTime() {
        return this.stirTimeMillis;
    }

}
