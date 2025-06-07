package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DataSeies;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertingData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ApiConsumption apiConsumption = new ApiConsumption();
		var json = apiConsumption.ObtainingData("https://www.omdbapi.com/?t=gilmore+girls&apikey=47f5743");
		System.out.println(json);
		ConvertingData converter = new ConvertingData();
		DataSeies data = converter.obtainingData(json, DataSeies.class);
		System.out.println(data);

	}
}
