package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;

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
public class JsonParser {

    public static JPObject getObject(JsonReader reader) {
        try {
            switch (reader.peek()) {
                case BEGIN_OBJECT:
                    Map<String,JPObject> map = new HashMap<>();

                    reader.beginObject();
                    while (reader.peek() != JsonToken.END_OBJECT) {
                        map.put(reader.nextName(),getObject(reader));
                    }
                    reader.endObject();

                    return new JPMap(map);

                case BEGIN_ARRAY:
                    List<JPObject> list = new ArrayList<>();

                    reader.beginArray();
                    while (reader.peek() != JsonToken.END_ARRAY) {
                        list.add(getObject(reader));
                    }
                    reader.endArray();

                    return new JPList(list);

                case NUMBER:
                case STRING:
                    return new JPString(reader.nextString());

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
