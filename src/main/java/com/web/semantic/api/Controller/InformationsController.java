package com.web.semantic.api.Controller;


import com.web.semantic.api.Services.JsonService;
import com.web.semantic.api.Services.QueryService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
public class InformationsController {

    @Autowired
    JsonService jsonService;
    @Autowired
    QueryService queryService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "information/weather", method = RequestMethod.GET)
    public ResponseEntity<?> getInformationWeather(@RequestParam("latitude") Double latitude, @RequestParam ("longitude") Double longitude) throws IOException {
        JSONObject weather = jsonService.getInformationsWeather(latitude,longitude);
        if (weather == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.accepted().body(jsonService.JsonObjectToString("data", weather));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "stops/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStops() throws IOException {
        return ResponseEntity.accepted().body(jsonService.JsonObjectToString("data", queryService.getAllStop()));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "stops", method = RequestMethod.GET)
    public ResponseEntity<?> getDetailsStop(@RequestParam("stop") String stop) throws IOException {
        return ResponseEntity.accepted().body(jsonService.JsonObjectToString("data", queryService.getLagLong(stop)));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "information/city", method = RequestMethod.GET)
    public ResponseEntity<?> getInformationCity(@RequestParam("latitude") Double latitude, @RequestParam ("longitude") Double longitude) throws IOException {
        JSONObject city = jsonService.getInformationsCity(latitude,longitude);
        if (city == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.accepted().body(jsonService.JsonObjectToString("data", city));
        }
    }
}
