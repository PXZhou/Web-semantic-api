# Web Semantic Train Api

Ce projet a été programmé dans le cadre d'un projet scolaire lors de l'année 2019
Développeur : <Patrick.zhou.git@gmail.com>

## Construit avec

* [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Version de Java
* [Spring Boot 2.1.7](https://docs.spring.io/spring-boot/docs/2.1.7.BUILD-SNAPSHOT/reference/html/) - Framework utilisé
* [DataSource](https://ressources.data.sncf.com/explore/dataset/sncf-ter-gtfs/table/) - GTFS SNCF, les données de ce projet date du 15/07/2019
* [Apache Jena 3.12.0](https://jena.apache.org/)
* [Maven Apache 3.6.1](https://maven.apache.org/docs/3.6.1/release-notes.html)

### Api utilisées

* [OpenWeather](https://openweathermap.org/api) - Api météo utilisé
* [GeoCoding](https://developers.google.com/maps/documentation/geocoding/intro) - Api de géolocalisation
* [Distance Matrix](https://developers.google.com/maps/documentation/distance-matrix/start) - Api de distance

## Déploiement en local

Le projet a été dockerizé.  

Le déploiement se fera avec le logiciel *Docker*. 

### Docker

#### Installation de Docker

Pour l'installtion de Docker sur MacOS  : [Lien](https://docs.docker.com/docker-for-mac/install/)

Pour l'installation de Docker sur Windows 10/PRO : [Lien](https://docs.docker.com/docker-for-windows/install/)

Pour l'installation de Docker sur Linux: [Lien](https://www.tutorialspoint.com/docker/installing_docker_on_linux.htm)

Pour l'installation de Docker sur des versions antérieurs de Windows 10, il faut télécharger *Docker Toolbox* : [Lien](https://docs.docker.com/toolbox/toolbox_install_windows/) et [Github](https://github.com/docker/toolbox/releases)

#### Lancement du containeur 

- Ouvrer un terminal 
- Allez dans le dossier courant de ce projet à l'aide du terminal
- Lancez cette commande :
```
$ docker-compose up --build -d
```
- Une fois, le containeur ait fini de construire, vous pouvez aller sur le [localhost](http://localhost:8080/).  Néanmoins, si vous utilisez Docker Toolbox, vous devez ajouter le port dans la [virtual box](https://i.stack.imgur.com/GF4x1.png) : 

| Host IP        | Host Port           | Guest IP  | Guest Port |
| -------------  |:-------------------:| ---------:| ----------:|
| 127.0.0.1      | 8080 | 192.168.99.100 | 8080 |
- Eteindre le containeur
```
$ docker-compose down
```
### Exemple d'urls disponibles

- [Génerer des fichiers Turtle](http://localhost:8080/convert-text)
- [Afficher toutes les gares](http://localhost:8080/stops/all)
- [Information sur la station Gouaux-de-Luchon](http://localhost:8080/stops?stop=%22Gouaux-de-Luchon%22)
- [Information sur un service](http://localhost:8080/information/calendar?service="386")
- etc ...

### Bibliographie

* [Exemple de requetes SPARQL](https://www.wikidata.org/wiki/Wikidata:SPARQL_query_service/queries/examples)
* [Cours des Mines de Saint Etienne](https://www.emse.fr/~zimmermann/Teaching/SemWeb/) 
* [Cours de l'Isep](https://educ.isep.fr/moodle/course/view.php?id=357) 
* [Tutorial de Spring Boot](https://howtodoinjava.com/spring-boot-tutorials/)
* [Information sur le format GTFS](https://developers.google.com/transit/gtfs/)
* [W3 RDF Sparql Query](https://www.w3.org/TR/rdf-sparql-query/)


