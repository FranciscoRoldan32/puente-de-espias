package lugar;

import java.util.Map;
import java.util.HashMap;


public class Zona_Espionaje {
	private Map<String, Coordinates> locations;
	public Zona_Espionaje() {
		this.locations = new HashMap<>();
		generateProvinceArgentina();
	}

  public void generateProvinceArgentina() {
	  locations.put("Buenos Aires", new Coordinates("Buenos Aires", -36.13095816508716, -60.58014633992997));
  }
  public Map<String, Coordinates> getLocations() {
      return locations;
  }
}
