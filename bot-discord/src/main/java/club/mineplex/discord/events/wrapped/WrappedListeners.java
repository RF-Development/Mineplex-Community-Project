package club.mineplex.discord.events.wrapped;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class WrappedListeners extends ListenerAdapter {

    /*
    MESSAGE SENT
     */
    public void onMessageReceived(MessageReceivedEvent message) {

        // Check correct guild
        if (!message.getGuild().getId().equals(Long.toString((Main.HOME_GUILD_ID)))) return;

        // Return if a bot
        if (message.getAuthor().isBot()) return;

        String userID = message.getAuthor().getId();
        long messageTime = Instant.now().getEpochSecond();

        System.out.println(userID + " " + messageTime);


        // Checking for mentions
        for (Member mentionedMember : message.getMessage().getMentionedMembers()) {

            // No, we don't count Bots...
            if (mentionedMember.getUser().isBot()) continue;

            // No... you don't count for yourself
            if (mentionedMember.getUser().getId().equals(userID)) continue;
            
            System.out.println(userID + " mentioned " + mentionedMember.getId());
        }


    }

    /*
    MEMBER JOIN
     */
    public void onGuildMemberJoin(GuildMemberJoinEvent member) {

        // Return if not the correct guild
        if (!(member.getGuild().getId().equals(Main.HOME_GUILD.getId()))) return;

        int memberCount = member.getGuild().getMemberCount();
        long messageTime = Instant.now().getEpochSecond();

        System.out.println("New Count: " + memberCount);
    }

    /*
    MEMBER LEAVE
     */
    public void onGuildMemberRemove(GuildMemberRemoveEvent member) {

        // Return if not the correct guild
        if (!(member.getGuild().getId().equals(Main.HOME_GUILD.getId()))) return;

        int memberCount = member.getGuild().getMemberCount();
        long messageTime = Instant.now().getEpochSecond();

        System.out.println("New Count: " + memberCount);

    }

    /*
    VOICE CHANNEL TIMER
     */
    public void onGuildVoiceJoin(GuildVoiceJoinEvent test) {

        long joinTime = Instant.now().getEpochSecond();

        VoiceChannel voiceChannel = test.getChannelJoined();
        Member joinUser = test.getMember();
        boolean foundMember = false;

        while (!foundMember) {

            // Check if they're in the channel
            for (Member currentMember : voiceChannel.getMembers()) {

                System.out.println(currentMember.getId());
                System.out.println(joinUser.getId());

                System.out.println(currentMember.equals(joinUser));
                if (currentMember.getId().equals(joinUser.getId())) foundMember = true;
            }

            // Check every 5s
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        long leaveTime = Instant.now().getEpochSecond();

        System.out.println("User was in the channel for " + (leaveTime - joinTime) + " seconds.");
    }

}

