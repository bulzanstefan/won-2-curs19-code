package ro.fasttrackit.curs19.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.curs19.model.Country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CountryService {
    private final List<Country> countries = new ArrayList<>();

    public CountryService(CountryReader countryReader) throws IOException {
        countries.addAll(countryReader.readCountriesFromFile());
    }

    public List<Country> getAll(String continent) {
        if (continent == null) {
            return new ArrayList<>(this.countries);
        } else {
            return this.countries.stream()
                    .filter(country -> country.continent().equalsIgnoreCase(continent))
                    .collect(toList());
        }
    }

    public Optional<Country> getById(long countryId) {
        return countries.stream()
                .filter(country -> country.id() == countryId)
                .findFirst();
    }

    public Country addCountry(Country country) {
        return addCountry(fetchMaxId() + 1, country);
    }

    public Country addCountry(int countryId, Country country) {
        Country newCountry = new Country(
                countryId,
                country.name(),
                country.capital(),
                country.population(),
                country.area(),
                country.continent(),
                country.neighbours()
        );
        this.countries.add(countryId - 1, newCountry);
        return newCountry;
    }

    private int fetchMaxId() {
        return this.countries.stream()
                .mapToInt(Country::id)
                .max()
                .orElse(1);
    }

    public Optional<Country> deleteCountry(int countryId) {
        Optional<Country> countryOptional = getById(countryId);
        countryOptional
                .ifPresent(countries::remove);
        return countryOptional;
    }

    public Optional<Country> replaceCountry(int countryId, Country newCountry) {
        Optional<Country> replacedCountry = deleteCountry(countryId);
        replacedCountry
                .ifPresent(deletedCountry -> addCountry(countryId, newCountry));
        return replacedCountry;
    }

    public Optional<Country> patchCountry(int countryId, Country country) {
        Optional<Country> countryById = getById(countryId);
        Optional<Country> patchedCountry = countryById
                .map(oldCountry -> new Country(
                        oldCountry.id(),
                        country.name() != null ? country.name() : oldCountry.name(),
                        country.capital() != null ? country.capital() : oldCountry.capital(),
                        country.population() != 0 ? country.population() : oldCountry.population(),
                        country.area() != 0 ? country.area() : oldCountry.area(),
                        country.continent() != null ? country.continent() : oldCountry.continent(),
                        country.neighbours() != null ? country.neighbours() : oldCountry.neighbours()
                ));
        patchedCountry.ifPresent(newCountry -> replaceCountry(countryId, newCountry));
        return patchedCountry;
    }
}
