package com.web.semantic.api.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.web.semantic.api.Modeles.Informations;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonService {

    @Autowired
    QueryService queryService;

    public JSONObject getInformationsWeather(Double latitude, Double longitude) throws IOException {
        Informations infoWeather = new Informations(latitude, longitude);
        return infoWeather.infoWeather();
    }

    public JSONObject getInformationsCity(Double latitude, Double longitude) throws IOException {
        Informations infoCity = new Informations(latitude, longitude);
        return infoCity.infoCity();
    }

    public JSONObject getInformationsDistance(Double latitude, Double longitude, String stopId) throws IOException {
        JSONObject stopOrigin = queryService.getInformationFromStop(stopId);
        Double latitudeOrigin = Double.parseDouble(stopOrigin.get("latitude").toString());
        Double longitudeOrigin = Double.parseDouble(stopOrigin.get("longitude").toString());
        Informations infoDistance = new Informations(latitudeOrigin, longitudeOrigin);
        return infoDistance.infoDistance(latitude,longitude);
    }

    public String JsonObjectToString(String fieldName, JSONObject jsonObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.createObjectNode();

        ((ObjectNode) rootNode).put(fieldName, String.valueOf(jsonObject));
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
