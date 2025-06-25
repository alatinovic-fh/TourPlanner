var directions = {
  "type" : "FeatureCollection",
  "bbox" : [ 16.364128, 48.22999, 16.366292, 48.231783 ],
  "features" : [ {
    "bbox" : [ 16.364128, 48.22999, 16.366292, 48.231783 ],
    "type" : "Feature",
    "properties" : {
      "segments" : [ {
        "distance" : 305.3,
        "duration" : 219.8,
        "steps" : [ {
          "distance" : 19.2,
          "duration" : 13.9,
          "type" : 11,
          "instruction" : "Head northwest on Denisgasse",
          "name" : "Denisgasse",
          "way_points" : [ 0, 1 ]
        }, {
          "distance" : 65.1,
          "duration" : 46.9,
          "type" : 0,
          "instruction" : "Turn left onto Othmargasse",
          "name" : "Othmargasse",
          "way_points" : [ 1, 2 ]
        }, {
          "distance" : 221.0,
          "duration" : 159.1,
          "type" : 1,
          "instruction" : "Turn right onto Treustraße",
          "name" : "Treustraße",
          "way_points" : [ 2, 9 ]
        }, {
          "distance" : 0.0,
          "duration" : 0.0,
          "type" : 10,
          "instruction" : "Arrive at Treustraße, on the left",
          "name" : "-",
          "way_points" : [ 9, 9 ]
        } ]
      } ],
      "way_points" : [ 0, 9 ],
      "summary" : {
        "distance" : 305.3,
        "duration" : 219.8
      }
    },
    "geometry" : {
      "coordinates" : [ [ 16.366292, 48.230082 ], [ 16.366165, 48.230232 ], [ 16.365365, 48.22999 ], [ 16.365236, 48.230148 ], [ 16.365005, 48.230432 ], [ 16.364415, 48.231112 ], [ 16.364369, 48.231168 ], [ 16.364211, 48.231573 ], [ 16.364134, 48.231768 ], [ 16.364128, 48.231783 ] ],
      "type" : "LineString"
    }
  } ],
  "metadata" : {
    "attribution" : "openrouteservice.org | OpenStreetMap contributors",
    "service" : "routing",
    "timestamp" : 1750814879404,
    "query" : {
      "coordinates" : [ [ 16.366121, 48.230018 ], [ 16.363947, 48.23175 ] ],
      "profile" : "foot-walking",
      "profileName" : "foot-walking",
      "format" : "json"
    },
    "engine" : {
      "version" : "9.3.0",
      "build_date" : "2025-06-06T15:39:25Z",
      "graph_date" : "2025-06-20T02:55:27Z",
      "osm_date" : "1970-01-01T00:00:00Z"
    }
  }
};
