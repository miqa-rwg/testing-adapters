package data;


public enum Month {

    JANUARY     (1, "Jan"),
    FEBRUARY    (2, "Feb"),
    MARCH       (3, "Mar"),
    APRIL       (4, "Apr"),
    MAY         (5, "May"),
    JUNE        (6, "Jun"),
    JULY        (7, "Jul"),
    AUGUST      (8, "Aug"),
    SEPTEMBER   (9, "Sep"),
    OCTOBER     (10, "Oct"),
    NOVEMBER    (11, "Nov"),
    DECEMBER    (12, "Dec");

    public final int index;
    public final String value;

    Month(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public static String getValueByIndex(int index) {
        for(Month item : Month.values()) {
            if(item.index == index)
                return item.value;
        }
        throw new RuntimeException("\nUnexpected value: " + index + ".\nPlease check 'index' value.");
    }
}
