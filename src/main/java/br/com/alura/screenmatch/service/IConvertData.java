package br.com.alura.screenmatch.service;

public interface IConvertData {
    <T> T obtainingData(String json, Class<T> classe);
}
