package com.web.semantic.api.Services;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;



@Service
public class QueryService {

    private final String prefix_rdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    private final String prefix_stations = "PREFIX ex:    <http://example.com/stations/>";
    private final String prefix_geo = "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>";
    private final String prefix_timestamp = "PREFIX timestamp: <http://www.w3.org/2001/XMLSchema#timestamp>";
    private final String prefix_trips = "PREFIX trip:  <http://example.com/trips/>";
    private final String prefix_stop_times = "PREFIX stop_times: <http://example.com/stop-times/>";
    private final String prefix_routes = "PREFIX route: <http://example.com/routes/>";
    private final String prefix_calendar = "PREFIX calendar: <http://example.com/calendar/>";

    public JSONObject  getAllStop() throws FileNotFoundException {
        String[] ListPath = new String[] {"data/ttl/stops.ttl"};
        String sparql = prefix_rdfs  + "\n" +
                prefix_stations + "\n" +
                "\n" +
                "SELECT distinct ?label \n" +
                "WHERE {\n" +
                "  ?ex rdfs:label ?label\n" +
                "}\n";

        QueryExecution qe = getResultSet(sparql, ListPath);
        ResultSet rs = qe.execSelect();
        JSONObject jsonObject = new JSONObject();
        ArrayList<String> listNameStops = new ArrayList<String>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            listNameStops.add(qs.getLiteral("label").getLexicalForm());
        }
        jsonObject.put("Name Stops", listNameStops);
        qe.close();
        return jsonObject;
    }

    public JSONObject getCalendarFromService(String serviceId) throws  FileNotFoundException {
        String[] ListPath = new String[] {"data/ttl/calendar.ttl"};

        QueryExecution qe = getResultSet(queryCalendarFromService(serviceId), ListPath);
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            details.put("days", qs.getLiteral("days").getLexicalForm());
            details.put("start_date", qs.getLiteral("start_date").getLexicalForm());
            details.put("end_date", qs.getLiteral("end_date").getLexicalForm());
        }
        qe.close();
        return details;
    }

    public JSONObject getInformationFromStop(String param) throws FileNotFoundException {
        String sparql =  prefix_rdfs + "\n" +
                prefix_geo + "\n" +
                "SELECT distinct ?lat ?long ?stop\n" +
                "WHERE {\n" +
                "  ?ex rdfs:label ?label.\n" +
                "  ?ex geo:lat ?lat.\n" +
                "  ?ex geo:long ?long.\n" +
                "  ?ex rdfs:stop ?stop\n" +
                "  FILTER (?label =" + param + "@fr || ?stop= "+ param+
                ")\n" +
                "}";
        String[] ListPath = new String[] {"data/ttl/stops.ttl"};
        QueryExecution qe = getResultSet(sparql, ListPath);
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            details.put("latitude", qs.getLiteral("lat").getLexicalForm());
            details.put("longitude", qs.getLiteral("long").getLexicalForm());
            details.put("stop_id", qs.getLiteral("stop").getLexicalForm());
        }
        qe.close();
        return details;
    }

    public JSONObject getStopTimesFromStop(String stopId) throws FileNotFoundException {
        String[] ListPath = new String[] {"data/ttl/stop_times.ttl", "data/ttl/trips.ttl"};
        QueryExecution qe = getResultSet(queryStopTimesByStopId(stopId), ListPath);
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();
        ArrayList<JSONObject> listInformation = new ArrayList<>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            JSONObject oneStopTime = new JSONObject();
            oneStopTime.put("arrival", qs.getLiteral("arrival").getLexicalForm());
            oneStopTime.put("departure", qs.getLiteral("departure").getLexicalForm());
            oneStopTime.put("trip", qs.getLiteral("trip_id").getLexicalForm());
            oneStopTime.put("service", qs.getLiteral("service").getLexicalForm());
            listInformation.add(oneStopTime);
        }
        details.put("stop_times", listInformation);
        return details;
    }

    public JSONObject getRouteFromStop(String stopId) throws FileNotFoundException {

        String[] ListPath = new String[] {"data/ttl/stop_times.ttl", "data/ttl/trips.ttl", "data/ttl/routes.ttl"};

        JSONObject details = new JSONObject();

        QueryExecution qe = getResultSet(queryRoutesFromStopId(stopId), ListPath);
        System.out.println(queryRoutesFromStopId(stopId));
        ArrayList<JSONObject> listInformation = new ArrayList<>();
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            JSONObject informationRoutes = new JSONObject();
            informationRoutes.put("agency", qs.getLiteral("agency").getLexicalForm());
            informationRoutes.put("label", qs.getLiteral("label").getLexicalForm());
            informationRoutes.put("id", qs.getLiteral("route_id").getLexicalForm());
            listInformation.add(informationRoutes);
        }
        details.put("Routes", listInformation);
        qe.close();
        return details;
    }

    public JSONObject getAllInformationFromTrip(String tripId) throws FileNotFoundException {

        String[] ListPath = new String[] {"data/ttl/stop_times.ttl", "data/ttl/stops.ttl", "data/ttl/trips.ttl"};
        QueryExecution qeStopTimes = getResultSet(queryStopAndStopTimesFromTripId(tripId), ListPath);
        ResultSet rsTrip = qeStopTimes.execSelect();

        JSONObject details = new JSONObject();
        ArrayList<JSONObject> listInformationStopTimes = new ArrayList<>();

        while (rsTrip.hasNext()) {
            QuerySolution qsTrip = rsTrip.next();
            JSONObject informationStopTimes = new JSONObject();
            informationStopTimes.put("arrival", qsTrip.getLiteral("arrival").getLexicalForm());
            informationStopTimes.put("departure", qsTrip.getLiteral("departure").getLexicalForm());
            informationStopTimes.put("name", qsTrip.getLiteral("label").getLexicalForm());
            informationStopTimes.put("longitude", qsTrip.getLiteral("longitude").getLexicalForm());
            informationStopTimes.put("latitude", qsTrip.getLiteral("latitude").getLexicalForm());
            listInformationStopTimes.add(informationStopTimes);
        }
        qeStopTimes.close();
        details.put("stop_times", listInformationStopTimes);
        return details;
    }

    private QueryExecution getResultSet(String sparql, String[] listPath) throws FileNotFoundException {
        Query qry = QueryFactory.create(sparql);
        Model model= ModelFactory.createDefaultModel();
        for (int i = 0; i <listPath.length; i++) {
            model.read(new FileInputStream(listPath[i]),null, "ttl");
        }
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        return qe;
    }

    private String queryStopTimesByStopId(String stopId) {
        return prefix_rdfs + "\n" +
                prefix_timestamp + "\n" +
                prefix_stop_times + "\n" +
                prefix_trips + "\n" +
                "SELECT Distinct ?service ?trip_id ?arrival ?departure\n" +
                "WHERE {\n" +
                "  ?stop_times rdfs:trip ?trip_id.\n" +
                "  ?trip rdfs:trip ?trip_id.\n" +
                "  ?trip rdfs:service ?service.\n" +
                "  ?stop_times timestamp:arrival_time ?arrival.\n" +
                "  ?stop_times timestamp:departure_time ?departure.\n" +
                "  ?stop_times rdfs:stop ?stop\n" +
                "  FILTER contains(?stop," + stopId + ")\n" +
                "}\n ORDER BY ASC(?arrival) " ;
    }

    private String queryRoutesFromStopId(String stopId) {
        return  prefix_rdfs +"\n" +
                prefix_trips + "\n" +
                prefix_stop_times + "\n" +
                prefix_timestamp + "\n" +
                prefix_routes + "\n" +
                "SELECT Distinct ?label ?route_id ?agency\n" +
                "WHERE {\n" +
                "  ?stop_times rdfs:stop ?stop.\n" +
                "  ?stop_times rdfs:trip ?trip_id.\n" +
                "  ?trip rdfs:trip ?trip_id.\n" +
                "  ?trip rdfs:route ?route_id.\n" +
                "  ?route rdfs:route ?route_id.\n" +
                "  ?route rdfs:label ?label.\n" +
                "  ?route rdfs:agency ?agency\n" +
                "  FILTER contains(?stop,"+ stopId +")\n" +
                "}";
    }

    private String queryStopAndStopTimesFromTripId(String tripId) {
        return  prefix_rdfs + "\n" +
                prefix_stop_times + "\n" +
                prefix_timestamp + "\n" +
                prefix_geo + "\n" +
                prefix_routes +"\n" +
                prefix_trips + "\n" +
                "SELECT Distinct ?label ?longitude ?latitude ?arrival ?departure ?service\n" +
                "WHERE {\n" +
                "  ?stop_times rdfs:stop ?stop_id.\n" +
                "  ?stop_times rdfs:trip ?trip_id.\n" +
                "  ?trip rdfs:trip ?trip_id. \n" +
                "  ?trip rdfs:service ?service. \n" +
                "  ?stop_times timestamp:arrival_time ?arrival.\n" +
                "  ?stop_times timestamp:departure_time ?departure.\n" +
                "  ?ex rdfs:stop ?stop_id.\n" +
                "  ?ex geo:long ?longitude.\n" +
                "  ?ex geo:lat ?latitude.\n" +
                "  ?ex rdfs:label ?label.\n" +
                "  FILTER (?trip_id = " + tripId + ")\n" +
                "} ORDER BY ASC(?arrival)\n";
    }

    private String queryCalendarFromService(String serviceId) {
        return  prefix_rdfs + "\n" +
                prefix_calendar + "\n" +
                "SELECT Distinct ?days ?start_date ?end_date \n" +
                "WHERE {\n" +
                "  ?calendar rdfs:service ?service_id.\n" +
                "  ?calendar rdfs:days ?days.\n" +
                "  ?calendar rdfs:start-date ?start_date.\n" +
                "  ?calendar rdfs:end-date ?end_date\n" +
                "  FILTER (?service_id = "+ serviceId +")" +
                "} ";
    }
}
