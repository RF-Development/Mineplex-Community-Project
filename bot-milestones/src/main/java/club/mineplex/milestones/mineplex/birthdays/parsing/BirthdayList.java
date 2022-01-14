package club.mineplex.milestones.mineplex.birthdays.parsing;

import club.mineplex.milestones.mineplex.birthdays.models.ForumsMember;
import club.mineplex.milestones.mineplex.models.ForumsAvatar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BirthdayList {

    public ArrayList<ForumsMember> forumsMembers = new ArrayList<>();

    public BirthdayList(final JSONArray rawList) {

        for (final Object object : rawList) {
            final JSONObject rawMember = ((JSONObject) object).getJSONObject("profile");
            final JSONObject rawAvatar = rawMember.getJSONObject("avatar");

            this.forumsMembers.add(
                    new ForumsMember(
                            rawMember.getInt("memberId"),
                            rawMember.getString("name"),
                            new ForumsAvatar(rawAvatar.getString("avatarUrl"), rawAvatar.getString("type")),
                            rawMember.getString("profileUrl")
                    )
            );
        }

    }

}
