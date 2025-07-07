package br.com.alura.screenmatch.service.translation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataResponse( @JsonAlias(value = "translatedText") String textoTraduzido) {
}