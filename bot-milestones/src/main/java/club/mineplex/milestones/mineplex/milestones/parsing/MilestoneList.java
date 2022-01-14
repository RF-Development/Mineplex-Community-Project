package club.mineplex.milestones.mineplex.milestones.parsing;

import club.mineplex.milestones.mineplex.milestones.models.StaffMember;
import club.mineplex.milestones.utilities.Logger;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MilestoneList {

    private static final Pattern MEMBER_PATTERN = Pattern.compile("^https://www\\.mineplex\\.com/members/(\\d*)/$");
    public List<StaffMember> staffMemberList = new LinkedList<>();

    public MilestoneList(final JSONObject milestones) {

        JSONObject member;
        String name;
        String profileURL;
        int milestone;

        for (final Object entry : milestones.getJSONArray("milestones")) {
            member = (JSONObject) entry;
            name = member.getString("user");
            profileURL = member.getString("profileURL");
            milestone = member.getInt("milestone");

            // Null profile URL
            if (profileURL == null) {
                Logger.log("Profile URL is empty for %s, their milestone will not be shown", Logger.LogLevel.ERROR,
                           member.getString("user")
                );
                continue;
            }

            // Extract ID
            final Matcher memberMatcher = MEMBER_PATTERN.matcher(profileURL);
            if (!memberMatcher.find()) {
                Logger.log("Can't find forums ID inside %s", Logger.LogLevel.ERROR, profileURL);
                continue;
            }

            final int forumsId = Integer.parseInt(memberMatcher.group(1));

            // Add them to the list
            this.staffMemberList.add(
                    new StaffMember(
                            name,
                            forumsId,
                            milestone,
                            profileURL
                    )
            );

        }

    }

    public void fillMembers() {

        for (final StaffMember member : this.staffMemberList) {
            member.scrape();
        }

    }

}
