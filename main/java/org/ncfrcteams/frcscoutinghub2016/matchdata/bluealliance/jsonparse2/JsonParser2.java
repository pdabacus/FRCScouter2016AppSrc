package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2;

import android.util.JsonReader;
import android.util.JsonToken;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JsonParser2 {

    public static JPObject2 getObject(JsonReader reader) {
        try {
            switch (reader.peek()) {
                case BEGIN_OBJECT:
                    Map<String, JPObject2> map = new HashMap<>();

                    reader.beginObject();
                    while (reader.peek() != JsonToken.END_OBJECT) {
                        map.put(reader.nextName(),getObject(reader));
                    }
                    reader.endObject();

                    return new JPMap2(map);

                case BEGIN_ARRAY:
                    List<JPObject2> list = new ArrayList<>();

                    reader.beginArray();
                    while (reader.peek() != JsonToken.END_ARRAY) {
                        list.add(getObject(reader));
                    }
                    reader.endArray();

                    return new JPList2(list);

                case NUMBER:
                case STRING:
                    return new JPString2(reader.nextString());

                default:
                    reader.skipValue();
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
