package geo;

public class MarkerDTO {

        private double lon;
        private double lat;
        String name;
        String content;
        String catagory;
       
        public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getCatagory() {
			return catagory;
		}
		public void setCatagory(String catagory) {
			this.catagory = catagory;
		}
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