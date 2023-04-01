package com.example.patientinformation.Data;

public class History {
    private int ID;
    private boolean smoker;
    private boolean heartCondition;
    private boolean asthma;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public boolean hasHeartCondition() {
        return heartCondition;
    }

    public void setHeartCondition(boolean heartCondition) {
        this.heartCondition = heartCondition;
    }

    public boolean hasAsthma() {
        return asthma;
    }

    public void setAsthma(boolean asthma) {
        this.asthma = asthma;
    }
}
