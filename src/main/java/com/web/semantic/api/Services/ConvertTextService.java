package com.web.semantic.api.Services;

import com.web.semantic.api.Modeles.File;
import org.springframework.stereotype.Service;

@Service
public class ConvertTextService {

    public void createFileAgency() {
        File agency = new File("data\\ttl\\agency.ttl","data\\gtfs\\agency.txt");
        agency.generateAgency();
    }

    public void createFileStopTimes() {
        File stopTimes = new File("data\\ttl\\stop_times.ttl","data\\gtfs\\stop_times.txt");
        stopTimes.generateStopTimes();
    }
    public void createFileStops() {
        File stop = new File("data\\ttl\\stops.ttl","data\\gtfs\\stops.txt");
        stop.generateStop();
    }

    public void createFileTrip() {
        File trips = new File("data\\ttl\\trips.ttl","data\\gtfs\\trips.txt");
        trips.generateTrip();
    }

    public void createFileRoutes() {
        File routes = new File("data\\ttl\\routes.ttl","data\\gtfs\\routes.txt");
        routes.generateRoutes();
    }

    public void createFileCalendarDates() {
        File calendarDates = new File("data\\ttl\\calendar_dates.ttl","data\\gtfs\\calendar_dates.txt");
        calendarDates.generateCalendarDates();
    }
}
