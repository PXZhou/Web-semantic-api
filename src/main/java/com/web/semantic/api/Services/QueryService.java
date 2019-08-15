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

    public JSONObject  getAllStop() throws FileNotFoundException {
        String sparql = prefix_rdfs  + "\n" +
                prefix_stations + "\n" +
                "\n" +
                "SELECT distinct ?label \n" +
                "WHERE {\n" +
                "  ?ex rdfs:label ?label\n" +
                "}\n";

        QueryExecution qe = getResultSet(sparql, "data/ttl/stops.ttl");
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

    public JSONObject getInformationFromStop(String nameStop) throws FileNotFoundException {
        String sparql =  prefix_rdfs + "\n" +
                prefix_geo + "\n" +
                "SELECT distinct ?lat ?long ?stop\n" +
                "WHERE {\n" +
                "  ?ex rdfs:label ?label.\n" +
                "  ?ex geo:lat ?lat.\n" +
                "  ?ex geo:long ?long.\n" +
                "  ?ex rdfs:stop ?stop\n" +
                "  FILTER (?label =" + nameStop + "@fr)\n" +
                "}";
        QueryExecution qe = getResultSet(sparql, "data/ttl/stops.ttl");
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

    public JSONObject getRouteFromStop(String stopId) throws FileNotFoundException {

        QueryExecution qe = getResultSet(queryStopTimesByStopId(stopId), "data/ttl/stop_times.ttl");
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();

        String routeId = null;
        if (rs.hasNext()) {
            QuerySolution qs = rs.next();
            QueryExecution qeTrip = getResultSet(queryTripById(qs.getLiteral("trip").getLexicalForm()), "data/ttl/trips.ttl");
            ResultSet rsTrip = qeTrip.execSelect();

            if (rsTrip.hasNext()) {
                QuerySolution qsTrip = rsTrip.next();
                routeId = qsTrip.getLiteral("route").getLexicalForm();
            }
            qeTrip.close();
        }

        qe.close();

        JSONObject informationRoutes = new JSONObject();

        qe = getResultSet(queryRoutesById(routeId), "data/ttl/routes.ttl");

        rs = qe.execSelect();
        if (rs.hasNext()) {
            QuerySolution qs = rs.next();
            informationRoutes.put("agency", qs.getLiteral("agency").getLexicalForm());
            informationRoutes.put("label", qs.getLiteral("label").getLexicalForm());
            informationRoutes.put("id", qs.getLiteral("route_id").getLexicalForm());
        }
        details.put("Routes", informationRoutes);
        return details;
    }

    public JSONObject getAllInformationFromTrip(String tripId) throws FileNotFoundException {
        QueryExecution qeStopTimes = getResultSet(queryStopTimesByTripId(tripId), "data/ttl/stop_times.ttl");
        ResultSet rsTrip = qeStopTimes.execSelect();

        JSONObject details = new JSONObject();
        ArrayList<JSONObject> listInformationStopTimes = new ArrayList<>();

        while (rsTrip.hasNext()) {
            QuerySolution qsTrip = rsTrip.next();
            JSONObject informationStopTimes = new JSONObject();
            informationStopTimes.put("sequence", qsTrip.getLiteral("sequence").getLexicalForm());
            informationStopTimes.put("arrival", qsTrip.getLiteral("arrival").getLexicalForm());
            informationStopTimes.put("departure", qsTrip.getLiteral("departure").getLexicalForm());
            listInformationStopTimes.add(informationStopTimes);
        }
        qeStopTimes.close();
        details.put("stop_times", listInformationStopTimes);
        return details;
    }

    public JSONObject getAllInformationFromRoutes(String routeId) throws FileNotFoundException {

        QueryExecution qe = getResultSet(queryTripByRoutes(routeId), "data/ttl/trips.ttl");
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();
        ArrayList<JSONObject> listInformation = new ArrayList<>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            JSONObject information = new JSONObject();
            information.put("direction", qs.getLiteral("direction").getLexicalForm());
            information.put("service", qs.getLiteral("service").getLexicalForm());
            information.put("trip_id", qs.getLiteral("trip_id").getLexicalForm());

//            QueryExecution qeStopTimes = getResultSet(queryStopTimesByTripId(qs.getLiteral("trip_id").getLexicalForm()), "data/ttl/stop_times.ttl");
//            ResultSet rsTrip = qeStopTimes.execSelect();
//
//            ArrayList<JSONObject> listInformationStopTimes = new ArrayList<>();
//
//            while (rsTrip.hasNext()) {
//                QuerySolution qsTrip = rsTrip.next();
//                JSONObject informationStopTimes = new JSONObject();
//                informationStopTimes.put("sequence", qsTrip.getLiteral("sequence").getLexicalForm());
//                informationStopTimes.put("arrival", qsTrip.getLiteral("arrival").getLexicalForm());
//                informationStopTimes.put("departure", qsTrip.getLiteral("departure").getLexicalForm());
//                listInformationStopTimes.add(informationStopTimes);
//            }
//            qeStopTimes.close();
//            information.put("stop_times", listInformationStopTimes);
            listInformation.add(information);
        }
        qe.close();
        details.put("Trips", listInformation);
        return details;
    }
    private QueryExecution getResultSet(String sparql, String path) throws FileNotFoundException {
        Query qry = QueryFactory.create(sparql);
        Model model= ModelFactory.createDefaultModel();
        model.read(new FileInputStream(path),null, "ttl");
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        return qe;
    }

    private String queryRoutesById(String routeId) {
        return  prefix_rdfs + "\n" +
                prefix_routes + "\n" +
                "SELECT Distinct ?agency ?label ?route_id \n" +
                "WHERE {\n" +
                "  ?route rdfs:agency ?agency.\n" +
                "  ?route rdfs:label ?label.\n" +
                "  ?route rdfs:route ?route_id\n" +
                "  FILTER (?route_id =\"" + routeId + "\")\n" +
                "}" ;
    }

    private String queryTripById(String tripId) {
        return  prefix_rdfs + "\n" +
                prefix_trips + "\n" +
                "SELECT Distinct ?direction ?service ?route\n" +
                "WHERE {\n" +
                "  ?trip rdfs:direction ?direction.\n" +
                "  ?trip rdfs:service ?service.\n" +
                "  ?trip rdfs:route ?route.\n" +
                "  ?trip rdfs:trip ?trip_id\n" +
                "  FILTER (?trip_id =\"" + tripId + "\")\n" +
                "} ORDER BY ASC(?direction)" ;
    }

    private String queryTripByRoutes(String routeId) {
        return  prefix_rdfs + "\n" +
                prefix_trips + "\n" +
                "SELECT Distinct ?direction ?service ?trip_id\n" +
                "WHERE {\n" +
                "  ?trip rdfs:direction ?direction.\n" +
                "  ?trip rdfs:service ?service.\n" +
                "  ?trip rdfs:route ?route.\n" +
                "  ?trip rdfs:trip ?trip_id\n" +
                "  FILTER (?route =" + routeId + ")\n" +
                "} ORDER BY ASC(?direction)  ASC(?service)" ;
    }

    private String queryStopTimesByStopId(String stopId) {
        return prefix_rdfs + "\n" +
                prefix_timestamp + "\n" +
                prefix_stop_times + "\n" +
                "SELECT Distinct ?sequence ?trip ?arrival ?departure\n" +
                "WHERE {\n" +
                "  ?stop_times rdfs:stop-sequence ?sequence.\n" +
                "  ?stop_times rdfs:trip ?trip.\n" +
                "  ?stop_times timestamp:arrival_time ?arrival.\n" +
                "  ?stop_times timestamp:departure_time ?departure.\n" +
                "  ?stop_times rdfs:stop ?stop\n" +
                "  FILTER (?stop =" + stopId + ")\n" +
                "}\n ORDER BY ASC(?sequence) ASC(?arrival) " ;
    }

    private String queryStopTimesByTripId(String tripId) {
        return prefix_rdfs + "\n" +
                prefix_timestamp + "\n" +
                prefix_stop_times + "\n" +
                "SELECT Distinct ?sequence ?arrival ?departure\n" +
                "WHERE {\n" +
                "  ?stop_times rdfs:stop-sequence ?sequence.\n" +
                "  ?stop_times rdfs:trip ?trip.\n" +
                "  ?stop_times timestamp:arrival_time ?arrival.\n" +
                "  ?stop_times timestamp:departure_time ?departure.\n" +
                "  FILTER (?trip ="+ tripId + ")\n" +
                "} ORDER BY ASC(?sequence) ASC(?arrival) " ;
    }
}
