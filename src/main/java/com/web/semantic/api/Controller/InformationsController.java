package com.web.semantic.api.Controller;

import com.web.semantic.api.Services.JsonService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class InformationsController {

    @Autowired
    JsonService jsonService;

    @RequestMapping(value = "information/weather", method = RequestMethod.GET)
    public ResponseEntity<?> getInformationWeather(@RequestParam("latitude") Double latitude, @RequestParam ("longitude") Double longitude) throws IOException {
        JSONObject weather = jsonService.getInformationsWeather(latitude,longitude);
        if (weather == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.accepted().body(jsonService.JsonObjectToString("weather", weather));
        }
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
