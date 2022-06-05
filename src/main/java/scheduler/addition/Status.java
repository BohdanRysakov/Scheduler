package scheduler.addition;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    low,
    medium,
    high,
    critical;
    public static List<String> getStatuses(){
        List<String> names = new ArrayList<>();
        for(Status status : Status.values()){
            names.add(status.name());
        }
        return names;
    }
}