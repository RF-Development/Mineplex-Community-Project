package club.mineplex.core.discord;

import club.mineplex.core.util.UtilText;
import lombok.*;

import java.awt.*;

@Value
@Builder
public class Embed {

    String title;
    String description;
    int color;
    Footer footer;
    Image image;
    Thumbnail thumbnail;
    Author author;
    Field[] fields;

    public static class EmbedBuilder {

        public EmbedBuilder color(final Color color) {
            this.color = UtilText.getDecimalFromColor(color);
            return this;
        }

    }

    @Value
    @AllArgsConstructor
    public static class Footer {

        @NonNull
        String text;
        String icon_url;

    }

    @Value
    @AllArgsConstructor
    public static class Image {

        @NonNull
        String url;
        int height;
        int width;

    }

    @Value
    @AllArgsConstructor
    public static class Thumbnail {

        @NonNull
        String url;
        int height;
        int width;

    }

    @Value
    @AllArgsConstructor
    public static class Author {

        @NonNull
        String name;
        String url;
        String icon_url;

    }

    @Getter
    @AllArgsConstructor
    public static class Field {

        @NonNull
        private final String name;
        @NonNull
        private final String value;
        private boolean inline;

    }

}
