package com.esjohnson.simplebrew;


/**
 * Brew Apparatus contains a name, style, and a boolean for if stirring is required
 */
public class brewApparatus {
    private long _id;
    private String name;
    private String style;
    private boolean stirTime = true;

    public brewApparatus(String name, String style, boolean stirTime) {
        this.name = name;
        this.style = style;
        this.stirTime = stirTime;
        this._id = _id;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public brewApparatus(){
        //empty constructor
    }

    public boolean isStirTime() {
        return stirTime;
    }

    public void setStirTime(boolean stirTime) {
        this.stirTime = stirTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
