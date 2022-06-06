package scheduler.addition;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    common(0),
    low(1),
    medium(2),
    high(3),
    critical(4);
    private int value;
    Status(int i) {
        value=i;
    }

    public static List<String> getStatuses(){
        List<String> names = new ArrayList<>();
        for(Status status : Status.values()){
            names.add(status.name());
        }
        return names;
    }
    public int getValue(){
        return value;
    }
}