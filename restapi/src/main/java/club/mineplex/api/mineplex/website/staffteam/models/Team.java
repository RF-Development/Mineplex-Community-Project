package club.mineplex.api.mineplex.website.staffteam.models;

import club.mineplex.api.mineplex.website.profile.models.Profile;
import lombok.Data;

import java.util.List;

@Data
public class Team {
    private final String name;
    private final List<Profile> members;
}
