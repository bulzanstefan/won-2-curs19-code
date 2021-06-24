package ro.fasttrackit.curs19.controller;

import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.curs19.model.Country;
import ro.fasttrackit.curs19.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("countries")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    List<Country> getAll(@RequestParam(required = false) String continent) {
        return countryService.getAll(continent);
    }

    @GetMapping("{countryId}")
    Country getById(@PathVariable long countryId) {
        return countryService.getById(countryId)
                .orElse(null);
    }

    @PostMapping
    Country createCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @DeleteMapping("{countryId}")
    Country deleteCountry(@PathVariable int countryId) {
        return countryService.deleteCountry(countryId)
                .orElse(null);
    }

    @PutMapping("{countryId}")
    Country replaceCountry(@PathVariable int countryId, @RequestBody Country newCountry) {
        return countryService.replaceCountry(countryId, newCountry)
                .orElse(null);
    }

    @PatchMapping("{countryId}")
    Country patchCountry(@PathVariable int countryId, @RequestBody Country country){
        return countryService.patchCountry(countryId, country)
                .orElse(null);
    }
}
