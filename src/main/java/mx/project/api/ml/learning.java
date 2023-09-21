package mx.project.api.ml;

import com.google.gson.Gson;
import mx.project.Mx_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class learning {

    static HashMap<Long, List<Number>> base = new HashMap<>();
    public static void putLearn(int id, long index, double data) {
        if(id == 1) {
            if (base.containsKey(index)) {
                if(!base.get(index).contains(data)) {
                    List<Number> local = base.get(index);
                    local.add(data);
                    base.put(index, base.get(index));
                }
            } else {
                List<Number> local = new ArrayList<>();
                local.add(data);
                base.put(index, local);
            }
        }
        Mx_project.getInstance().getLogger().info(new Gson().toJson(base));
    }
}
