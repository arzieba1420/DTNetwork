package pl.nazwa.arzieba.dtnetworkproject.model;

public enum LeaveType {

    WYPOCZYNKOWY("wypoczynkowy,wypoczynkowego"),
    NŻ("na żądanie"),
    OPIEKUŃCZY("opiekuńczy,opiekuńczego"),
    OKOLICZNOŚCIOWY("okolicznościowy,okolicznościowego"),
    INNY("inny");

    private final String TYPE;

    private LeaveType(String TYPE){
        this.TYPE = TYPE;
    };
}
