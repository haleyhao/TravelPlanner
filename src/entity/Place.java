package entity;

public class Place {
	private String place_id;
	private String name;
	private String formatted_address;
	private double lat;
	private double lon;
	private double rating;
	private int user_ratings_total;
	
	public double getTotalScore() {
		return rating * user_ratings_total;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlaceId() {
		return place_id;
	}
	
	public double getLon() {
		return lon;
	}
	
	public double getLat() {
		return lat;
	}
	
	private Place(PlaceBuilder builder) {
		this.place_id = builder.place_id;
		this.name = builder.name;
		this.rating = builder.rating;
		this.formatted_address = builder.formatted_address;
		this.lat = builder.lat;
		this.lon = builder.lon;
		this.user_ratings_total = builder.user_ratings_total;
		
	}
	
	public static class PlaceBuilder {
		private String place_id;
		private String name;
		private String formatted_address;
		private double lat;
		private double lon;
		private double rating;
		private int user_ratings_total;
		
		public PlaceBuilder setPlace_id(String place_id) {
			this.place_id = place_id;
			return this;
		}

		public PlaceBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public PlaceBuilder setAddress(String formatted_address) {
			this.formatted_address = formatted_address;
			return this;
		}
		
		public PlaceBuilder setLocation(double lat, double lon) {
			this.lat = lat;
			this.lon = lon;
			return this;
		}
		public PlaceBuilder setRating(double rating) {
			this.rating = rating;
			return this;
		}
		public PlaceBuilder setRaingTotal(int user_ratings_total) {
			this.user_ratings_total = user_ratings_total;
			return this;
		}
		
		public Place build() {
			return new Place(this);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((place_id == null) ? 0 : place_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (place_id == null) {
			if (other.place_id != null)
				return false;
		} else if (!place_id.equals(other.place_id))
			return false;
		return true;
	}
}
