package com.team_red.melody.models;



public class Quote {

    public String mQuote;
    public String mAuthor;
    public String mID;

    public Quote(String mQuote, String mAuthor, String mID) {
        this.mQuote = mQuote;
        this.mAuthor = mAuthor;
        this.mID = mID;
    }

    public Quote(String mQuote, String mAuthor) {
        this.mQuote = mQuote;
        this.mAuthor = mAuthor;
    }

    public String getQuote() {
        return mQuote;
    }

    public void setQuote(String mQuote) {
        this.mQuote = mQuote;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }
}
