package com.web.semantic.api.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.web.semantic.api.Modeles.Informations;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonService {

    public JSONObject getInformationsWeather(Double longitude, Double latitude) throws IOException {
        Informations infoWeather = new Informations(latitude, longitude);
        return infoWeather.infoWeather();
    }

    public JSONObject getInformationsCity(Double longitude, Double latitude) throws IOException {
        Informations infoCity = new Informations(latitude, longitude);
        return infoCity.infoCity();
    }

    public String JsonObjectToString(String fieldName, JSONObject jsonObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode childNode = mapper.createObjectNode();
        JsonNode rootNode = mapper.createObjectNode();

        ((ObjectNode) childNode).put(fieldName, String.valueOf(jsonObject));
        ((ObjectNode) rootNode).set("obj", childNode);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
