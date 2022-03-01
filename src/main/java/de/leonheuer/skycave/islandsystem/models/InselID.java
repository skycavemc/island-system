package de.leonheuer.skycave.islandsystem.models;

public class InselID {

    private int xAnInt;
    private int zAnInt;

    public InselID(int xAnInt, int zAnInt) {
        setXanInt(xAnInt);
        setZanInt(zAnInt);
    }

    public int getXanInt() {
        return xAnInt;
    }

    public void setXanInt(int xAnInt) {
        this.xAnInt = xAnInt;
    }

    public int getZanInt() {
        return zAnInt;
    }

    public void setZanInt(int zAnInt) {
        this.zAnInt = zAnInt;
    }


}
