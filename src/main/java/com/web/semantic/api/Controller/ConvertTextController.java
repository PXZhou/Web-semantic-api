package com.web.semantic.api.Controller;

import com.web.semantic.api.Services.ConvertTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConvertTextController {

    @Autowired
    ConvertTextService convertTextService;

    @RequestMapping(value = "convert-text", method = RequestMethod.GET)
    public ResponseEntity<?>  createFiles() {
        convertTextService.createFileAgency();
        convertTextService.createFileCalendarDates();
        convertTextService.createFileRoutes();
        convertTextService.createFileStops();
        convertTextService.createFileStopTimes();
        convertTextService.createFileTrip();
        convertTextService.createCalendar();
        return ResponseEntity.accepted().body("Create");
    }

}
