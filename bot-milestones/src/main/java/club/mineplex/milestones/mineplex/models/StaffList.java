package club.mineplex.milestones.mineplex.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class StaffList {

    public HashMap<Integer, JSONObject> staff = new HashMap<>();

    public StaffList(final JSONObject rawList) {

        // Go through all teams
        for (final Iterator<String> it = rawList.keys(); it.hasNext(); ) {

            // Get the team name
            final String team = it.next();

            // Go through all members in the team
            for (final Object object : rawList.getJSONArray(team)) {
                final JSONObject user = (JSONObject) object;
                JSONArray teams = new JSONArray();
                final Integer profileID = user.getInt("memberId");

                // If they already have an entry, update teams
                if (this.staff.containsKey(profileID)) {
                    teams = this.staff.get(profileID).getJSONArray("teams");
                }

                // Add this entry's teams
                teams.put(team);

                // Add teams to the JSONArray
                user.put("teams", teams);

                // Add them to the HashMap
                this.staff.put(profileID, user);

            }

        }

    }

}
