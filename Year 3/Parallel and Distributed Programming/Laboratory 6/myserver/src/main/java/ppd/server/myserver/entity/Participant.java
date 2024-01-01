package ppd.server.myserver.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {

    private String id;

    private Double score;

    private String country;

    @Override
    public String toString() {
        return "Participant with id: " + id + " and score: " + score + " from country: " + country + "\n";
    }
}
