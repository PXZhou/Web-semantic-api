package com.web.semantic.api.Modeles;

import org.apache.jena.atlas.csv.CSVParser;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

public class File {

    public static final String _LANG_FR = "fr"; // The language of the station names
    public static final String _OUTPUT_FORMAT = "Turtle"; // The output format
    public static final String _UTF_8 = "UTF-8"; // Character encoding

    // Namespaces of the vocabularies we use
    public static final String _EX_NS = "http://example.com/stations/";
    public static final String _EX_NS_AGENCIES = "http://example.com/agencies/";
    public static final String _EX_NS_TRIPS = "http://example.com/trips/";
    public static final String _EX_NS_STOPS = "http://example.com/stops/";
    public static final String _EX_NS_ROUTES = "http://example.com/routes/";
    public static final String _EX_NS_DIRECTIONS = "http://example.com/directions/";
    public static final String _EX_NS_SERVICES = "http://example.com/services/";
    public static final String _EX_NS_DATES = "http://example.com/dates/";
    public static final String _GEO_NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static final String _RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String _TIMESTAMP_NS = "http://www.w3.org/2001/XMLSchema#timestamp";

    // Prefixes
    public static final String _EX_PREFIX = "ex";
    public static final String _TRIP_PREFIX = "trip";
    public static final String _AGENCY_PREFIX = "agency";
    public static final String _STOP_PREFIX = "stop";
    public static final String _ROUTE_PREFIX = "route";
    public static final String _DIRECTIONS_PREFIX = "direction";
    public static final String _SERVICE_PREFIX = "service";
    public static final String _GEO_PREFIX = "geo";
    public static final String _RDFS_PREFIX = "rdfs";
    public static final String _TIMESTAMP_PREFIX = "timestamp";
    public static final String _DATE_PREFIX = "date";

    // THE IRIs of the geo: terms we are using
    public static final String _GEO_LAT = "http://www.w3.org/2003/01/geo/wgs84_pos#lat";
    public static final String _GEO_LONG = "http://www.w3.org/2003/01/geo/wgs84_pos#long";
    public static final String _GEO_SPATIAL_THING = "http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing";

    public String url_rdf;
    public String url_text;

    public File(String url_rdf, String url_text) {
        this.url_rdf = url_rdf;
        this.url_text = url_text;
    }

