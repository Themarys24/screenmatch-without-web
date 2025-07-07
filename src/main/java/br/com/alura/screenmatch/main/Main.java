package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner reading = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private ConvertingData converter = new ConvertingData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=47f5743";
    private List<DataSeries> data = new ArrayList<>();

    private SerieRepository repository;

    private List<Series> series = new ArrayList<>();

    private Optional<Series> seriesToSearch;

    public Main(SerieRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Search series
                    2 - Search episodes
                    3 - List the series searched
                    4 - Search series by title
                    5 - Search series by actor
                    6 - Search Top 5 Series
                    7 - Search series by category
                    8 - Search series for maximum number of season and minimum rating
                    9 - Search Episodes by excerpt
                    10 - Top episodes by series
                    11 - Search episodes from a date
                    0 - Exit
                    """;

            System.out.println(menu);
            option = reading.nextInt();
            reading.nextLine();

            switch (option) {
                case 1:
                    searchSeriesWeb();
                    break;
                case 2:
                    searchEpisodeBySeries();
                    break;
                case 3:
                    listSeriessearched();
                    break;
                case 4:
                    searchSeriesByName();
                    break;
                case 5:
                    searchSeriesByActor();
                    break;
                case 6:
                    searchTop5Series();
                    break;
                case 7:
                    searchSeriesByCategory();
                    break;
                case 8:
                    searchSeriesByMaxSeasonMinRating();
                case 9:
                    searchEpisodesByExcerpt();
                    break;
                case 10:
                    topEpisodesBySeries();
                    break;
                case 11:
                    searchEpisodeByDate();
                    break;
                case 0:
                    System.out.println("Leaving...");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        }
    }

    private void searchSeriesWeb() {
        DataSeries dataSeries = getDataSeries();
        Series series = new Series(dataSeries);
        //data.add(dataSeries);
        repository.save(series);
        System.out.println(dataSeries);
    }

    private DataSeries getDataSeries() {
        System.out.println("Enter the series name to search:");
        var nameSerie = reading.nextLine();
        var json = consumption.obtainingData(ADDRESS + nameSerie.replace(" ", "+") + API_KEY);
        DataSeries dataSeries = converter.obtainingData(json, DataSeries.class);
        return dataSeries;
    }

    private void searchEpisodeBySeries() {
        listSeriessearched();
        System.out.println("Choose a series for the name: ");
        var nameSerie = reading.nextLine();

        Optional<Series> serie = repository.findByTitleContainingIgnoreCase(nameSerie);

        if(serie.isPresent()) {

            var serieFound = serie.get();
            List<DataSeason> seasons = new ArrayList<>();

            for (int i = 1; i <= serieFound.getTotalSeasons(); i++) {
                var json = consumption.obtainingData(ADDRESS + serieFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                DataSeason dataSeason = converter.obtainingData(json, DataSeason.class);
                seasons.add(dataSeason);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                    .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());
            serieFound.setEpisodios(episodes);
            repository.save(serieFound);
        }else{
            System.out.println("Series not found");
        }
    }

    private void listSeriessearched(){

        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void searchSeriesByName() {
        System.out.println("Choose a series for the name: ");
        var nameSerie = reading.nextLine();
        seriesToSearch = repository.findByTitleContainingIgnoreCase(nameSerie);

        if (seriesToSearch.isPresent()){
            System.out.println("Data Serie: " + seriesToSearch.get());
        } else {
            System.out.println("Series not found");
        }
    }

    private void searchSeriesByActor(){
        System.out.println("Enter the name to be queried: ");
        var nameActor = reading.nextLine();
        System.out.println("Ratings starting from what value? ");
        var rating = reading.nextDouble();
        List<Series> seriesFound = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(nameActor, rating);
        System.out.println("Series in which the actor worked: ");
        seriesFound.forEach(s ->
                System.out.println(s.getTitle() + " rating: " + s.getRating()));
    }

    private void searchTop5Series(){
        List<Series> seriesTop5 = repository.findTop5ByOrderByRatingDesc();
        seriesTop5.forEach(
                s ->
                        System.out.println(s.getTitle() + " rating: " + s.getRating())
        );
    }

    private void searchSeriesByCategory(){
        System.out.println("Do you want to search for a series by which category? ");
        var nameGenre = reading.nextLine();
        Category category = Category.fromPortugues(nameGenre);
        List<Series> seriesByCategory = repository.findByGenre(category);
        System.out.println("Series of Category " + nameGenre);
        seriesByCategory.forEach(System.out::println);
    }

    private void searchSeriesByMaxSeasonMinRating(){
        System.out.println("Enter the maximum number of seasons: ");
        var totalSeasons = reading.nextInt();
        System.out.println("Enter the minimum number of ratings: ");
        var rating = reading.nextDouble();
        reading.nextLine();

        List<Series> seriesFilter = repository.seriesBySeasonAndRating(totalSeasons, rating);
        System.out.println("*** Filtered Series ***");
        seriesFilter.forEach(s ->
                System.out.println(s.getTitle() + " - rating: " + s.getRating()));

    }

    private void searchEpisodesByExcerpt(){
        System.out.println("Enter the episode name to be queried: ");
        var excerptEpisode = reading.nextLine();
        List<Episode> episodesFound = repository.episodesByexcerpt(excerptEpisode);
        episodesFound.forEach(e ->
                System.out.printf("Series: %s Season %s - Episode %s - %s\n" ,
                e.getSerie().getTitle(), e.getSeason(),
                e.getNumber(), e.getTitle()));
    }

    private void topEpisodesBySeries(){
        searchSeriesByName();

        if (seriesToSearch.isPresent()){
            Series series = seriesToSearch.get();
            List<Episode> topEpisodes = repository.topEpisodesBySeries(series);
            topEpisodes.forEach(e ->
                    System.out.printf("Series: %s Season %s - Episode %s - %s\n" ,
                            e.getSerie().getTitle(), e.getSeason(),
                            e.getNumber(), e.getTitle()));;
        }
    }

    private void searchEpisodeByDate(){
        searchSeriesByName();

        if (seriesToSearch.isPresent()){
            Series series = seriesToSearch.get();
            System.out.println("Enter the limit year to release: ");
            var releaseYear = reading.nextInt();
            reading.nextLine();

            List<Episode> episodeYear = repository.episodeBySerieAndYear(series, releaseYear);
            episodeYear.forEach(System.out::println);
        }
    }



}
