package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.translation.DataTranslation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class ConsultaMyMemory {
    public static String obterTraducao(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ApiConsumption consumo = new ApiConsumption();

        String texto = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

        String json = consumo.obtainingData(url);

        DataTranslation traducao;
        try {
            traducao = mapper.readValue(json, DataTranslation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return traducao.dadosResposta().textoTraduzido();
    }
}




