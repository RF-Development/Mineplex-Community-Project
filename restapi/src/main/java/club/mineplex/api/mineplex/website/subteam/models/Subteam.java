package club.mineplex.api.mineplex.website.subteam.models;

import club.mineplex.api.mineplex.website.profile.models.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Subteam {
    private final Profile[] members;
    private final String category;
    private final String name;
    private String description;
}
