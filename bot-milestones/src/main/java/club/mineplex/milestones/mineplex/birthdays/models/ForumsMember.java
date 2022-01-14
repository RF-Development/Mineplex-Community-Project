package club.mineplex.milestones.mineplex.birthdays.models;

import club.mineplex.milestones.mineplex.birthdays.UpdateBirthdays;
import club.mineplex.milestones.mineplex.models.ForumsAvatar;

public class ForumsMember {

    public final int id;
    public final String name;
    public final ForumsAvatar avatar;
    public final String profileURL;
    public final boolean isStaff;

    public ForumsMember(final int forumsId, final String forumsName, final ForumsAvatar forumsAvatar, final String profileURL) {
        this.id = forumsId;
        this.name = forumsName;
        this.avatar = forumsAvatar;
        this.profileURL = profileURL;
        this.isStaff = UpdateBirthdays.staffList.staff.containsKey(this.id);

    }

    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof ForumsMember)) {
            return false;
        }

        return ((ForumsMember) o).id == this.id;

    }

}
