package club.mineplex.milestones.mineplex.milestones.models;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.mineplex.milestones.UpdateMilestones;
import club.mineplex.milestones.mineplex.models.ForumsAvatar;
import club.mineplex.milestones.utilities.BotUtil;
import club.mineplex.milestones.utilities.Logger;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StaffMember {

    public final int milestone;
    public final String profileURL;
    public final int forumsId;
    public List<String> subTeams = new ArrayList<>();
    public List<String> mainTeams = new ArrayList<>();
    public MinecraftAccount bedrockAccount;
    public MinecraftAccount javaAccount;
    public int followers;
    public int following;
    public int posts;
    public int likes;
    public String forumsName;
    public ForumsAvatar avatar;

    public StaffMember(final String effectiveName, final int forumsId, final int milestone, final String profileURL) {
        this.forumsName = effectiveName;
        this.forumsId = forumsId;
        this.milestone = milestone;
        this.profileURL = profileURL;
    }

    public void scrape() {

        // Scrape the pertinent data for sorting
        final JSONObject staffObject = UpdateMilestones.staffList.staff.get(this.forumsId);
        final JSONObject subteamObject = UpdateMilestones.subteamList.subStaff.get(this.forumsId);
        final String memberString = BotUtil.scrapeWebsite(Main.config.MINEPLEX_API_URL + "forum/user/" + this.forumsId);

        /*
        Staff & Sub Teams
         */
        if (staffObject != null) {
            this.mainTeams = staffObject.getJSONArray("teams").toList().stream()
                                        .map(object -> Objects.toString(object, null))
                                        .collect(Collectors.toList());

        }
        if (subteamObject != null) {
            this.subTeams = subteamObject.getJSONArray("teams").toList().stream()
                                         .map(object -> Objects.toString(object, null))
                                         .collect(Collectors.toList());
        }

        if (memberString != null && memberString.startsWith("{")) {
            final JSONObject memberObject = new JSONObject(memberString);
            /*
            Minecraft Accounts
             */
            if (!memberObject.isNull("javaPlayer")) {
                final JSONObject javaPlayer = memberObject.getJSONObject("javaPlayer");
                this.javaAccount = new MinecraftAccount(javaPlayer.getString("name"),
                                                        javaPlayer.getString("rank"),
                                                        javaPlayer.getString("uuid")
                );
            }

            if (!memberObject.isNull("bedrockPlayer")) {
                final JSONObject bedrockPlayer = memberObject.getJSONObject("bedrockPlayer");
                this.bedrockAccount = new MinecraftAccount(bedrockPlayer.getString("name"),
                                                           bedrockPlayer.getString("rank"),
                                                           null
                );
            }

            /*
            Forums Statistics
             */
            final JSONObject forumStats = memberObject.getJSONObject("forumStatistic");

            this.followers = forumStats.getInt("followers");
            this.following = forumStats.getInt("following");
            this.posts = forumStats.getInt("posts");
            this.likes = forumStats.getInt("likes");

            /*
            Profile Data
             */
            this.forumsName = memberObject.getString("name");
            final JSONObject forumAvatar = memberObject.getJSONObject("avatar");
            this.avatar = new ForumsAvatar(
                    forumAvatar.getString("avatarUrl").replaceFirst("/s/", "/l/"),
                    forumAvatar.getString("type")
            );
        }

        Logger.log("Scraped user %s %s", Logger.LogLevel.DEBUG, this.forumsName, this.toString());
    }

    @Override
    public String toString() {
        return String.format(
                "(Forums-ID: %s, Java: %s, Bedrock: %s, Avatar-URL: %s, Teams: %s, Sub-Teams: %s)",
                this.forumsId, this.javaAccount == null ? "N/A" : this.javaAccount.username,
                this.bedrockAccount == null ? "N/A" : this.bedrockAccount.username,
                this.avatar == null ? "N/A" : this.avatar.avatarURL,
                this.mainTeams, this.subTeams
        );
    }

}
