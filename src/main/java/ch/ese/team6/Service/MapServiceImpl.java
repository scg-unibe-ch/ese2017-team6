package ch.ese.team6.Service;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;

@Service
public class MapServiceImpl implements MapService{
	/*
	@Override
	public boolean checkAddress(Address address) {
		// TODO Auto-generated method stub
		return false;
	}
	"https://maps.googleapis.com/maps/api/distancematrix/json?origins={origin}&destinations={destination}&key="
	*/
	private final String APIKEY = "AIzaSyAb-SCpWFMSIO0OeVAES-xWjRfLAIwzv7g&amp";
	
	@Override
	public boolean checkAddress(Address address) throws InvalidAddressException {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("address", address.toString());
		String result = restTemplate.getForObject("https://maps.googleapis.com/maps/api/geocode/json?address={address}&key="+APIKEY, String.class, uriVariables);
		JSONObject data;
		try {
			data = new JSONObject(result);
			System.out.println(data.getString("status"));
			if(data.getString("status").equals("OK")) return true;
			if(data.getString("status").equals("ZERO RESULTS")) {
				throw new InvalidAddressException("Google can't find Address.");
				}
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		System.out.println(result);
		return false;
		
	}
	
	public long calculateDistance(Address origin, Address destination) throws InvalidAddressException {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("origin", origin.toString());
		uriVariables.put("destination", destination.toString());
		String result = restTemplate.getForObject("https://maps.googleapis.com/maps/api/distancematrix/json?origins={origin}&destinations={destination}&key="+APIKEY, String.class, uriVariables);
		System.out.println("#########################################################################");
		System.out.println(result);
		JSONObject data;
		try {
			data= new JSONObject(result);
			System.out.println("Created JSONObject.");
			System.out.println(data.getString("origin_addresses"));
			JSONArray rows = (JSONArray) data.get("rows");
			System.out.println(rows.toString());
			System.out.println("_____________________");
			JSONObject elements = rows.getJSONObject(0);
			System.out.println(elements.toString());
			System.out.println("_____________________");
			JSONArray element = elements.getJSONArray("elements");
			System.out.println(element.toString());
			System.out.println("_____________________");
			JSONObject distanceComplete = element.getJSONObject(element.length()-1);
			System.out.println(distanceComplete);
			JSONObject duration = distanceComplete.getJSONObject("duration");
			System.out.println(duration);
			System.out.println(duration.getString("value"));
			JSONObject distance = distanceComplete.getJSONObject("distance");
			System.out.println(distance);
			System.out.println(distance.getString("value"));
			long distanceInMetres = Long.parseLong(distance.getString("value"));
			long distanceInSeconds =Long.parseLong(duration.getString("value"));
			return distanceInSeconds;
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new InvalidAddressException("Google didn't find a distance.");
		}
	}

}

