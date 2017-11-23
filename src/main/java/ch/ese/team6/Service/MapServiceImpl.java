package ch.ese.team6.Service;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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

}

