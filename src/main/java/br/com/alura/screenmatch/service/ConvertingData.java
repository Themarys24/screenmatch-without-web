package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ConvertingData implements IConvertData {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtainingData(String json, Class<T> classe) {

        if (json == null || json.trim().isEmpty() || json.contains("Movie not found") || json.startsWith("error")) {
            throw new RuntimeException("Erro ao buscar dados da API.");
        }
        try {
            return mapper.readValue(json, classe);
        } catch (IOException e) {
            System.out.println("❌ Error to convert JSON:");
            System.out.println("JSON received: " + json); // log para depuração
            e.printStackTrace(); // mostra o erro real
            throw new RuntimeException("Error to convert JSON: " + e.getMessage(), e);
        }
    }
}

