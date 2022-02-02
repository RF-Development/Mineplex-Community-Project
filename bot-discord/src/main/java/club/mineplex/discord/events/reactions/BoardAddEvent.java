package club.mineplex.discord.events.reactions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/*
 * Custom ClickUp Handler. Forwards Ideas to Trello
 */
public class BoardAddEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(final GuildMessageReactionAddEvent e) {
        /* If the recation is an actual emote */
        if (!e.getReactionEmote().isEmote()) {
            return;
        }

        /* Checking if it matches with the trello emote */
        if (e.getReactionEmote().getEmote().getIdLong() != Main.config.get("clickup-add-emote", Long.class)) {
            return;
        }

        final List<Webhook> webhooks;
        try {
            final RestAction<List<Webhook>> ra = e.getGuild().retrieveWebhooks();
            webhooks = ra.complete();
        } catch (final InsufficientPermissionException x) {
            return;
        }

        /* Looping through webhooks to find suggestions */
        for (final Webhook w : webhooks) {

            /* If the webhook matches with suggestions */
            if (w.getName().equals("Suggestions") && e.getChannel().equals(w.getChannel())) {


                try {

                    final Message query = w.getChannel().retrieveMessageById(e.getMessageIdLong()).complete();

                    /* Looping through reactions to remove the trello add one */
                    for (final MessageReaction mr : query.getReactions()) {
                        if (mr == null) {
                            continue;
                        }

                        if (mr.getReactionEmote().getEmote().getIdLong() == Main.config.get("clickup-add-emote",
                                                                                            Long.class
                        )) {
                            if (mr.getCount() > 1) {
                                query.removeReaction(mr.getReactionEmote().getEmote(), e.getUser()).queue();
                                return;
                            }
                        }
                    }

                    /* Returning if the message to add to trello isn't a suggestion */
                    if (!query.isWebhookMessage()) {
                        return;
                    }
                    final MessageEmbed embed = query.getEmbeds().get(0);

                    /* Returning if there's no embed */
                    if (embed == null) {
                        return;
                    }

                    /* Initializaing variables */
                    String ideasCount = embed.getTitle() != null ?
                            embed.getTitle().replaceAll("Mineplex Community Idea - #", "") : "?";
                    if (!BotUtil.isInteger(ideasCount)) {
                        ideasCount = "?";
                    }
                    final String description = embed.getDescription();
                    final String footer = embed.getFooter() != null ? embed.getFooter().getText() : null;
                    String discriminator = (footer == null ? "0000" : footer.substring(footer.length() - 4));
                    if (!BotUtil.isInteger(discriminator)) {
                        discriminator = "0000";
                    }
                    final String author = query.getAuthor().getName() + "#" + discriminator;

                    final String CLICKUP_TOKEN = Main.config.get("clickup-api-token", String.class);
                    final String CLICKUP_LIST_ID = Main.config.get("clickup-ideas-list-id", String.class);
                    final String CLICKUP_STATUS_NAME = Main.config.get("clickup-status-name", String.class);

                    final String url = String.format("https://api.clickup.com/api/v1/list/%s/task", CLICKUP_LIST_ID);

                    final JsonObject obj = new JsonObject();
                    obj.addProperty("name", "Unprocessed Idea | ID: " + ideasCount);
                    obj.addProperty("status", CLICKUP_STATUS_NAME);
                    obj.addProperty("content", String.format("➜ From: %s\n➜ Idea: %s",

                                                             author,
                                                             description

                    ));

                    final Client client = ClientBuilder.newClient();
                    final Entity payload = Entity.json(new Gson().toJson(obj));
                    final Invocation.Builder builder = client.target(url)
                                                             .request(MediaType.APPLICATION_JSON_TYPE)
                                                             .header("Authorization", CLICKUP_TOKEN);

                    final Response response = builder.post(payload);

                    if (response.getStatus() == 200) {
                        System.out.println(
                                "[SUCCESS] Saved idea from " + query.getAuthor().getName() + " to ClickUp.");
                    } else {
                        System.out.println("Headers: " + response.getHeaders());
                        System.out.println("Body:" + response.readEntity(String.class));
                        throw new Exception("Invalid connection request! Code: " + response.getStatus());
                    }
                } catch (final Exception exception) {
                    System.out.println("[ERROR] Could not save idea to ClickUp!");
                    exception.printStackTrace();
                }
            }
        }
    }
}

