package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.json.JSONException;
import org.json.JSONObject;

public class MyDatabase {
	
	private static String userName = "root";
	private static String password = "root";
	private static BasicDataSource source;
	
	public MyDatabase(String username, String password){
		MyDatabase.userName=username;
		MyDatabase.password=password;
		MyDatabase.setupDataSource();
		
	}

/*	 public static void main(String[] args) {
		 System.out.println("Setting up data source.");
		 DataSource dataSource = setupDataSource();
		 System.out.println("Done.");


			 System.out.println("Register.");
			 double lat = 41.130614;
			 double lng = -8.643039;
			 System.out.print(gps2m(lat, lng, lat, lng));
			 JSONObject js = getNearbyPlaces(lat, lng, 10000);
			 System.out.print(js.toString());
			 
	 }*/
	
	 public static DataSource setupDataSource(){

		 source = new BasicDataSource();
		 source.setDriverClassName("com.mysql.jdbc.Driver");
		 source.setUsername(userName);
		source.setPassword(password);
		source.setUrl("jdbc:mysql://localhost:3306/sia");
		return source;
	}
	 
	 private static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
		    double pk = (double) (180/3.14169);

		    double a1 = lat_a / pk;
		    double a2 = lng_a / pk;
		    double b1 = lat_b / pk;
		    double b2 = lng_b / pk;

		    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
		    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
		    double t3 = Math.sin(a1)*Math.sin(b1);
		    double tt = Math.acos(t1 + t2 + t3);

		    return 6366000*tt;
		}
	 	    
	/**
	 * Function to return every nearby places of a point within a certain radius
	 * @param lat the current GPSCoordinates latitude
	 * @param lng the current GPSCoordinates longitude
	 * @param radius in meters from where to search
	 * @return JSONObject nbPlaces with all the GPSCoordinates of our points
	 */
	public static JSONObject getNearbyPlaces(double lat, double lng, double radius){
		
		JSONObject nbPlaces = new JSONObject();
		
		PreparedStatement stmt = null;
		Connection con = null;
		ResultSet rset = null;
		
		try{
			con = source.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Places");
			
			rset = stmt.executeQuery();
			
			double latitude;
			double longitude;
			String info;
			int count = 0;
			
			JSONObject list = new JSONObject();
			while(rset.next()){
				latitude = rset.getDouble("lat");
				longitude = rset.getDouble("lng");
				info = rset.getString("info");
				if(gps2m(latitude, longitude, lat, lng) <= radius && gps2m(latitude, longitude, lat, lng) != 0){
					list = new JSONObject();
					list.put("lat", Double.toString(lat));
					list.put("lng",Double.toString(lng));
					list.put("info",info);
					nbPlaces.put(Integer.toString(count), list);
					count++;
				}
			}	
			return nbPlaces;
		
		}
		catch(SQLException e){
			 e.printStackTrace();
		 } catch (JSONException e) {
			e.printStackTrace();
		} finally {
			 try { if (rset != null) rset.close(); } catch(Exception e) { }
			 try { if (stmt != null) stmt.close(); } catch(Exception e) { }
			 try { if (con != null) con.close(); } catch(Exception e) { }
			 
		 }
		
		return null;
		
	}

	public static JSONObject getInfo(double lat, double lng){
		
		JSONObject info = new JSONObject();

		PreparedStatement stmt = null;
		Connection con = null;
		ResultSet rset = null;

		try{
		con = source.getConnection();
		stmt = con.prepareStatement("SELECT * FROM Places WHERE lat = ? AND lng = ?");
		stmt.setDouble(1, lat);
		stmt.setDouble(2, lng);
		
		rset = stmt.executeQuery();
		rset.next();
		JSONObject pointInfo = new JSONObject();

		pointInfo.put("info", rset.getString("info"));
		info.put(lat + " " + lng, pointInfo);
	
		return info;
		
		
		}
		catch(SQLException e){
			 e.printStackTrace();
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 try { if (rset != null) rset.close(); } catch(Exception e) { }
			 try { if (stmt != null) stmt.close(); } catch(Exception e) { }
			 try { if (con != null) con.close(); } catch(Exception e) { }
			 
		 }
		return null;
		
	}
	
	public static boolean register(double lat, double lng, String info){
		PreparedStatement stmt = null;
		Connection con = null;
		ResultSet rset = null;
		try{
		con = source.getConnection();
		stmt = con.prepareStatement("INSERT INTO Places (lat,lng,info) "
				+ "VALUES (?,?,?)");
		stmt.setDouble(1, lat);
		stmt.setDouble(2, lng);
		stmt.setString(3, info);
		
		int i = stmt.executeUpdate();
		if(i>0) return true;
		else return false;
		}
		catch(SQLException e){
			 e.printStackTrace();
		 } finally {
			 try { if (rset != null) rset.close(); } catch(Exception e) { }
			 try { if (stmt != null) stmt.close(); } catch(Exception e) { }
			 try { if (con != null) con.close(); } catch(Exception e) { }
			 
		 }
		return false;
	}
	
}