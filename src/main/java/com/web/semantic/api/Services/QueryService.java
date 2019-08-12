package com.web.semantic.api.Services;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class QueryService {

    private final String prefix_rdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    private final String prefix_stations = "PREFIX ex:    <http://example.com/stations/>";
    private final String prefix_geo = "<http://www.w3.org/2003/01/geo/wgs84_pos#>";


    public void  getAllStop() throws FileNotFoundException {
        String sparql = prefix_rdfs +
                prefix_stations +
                "SELECT ?label" +
                "WHERE {?ex rdfs:label ?label;} LIMIT 50";
        Query qry = QueryFactory.create(sparql);
        Model model= ModelFactory.createDefaultModel();
        model.read(new FileInputStream("data/ttl/stops.ttl"),null, "ttl");
//        QueryExecution qe = QueryExecutionFactory.create(qry, model);
//        ResultSet rs = qe.execSelect();
    }

    public void getLagLong(String nameStop) throws FileNotFoundException {
        String sparql = prefix_rdfs +
                prefix_stations +
                prefix_geo +
                "SELECT ?lat ?long" +
                "WHERE { ?ex rdfs:label ?label. ?ex geo:lat ?lat. ?ex geo:long ?long FILTER (?label = " + nameStop + "@fr}"
                + "GROUP BY ?label";
        Query qry = QueryFactory.create(sparql);
        Model model= ModelFactory.createDefaultModel();
        model.read(new FileInputStream("data/ttl/stops.ttl"),null, "ttl");
//        QueryExecution qe = QueryExecutionFactory.create(qry, model);
//        ResultSet rs = qe.execSelect();
    }
}
