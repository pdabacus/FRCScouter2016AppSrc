package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPList;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPMap;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPObject;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JsonParser;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2.JPList2;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2.JPObject2;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2.JsonParser2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kyle Brown on 4/6/2016.
 */
public class BlueAllianceWebInterface {
    public static final DateFormat EVENT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final String header =  "frc3459:Scouting Application:v1.0";

    public static List<Event> getEvents(int year) {
        List<Event> events = new ArrayList<>();

        try {
            URL eventUrl = new URL("https://www.thebluealliance.com/api/v2/events/" + Integer.toString(year));
            Log.d("JP","URL String = " + eventUrl.toString());

            try(InputStream is = getInputStream(eventUrl)) {

                JsonReader reader = new JsonReader(new InputStreamReader(is));

                JPObject2 object2 = JsonParser2.getObject(reader);
                if(object2 != null) {
                    JPList2 list2 = object2.getAsType(JPList2.class);
                }

//                JPObject2 object2 = JsonParser2.getObject(reader);
//                if(object2 != null) {
//                    JPList2 list2 = object2.getAsType(JPList2.class);
//                }

                Log.d("JP", "type = " + object2.getType());
                JPList list = ((JPList) object2.getAsType(JPList.class));

                JPMap map;

                String shortName = "";
                String startDate = "";
                String key = "";

                boolean working;

                if(list != null) {

                    for(JPObject jpObject : list) {
                        working = true;

                        map = ((JPMap) jpObject.getAsType(JPMap.class));

                        if(map != null) {
                            //**********************************************************************
                            shortName = ((String) map.get("short_name").getAsType(String.class));
                            if(shortName == null)
                                working = false;

                            //**********************************************************************
                            startDate = ((String) map.get("start_date").getAsType(String.class));
                            if(startDate == null)
                                working = false;

                            //**********************************************************************
                            key = ((String) map.get("key").getAsType(String.class));
                            if(key == null)
                                working = false;
                        }

                        if(working) {
                            events.add(new Event(shortName,startDate,key));
                            Log.d("JP", "Successful Event: " + shortName + ", " + startDate + ", " + key);
                        } else {
                            Log.d("Event","Failed Event: " + shortName + ", " + startDate + ", " + key);
                        }
                    }
                } else {
                    Log.d("JP", "jpList is null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return events;
        }

        return events;
    }


    public static String getScheduleCSVForEvent(String key) {
        List<ScheduleRow> scheduleRows = new ArrayList<>();

        try {
            URL url = new URL("https://www.thebluealliance.com/api/v2/event/" + key + "/matches");

            try(InputStream is = getInputStream(url)) {
                JsonReader reader = new JsonReader(new InputStreamReader(is));

                JPObject json = JsonParser.getObject(reader);
                if(json == null) {
                    Log.d("JP", "json is null");
                    return null;
                }

                JPList list = ((JPList) json.getAsType(JPList.class));
                JPMap map;

                String compLevel;
                String matchNumText;

                JPMap allianceMap;
                JPList blueAllianceList;
                JPList redAllianceList;

                if(list != null) {
                    int matchNumber = 0;
                    int[] teams;
                    boolean working;

                    for(JPObject jpObject : list) {
                        working = true;
                        teams = new int[6];

                        map = ((JPMap) jpObject.getAsType(JPMap.class));

                        if(map != null) {

                            //**********************************************************************
                            compLevel = ((String) map.get("comp_level").getAsType(String.class));
                            if(compLevel == null)
                                working = false;

                            //**********************************************************************
                            matchNumText = ((String) map.get("match_number").getAsType(String.class));
                            if(matchNumText != null)
                                matchNumber = Integer.parseInt(matchNumText);
                            else
                                working = false;

                            //**********************************************************************
                            allianceMap = ((JPMap) map.get("alliances").getAsType(JPMap.class));
                            if(allianceMap != null) {
                                //***
                                redAllianceList = ((JPList) ((JPMap) allianceMap.get("red").getAsType(JPMap.class)).get("teams").getAsType(JPList.class));
                                if(redAllianceList != null) {
                                    for(int i=0; i<3; i++)
                                        teams[i] = getTeamNumFromID(((String) redAllianceList.get(i).getAsType(String.class)));
                                } else {
                                    working = false;
                                }
                                //***
                                blueAllianceList = ((JPList) ((JPMap) allianceMap.get("blue").getAsType(JPMap.class)).get("teams").getAsType(JPList.class));
                                if(blueAllianceList != null) {
                                    for(int i=0; i<3; i++)
                                        teams[i+3] = getTeamNumFromID(((String) blueAllianceList.get(i).getAsType(String.class)));
                                } else {
                                    working = false;
                                }
                            } else {
                                working = false;
                            }

                            if(working) {
                                scheduleRows.add(new ScheduleRow(compLevel,matchNumber,teams));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        StringBuilder out = new StringBuilder();

        for(ScheduleRow row : scheduleRows) {
            out.append(row.toString());
        }

        if(out.length() > 0)
            out.deleteCharAt(out.length()-1);

        return out.toString();
    }

    public static InputStream getInputStream(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("X-TBA-App-Id", header);
        return connection.getInputStream();
    }

    public static int getTeamNumFromID(String id) {
        return Integer.parseInt(id.substring(3));
    }


    public static List<Event> eventsInRange(List<Event> originalList, Date startDate, Date endDate) {
        List<Event> filteredList = new ArrayList<>();

        for(Event event : originalList) {
            if(event.startDate.after(startDate) && event.startDate.before(endDate)) {
                filteredList.add(event);
            }
        }

        return filteredList;
    }

    public static List<String> getNamesOf(List<Event> events) {
        List<String> names = new ArrayList<>();

        for(Event event : events) {
            names.add(event.name);
        }

        return names;
    }

    static class Event {
        public String name;
        public Date startDate;
        public String key;

        public Event(String name, String startDateText, String key) {
            this.name = name;
            try {
                this.startDate = EVENT_DATE_FORMAT.parse(startDateText);
            } catch (ParseException e) {
                this.startDate = new Date();
            }
            this.key = key;
        }
    }

    static class ScheduleRow implements Comparable<ScheduleRow> {
        public String compLevel = "";
        public int matchNum;
        public int[] teams;

        public ScheduleRow(String compLevel, int matchNum, int[] teams) {
            this.matchNum = matchNum;
            this.compLevel = compLevel;
            this.teams = teams;
        }

        public String toString() {
            if(!compLevel.equals("qm"))
                return "";

            StringBuilder out = new StringBuilder();

            out.append("Q");
            out.append(matchNum);
            out.append(",");
            for(int teamNum : teams) {
                out.append(teamNum);
                out.append(",");
            }

            if(out.length() > 0)
                out.deleteCharAt(out.length()-1);

            out.append(";");

            return out.toString();
        }

        @Override
        public int compareTo(ScheduleRow another) {
            if(another == null)
                return -1;

            boolean thisIsQual = compLevel.equals("qm");
            boolean otherIsQual = another.compLevel.equals("qm");

            if(thisIsQual == otherIsQual) {
                if(matchNum < another.matchNum){
                    return -1;
                }
                if(matchNum > another.matchNum){
                    return 1;
                }
                return 0;

            } else {
                if(thisIsQual) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        public boolean shouldIgnore() {
            return !compLevel.equals("qm");
        }
    }
}
