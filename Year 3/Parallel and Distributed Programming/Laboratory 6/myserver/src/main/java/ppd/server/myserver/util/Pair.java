package ppd.server.myserver.util;

import java.util.Objects;

public class Pair {
    private final String id;
    private final String country;

    public Pair(String id, String country) {
        this.id = id;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair pair)) return false;
        return Objects.equals(id, pair.id) && Objects.equals(country, pair.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }
}