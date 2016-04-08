package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPList;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPMap;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPObject;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JPString;
import org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse.JsonParser;

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
import java.util.Map;

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

            String shortName = "";
            String startDate = "";
            String key = "";
            boolean working;

            try(InputStream is = getInputStream(eventUrl)) {

                JsonReader reader = new JsonReader(new InputStreamReader(is));

                JPObject json = JsonParser.getObject(reader);
                if(json == null) {
                    Log.d("JP", "json is null");
                    return null;
                }

                Log.d("JP", "type = " + json.getType());
                JPList jpList = json.getAsType(JPList.class);
                List<JPObject> list;

                JPMap jpMap;
                Map<String,JPObject> map;

                JPString shortNameObj;
                JPString startDateObj;
                JPString keyObj;

                if(jpList != null) {
                    list = jpList.getList();

                    for(JPObject jpObject : list) {
                        working = true;

                        jpMap = jpObject.getAsType(JPMap.class);

                        if(jpMap != null) {
                            map = jpMap.getMap();

                            //**********************************************************************
                            shortNameObj = map.get("short_name").getAsType(JPString.class);
                            if(shortNameObj != null)
                                shortName = shortNameObj.getString();
                            else
                                working = false;

                            //**********************************************************************
                            startDateObj = map.get("start_date").getAsType(JPString.class);
                            if(startDateObj != null)
                                startDate = startDateObj.getString();
                            else
                                working = false;

                            //**********************************************************************
                            keyObj = map.get("key").getAsType(JPString.class);
                            if(keyObj != null)
                                key = keyObj.getString();
                            else
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

                JPList jpList = json.getAsType(JPList.class);
                List<JPObject> list;

                JPMap jpMap;
                Map<String,JPObject> map;

                JPString compLevelObj;
                JPString matchNumObj;

                JPMap allianceMap;
                JPList blueAllianceList;
                JPList redAllianceList;

                if(jpList != null) {
                    list = jpList.getList();

                    String compLevel = "";
                    int matchNumber = 0;
                    int[] teams;
                    boolean working;

                    for(JPObject jpObject : list) {
                        working = true;
                        teams = new int[6];

                        jpMap = jpObject.getAsType(JPMap.class);

                        if(jpMap != null) {
                            map = jpMap.getMap();

                            //**********************************************************************
                            compLevelObj = map.get("comp_level").getAsType(JPString.class);
                            if(compLevelObj != null)
                                compLevel = compLevelObj.getString();
                            else
                                working = false;

                            //**********************************************************************
                            matchNumObj = map.get("match_number").getAsType(JPString.class);
                            if(matchNumObj != null)
                                matchNumber = Integer.parseInt(matchNumObj.getString());
                            else
                                working = false;

                            //**********************************************************************
                            allianceMap = map.get("alliances").getAsType(JPMap.class);
                            if(allianceMap != null) {
                                //***
                                redAllianceList = allianceMap.getMap().get("red").getAsType(JPMap.class).get("teams").getAsType(JPList.class);
                                if(redAllianceList != null) {
                                    for(int i=0; i<3; i++)
                                        teams[i] = getTeamNumFromID(redAllianceList.getList().get(i).getAsType(JPString.class).getString());
                                } else {
                                    working = false;
                                }
                                //***
                                blueAllianceList = allianceMap.getMap().get("blue").getAsType(JPMap.class).get("teams").getAsType(JPList.class);
                                if(blueAllianceList != null) {
                                    for(int i=0; i<3; i++)
                                        teams[i+3] = getTeamNumFromID(blueAllianceList.getList().get(i).getAsType(JPString.class).getString());
                                } else {
                                    working = false;
                                }
                            } else {
                                working = false;
                            }
                        }

                        if(working) {
                            scheduleRows.add(new ScheduleRow(compLevel,matchNumber,teams));
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
