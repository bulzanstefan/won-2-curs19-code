package ro.fasttrackit.curs19.model;

import java.util.List;

public record Country(
        int id,
        String name,
        String capital,
        long population,
        long area,
        String continent,
        List<String> neighbours) {
}
