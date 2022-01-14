package club.mineplex.discord.objects;

import java.util.ArrayList;

public class TeamProfile {

    private String name, description;
    private final ArrayList<PlayerProfile> members;

    public TeamProfile(String name, String description, ArrayList<PlayerProfile> members) {
        this.name = name;
        this.description = description;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PlayerProfile> getMembers() {
        return members;
    }

    public String getDescription() {
        return description;
    }

    public void addMember(PlayerProfile profile) {
        this.members.add(profile);
    }

    public void removeMember(PlayerProfile profile) {
        this.members.remove(profile);
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
