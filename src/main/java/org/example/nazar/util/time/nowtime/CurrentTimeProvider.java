package org.example.nazar.util.time.nowtime;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//@Setter
//@Getter
@Component
public class CurrentTimeProvider {


    //    @Value("${data.time.region.continent}")
    private String continent = "Asia";
    //    @Value("${data.time.region.capital}")
    private String capital = "Tehran";

    public String get() {

//        if (continent == null || continent.isEmpty()) {
//            throw new IllegalArgumentException("Continent must not be null or empty");
//        }
//
//        if (capital == null || capital.isEmpty()) {
//            throw new IllegalArgumentException("Capital must not be null or empty");
//        }

        String formattedCapital = rightFormat(capital);
        String zoneId = rightFormat(continent) + "/" + formattedCapital;
        ZonedDateTime nowInIran = ZonedDateTime.now(ZoneId.of(zoneId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return nowInIran.format(formatter);

    }

    private String rightFormat(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase().trim();
    }

}
