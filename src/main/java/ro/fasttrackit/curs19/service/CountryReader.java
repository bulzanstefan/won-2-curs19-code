package ro.fasttrackit.curs19.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.fasttrackit.curs19.model.Country;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class CountryReader {
    private final String fileLocation;

    public CountryReader(@Value("${countries.file}") String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public List<Country> readCountriesFromFile() throws IOException {
        AtomicInteger id = new AtomicInteger(0);
        return Files.lines(Path.of(fileLocation))
                .map(line -> lineToCountry(id.incrementAndGet(), line))
                .collect(toList());
    }

    private Country lineToCountry(int id, String line) {
        String[] countryComponents = line.split("\\|");
        return new Country(id,
                countryComponents[0],
                countryComponents[1],
                Long.parseLong(countryComponents[2]),
                Long.parseLong(countryComponents[3]),
                countryComponents[4],
                countryComponents.length > 5
                        ? parseNeighbours(countryComponents[5])
                        : List.of()
        );
    }

    private List<String> parseNeighbours(String neighbours) {
        return asList(neighbours.split("~"));
    }
}
