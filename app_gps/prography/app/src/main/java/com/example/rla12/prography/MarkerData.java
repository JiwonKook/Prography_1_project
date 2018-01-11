package com.example.rla12.prography;

/**
 * Created by rla12 on 2018-01-07.
 */

public class MarkerData {

        private double lon;
        private double lat;
        String name;
        // 이후에 추가 필요

        /*public MarkerData(double lat, double lon, String stname){
            this.lat = lat;
            this.lon = lon;
            this.name = stname;
        }*/
        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

}
