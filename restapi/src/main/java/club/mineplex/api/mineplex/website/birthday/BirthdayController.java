package club.mineplex.api.mineplex.website.birthday;

import club.mineplex.api.mineplex.website.birthday.models.Birthday;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
@Tag(name = "Mineplex")
public class BirthdayController {
    private final BirthdayService birthdayService;

    @GetMapping("/birthdays")
    @Operation(summary = "Get all birthdays")
    public Set<Birthday> getBirthdays() {
        return this.birthdayService.getBirthdays();
    }
}
