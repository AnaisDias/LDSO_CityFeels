package Database;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class SIA implements Runnable {

	HttpExchange httpE;
	
	public SIA(HttpExchange httpE) {
		this.httpE = httpE;
	}
	
	public Map<String, String> queryToMap(String query){
	    Map<String, String> myMap = new HashMap<String, String>();
	    String[] param = query.split("&");
	    
	    for (int i = 0; i < param.length; i++) {
	        String pair[] = param[i].split("=");
	        if (pair.length>1) {
	            myMap.put(pair[0], pair[1]);
	        }else{
	            myMap.put(pair[0], "");
	        }
	    }
	    return myMap;
	}

	private void main() throws JSONException, IOException {
		
		String method = httpE.getRequestMethod();
		System.out.println(method);
		if(!method.equals("GET")){
			System.out.println("Request method invalid!");
			JSONObject js = new JSONObject();
			js.put("success", false);
			js.put("message", "Parameters invalid!");
			String response = js.toString();
			httpE.sendResponseHeaders(200, response.length());
			OutputStream os = httpE.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
	
		Map<String,String> parameters = queryToMap(httpE.getRequestURI().getQuery());
		if(parameters.containsKey("lat") && !parameters.get("lat").isEmpty()){
			String type = parameters.get("lat");
			
			if(type.equals("0")){
				if(parameters.containsKey("lat") && !parameters.get("lat").isEmpty() &&
						parameters.containsKey("lng") && !parameters.get("lng").isEmpty()){
					double lat = Double.parseDouble(parameters.get("lat"));
					double lng = Double.parseDouble(parameters.get("lng"));
					JSONObject js = new JSONObject();
					String response = "";
					js = MyDatabase.getInfo(lat, lng);
					if(js != null){
						response = js.toString();
						
						httpE.sendResponseHeaders(200, response.length());
						OutputStream os = httpE.getResponseBody();
						os.write(response.getBytes());
						os.close();
						return;
					}
				}
			}
			if(type.equals("1")){
				
				
				if(parameters.containsKey("lat") && !parameters.get("lat").isEmpty() &&
						parameters.containsKey("lng") && !parameters.get("lng").isEmpty() &&
						parameters.containsKey("radius") && !parameters.get("radius").isEmpty()){
					double lat = Double.parseDouble(parameters.get("lat"));
					double lng = Double.parseDouble(parameters.get("lng"));
					double radius = Double.parseDouble(parameters.get("radius"));
					JSONObject js = new JSONObject();
					js = MyDatabase.getNearbyPlaces(lat, lng, radius);
					
					if(js != null){
						
							String response = js.toString();
							httpE.sendResponseHeaders(200, response.length());
							OutputStream os = httpE.getResponseBody();
							os.write(response.getBytes());
							os.close();
							return;
						}
						else{
							System.out.println("Nothing found");
							js = new JSONObject();
							js.put("success", true);
							js.put("message", "There is nothing in the database with that name!");
							String response = js.toString();
							httpE.sendResponseHeaders(200, response.length());
							OutputStream os = httpE.getResponseBody();
							os.write(response.getBytes());
							os.close();
							return;
						}
					}
				}
				else{
					System.out.println("Parameters invalid!");
					JSONObject js = new JSONObject();
					js.put("success", false);
					js.put("message", "Parameters invalid!");
					String response = js.toString();
					httpE.sendResponseHeaders(200, response.length());
					OutputStream os = httpE.getResponseBody();
					os.write(response.getBytes());
					os.close();
					return;
			}
				
		}
				
			
			
			
	}
	
	
	public void run() {
		try {
			main();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
