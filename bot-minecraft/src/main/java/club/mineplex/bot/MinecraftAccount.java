package club.mineplex.bot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MinecraftAccount {

    private final AccountType accountType;
    private final String username;
    private final String password;

    @JsonCreator
    public MinecraftAccount(@JsonProperty("accountType") final AccountType accountType,
                            @JsonProperty("username") final String username,
                            @JsonProperty("password") final String password) {
        this.accountType = accountType;
        this.username = username;
        this.password = password;
    }

    public enum AccountType {

        MICROSOFT,
        MOJANG

    }

}
