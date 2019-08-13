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

    public JSONObject getLagLong(String nameStop) throws FileNotFoundException {
        String sparql =  prefix_rdfs + "\n" +
                prefix_geo + "\n" +
                "SELECT distinct ?lat ?long \n" +
                "WHERE {\n" +
                "  ?ex rdfs:label ?label.\n" +
                "  ?ex geo:lat ?lat.\n" +
                "  ?ex geo:long ?long\n" +
                "  FILTER (?label =" + nameStop + "@fr)\n" +
                "} \n" +
                "Limit 25";
        QueryExecution qe = getResultSet(sparql, "data/ttl/stops.ttl");
        ResultSet rs = qe.execSelect();
        JSONObject details = new JSONObject();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            details.put("latitude", qs.getLiteral("lat").getLexicalForm());
            details.put("longitude", qs.getLiteral("long").getLexicalForm());
        }
        qe.close();
        return details;
    }

    private QueryExecution getResultSet(String sparql, String path) throws FileNotFoundException {
        Query qry = QueryFactory.create(sparql);
        Model model= ModelFactory.createDefaultModel();
        model.read(new FileInputStream(path),null, "ttl");
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        return qe;
    }
}
