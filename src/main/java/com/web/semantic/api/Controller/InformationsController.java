package com.web.semantic.api.Controller;

import com.web.semantic.api.Services.JsonService;
import com.web.semantic.api.Services.QueryService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class InformationsController {

    @Autowired
    JsonService jsonService;
    @Autowired
    QueryService queryService;

    @RequestMapping(value = "information/weather", method = RequestMethod.GET)
    public ResponseEntity<?> getInformationWeather(@RequestParam("latitude") Double latitude, @RequestParam ("longitude") Double longitude) throws IOException {
        JSONObject weather = jsonService.getInformationsWeather(latitude,longitude);
        if (weather == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.accepted().body(jsonService.JsonObjectToString("weather", weather));
        }
    }

    @RequestMapping(value = "stops/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStops() throws FileNotFoundException {
        queryService.getAllStop();
        return ResponseEntity.accepted().body("OK");
    }

    @RequestMapping(value = "information/city", method = RequestMethod.GET)
    public ResponseEntity<?> getInformationCity(@RequestParam("latitude") Double latitude, @RequestParam ("longitude") Double longitude) throws IOException {
        JSONObject city = jsonService.getInformationsCity(latitude,longitude);
        if (city == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.accepted().body(jsonService.JsonObjectToString("weather", city));
        }
    }
}
