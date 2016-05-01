package net.core.tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.core.IStorageServices;
import net.core.models.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ANTON on 06.02.2016.
 */
public class Converter {
    @Autowired
    private IStorageServices metricStorage;
    private final static String baseFile = "value.json";

    public static void toJSON(List<Value> object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(baseFile), object);
//        System.out.println("json created!");
    }
    public static void toJSONTest() throws IOException {
    }




}
