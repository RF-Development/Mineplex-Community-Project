package club.mineplex.milestones.mineplex.milestones.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class SubteamList {

    public HashMap<Integer, JSONObject> subStaff = new HashMap<>();

    public SubteamList(final JSONArray teamList) {

        JSONObject team;
        String teamName;

        JSONObject member;
        Integer memberID;

        JSONArray teams;
        List<Object> teamStringList;

        // Go through all teams in the list
        for (final Object object : teamList) {
            team = (JSONObject) object;
            teamName = team.getString("name");
            // Go through all the members of the team
            for (final Object obj : team.getJSONArray("members")) {
                member = (JSONObject) obj;
                memberID = member.getInt("memberId");
                teams = new JSONArray();

                // If they're in the cache already
                if (this.subStaff.containsKey(memberID)) {

                    // Get their current teams
                    teams = this.subStaff.get(memberID).getJSONArray("teams");
                    teamStringList = teams.toList();

                    // If this team isn't there (stop duplicates)
                    if (!teamStringList.contains(teamName)) {
                        teams.put(teamName);
                    }
                } else {
                    teams.put(teamName);
                }

                member.put("teams", teams);
                this.subStaff.put(memberID, member);

            }

        }

    }

}