    public void generateAgency() {

        Model stationGraph = ModelFactory.createDefaultModel();

        stationGraph.setNsPrefix(_EX_PREFIX,_EX_NS_AGENCIES);
        stationGraph.setNsPrefix(_RDFS_PREFIX,_RDFS_NS);

        Property name = stationGraph.createProperty("Name");
        Property url = stationGraph.createProperty("Url");
        Property timezone = stationGraph.createProperty("Timezone");
        Property lang = stationGraph.createProperty("Langue");

        CSVParser parser = CSVParser.create(this.url_text);

        parser.parse1();

        for(List<String> line : parser) {

            String agency_id = line.get(0);
            String agency_name = line.get(1);
            String agency_url = line.get(2);
            String agency_timezone = line.get(3);
            String agency_lang = line.get(4);

            try {

                String agency_iri = _EX_NS_AGENCIES + URLEncoder.encode(agency_id,_UTF_8);
                Resource agency = stationGraph.createResource(agency_iri);

                agency.addProperty(name,agency_name,_LANG_FR);
                agency.addProperty(url,agency_url);
                agency.addProperty(timezone,agency_timezone);
                agency.addProperty(lang,agency_lang);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // We have to catch I/O exceptions in case the file is not writable
        try {

            // An output stream to save the generated RDF graph
            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            // Save the RDF graph to a file
            stationGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateStopTimes(){
        Model stopTimesGraph = ModelFactory.createDefaultModel();

        stopTimesGraph.setNsPrefix(_TRIP_PREFIX,_EX_NS_TRIPS);
        stopTimesGraph.setNsPrefix(_STOP_PREFIX,_EX_NS_STOPS);
        stopTimesGraph.setNsPrefix(_RDFS_PREFIX,_RDFS_NS);
        stopTimesGraph.setNsPrefix(_TIMESTAMP_PREFIX, _TIMESTAMP_NS);

        CSVParser parser = CSVParser.create(this.url_text);
        parser.parse1();

        Property stop = stopTimesGraph.createProperty(_EX_NS_STOPS);
        Property trip = stopTimesGraph.createProperty(_EX_NS_TRIPS);
        Property stopSequence = stopTimesGraph.createProperty("Stop Sequence");
        int compteur = 0;

        for(List<String> line : parser) {

            String trip_id = line.get(0);
            String arrival_time = line.get(1);
            String departure_time = line.get(2);
            String stop_id = line.get(3);
            String stop_sequence = line.get(4);
            String stop_headsign = line.get(5);
            compteur ++;
            try {
                String stop_time_iri = "http://example.com/stop_times/" + URLEncoder.encode(String.valueOf(compteur),_UTF_8);

                Resource stop_time = stopTimesGraph.createResource(stop_time_iri);

                Property arrival_time_iri = stopTimesGraph.createProperty(_TIMESTAMP_NS +  URLEncoder.encode("arrival_time",_UTF_8));
                Property departure_time_iri = stopTimesGraph.createProperty(_TIMESTAMP_NS +  URLEncoder.encode("departure_time",_UTF_8));

                stop_time.addProperty(trip, trip_id);
                stop_time.addProperty(arrival_time_iri,arrival_time);
                stop_time.addProperty(departure_time_iri,departure_time);
                stop_time.addProperty(stop,stop_id);
                stop_time.addProperty(stopSequence,stop_sequence);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // We have to catch I/O exceptions in case the file is not writable
        try {

            // An output stream to save the generated RDF graph
            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            // Save the RDF graph to a file
            stopTimesGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateStop() {

        // Starts with an empty Jena model
        Model stationGraph = ModelFactory.createDefaultModel();

        // Define prefixes for pretty output
        stationGraph.setNsPrefix(_EX_PREFIX,_EX_NS);
        stationGraph.setNsPrefix(_GEO_PREFIX,_GEO_NS);
        stationGraph.setNsPrefix(_RDFS_PREFIX,_RDFS_NS);

        // We initialise two properties and one resource from the geo: vocabulary
        Resource geo_SpatialThing = stationGraph.createResource(_GEO_SPATIAL_THING);
        Property geo_lat = stationGraph.createProperty(_GEO_LAT);
        Property geo_long = stationGraph.createProperty(_GEO_LONG);

        // Initialise a CSV parser
        CSVParser parser = CSVParser.create(this.url_text);

        // Skip the first line that contains headers
        parser.parse1();

        // For each line, we will generate some triples with the same subject
        for(List<String> line : parser) {

            //Store all what we need in Strings
            String stop_id = line.get(0); // first column
            String stop_name = line.get(1);
            String stop_lat = line.get(3);
            String stop_long = line.get(4);

            // We have to catch exceptions
            try {

                // Make a new IRI for the station described in this line
                String stop_iri = _EX_NS + URLEncoder.encode(stop_id,_UTF_8);

                // Create the resource that corresponds to the station
                Resource stop = stationGraph.createResource(stop_iri);

                // Add a type to the station
                stop.addProperty(RDF.type,geo_SpatialThing);

                // Add a label to the station (in French)
                stop.addProperty(RDFS.label,stop_name,_LANG_FR);

                // Add latitude and longitude of the station
                stop.addProperty(geo_lat,stop_lat, XSDDatatype.XSDdecimal);
                stop.addProperty(geo_long,stop_long,XSDDatatype.XSDdecimal);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // We have to catch I/O exceptions in case the file is not writable
        try {

            // An output stream to save the generated RDF graph
            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            // Save the RDF graph to a file
            stationGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateTrip() {

        // Starts with an empty Jena model
        Model stationGraph = ModelFactory.createDefaultModel();

        stationGraph.setNsPrefix(_TRIP_PREFIX,_EX_NS_TRIPS);
        stationGraph.setNsPrefix("route","http://example.com/routes/");
        stationGraph.setNsPrefix(_SERVICE_PREFIX,_EX_NS_SERVICES);
        stationGraph.setNsPrefix("direction","http://example.com/directions/");
        stationGraph.setNsPrefix(_RDFS_PREFIX,_RDFS_NS);

        CSVParser parser = CSVParser.create(this.url_text);
        parser.parse1();

        Property route = stationGraph.createProperty("http://example.com/routes/");
        Property service = stationGraph.createProperty(_EX_NS_SERVICES);
        Property direction = stationGraph.createProperty("http://example.com/directions/");
        Property headsign = stationGraph.createProperty("headsign");

        for(List<String> line : parser) {

            String route_id = line.get(0);
            String service_id = line.get(1);
            String trip_id = line.get(2);
            String trip_headsign = line.get(3);
            String direction_id = line.get(4);

            try {


                String trip_iri = _EX_NS_TRIPS + URLEncoder.encode(trip_id,_UTF_8);

                Resource trip = stationGraph.createResource(trip_iri);

                trip.addProperty(route, route_id);
                trip.addProperty(service, service_id);
                trip.addProperty(direction, direction_id);
                trip.addProperty(headsign, trip_headsign);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // We have to catch I/O exceptions in case the file is not writable
        try {

            // An output stream to save the generated RDF graph
            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            // Save the RDF graph to a file
            stationGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateRoutes(){

        Model routeGraph = ModelFactory.createDefaultModel();

        routeGraph.setNsPrefix(_ROUTE_PREFIX,_EX_NS_ROUTES);
        routeGraph.setNsPrefix(_AGENCY_PREFIX,_EX_NS_AGENCIES);
        routeGraph.setNsPrefix(_RDFS_PREFIX,_RDFS_NS);

        CSVParser parser = CSVParser.create(this.url_text);

        parser.parse1();

        Property agency = routeGraph.createProperty(_EX_NS_AGENCIES);
        Property routeType = routeGraph.createProperty("Route Type");

        for(List<String> line : parser) {

            String route_id = line.get(0);
            String agency_id = line.get(1);
            String route_long_name = line.get(3);
            String route_type = line.get(5);

            try {
                String route_iri = _EX_NS_ROUTES + URLEncoder.encode(route_id,_UTF_8);

                Resource route = routeGraph.createResource(route_iri);

                route.addProperty(agency, agency_id);
                route.addProperty(RDFS.label,route_long_name);
                route.addProperty(routeType,route_type);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // We have to catch I/O exceptions in case the file is not writable
        try {

            // An output stream to save the generated RDF graph
            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            // Save the RDF graph to a file
            routeGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateCalendarDates(){
        Model calendarDatesGraph = ModelFactory.createDefaultModel();

        calendarDatesGraph.setNsPrefix(_SERVICE_PREFIX,_EX_NS_SERVICES);
        calendarDatesGraph.setNsPrefix(_DATE_PREFIX,_EX_NS_DATES);

        Property exceptionType =  calendarDatesGraph.createProperty("Exception Type");
        Property dates =  calendarDatesGraph.createProperty(_EX_NS_DATES);
        Property services =  calendarDatesGraph.createProperty(_EX_NS_SERVICES);

        CSVParser parser = CSVParser.create(this.url_text);

        parser.parse1();
        int compteur = 0;
        for(List<String> line:parser) {

            String service_id = line.get(0);
            String date = line.get(1);
            String exception_type = line.get(2);
            compteur++;
            try {
                String calendar_dates_iri = _EX_NS_SERVICES + URLEncoder.encode(String.valueOf(compteur),_UTF_8);

                Resource calendar_dates = calendarDatesGraph.createResource(calendar_dates_iri);

                calendar_dates.addProperty(services, service_id);
                calendar_dates.addProperty(dates, date);
                calendar_dates.addProperty(exceptionType,exception_type);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {

            OutputStream output = new FileOutputStream(new java.io.File(this.url_rdf));

            calendarDatesGraph.write(output,_OUTPUT_FORMAT);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
