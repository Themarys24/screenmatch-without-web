package br.com.alura.screenmatch.service.translation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataTranslation(@JsonAlias(value = "responseData") DataResponse dadosResposta) {
}
