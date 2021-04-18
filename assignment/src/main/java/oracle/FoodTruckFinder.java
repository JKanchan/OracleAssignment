package oracle;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoodTruckFinder {
	public static void main(String[] args) {
		try {
			Date d = new Date();
			int dayOrder = d.getDay();  //Current Day of the week

			URL url = new URL("https://data.sfgov.org/resource/jjew-r69b.json?$order=applicant&dayorder="+dayOrder);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			int currentTime = d.getHours();		
			SimpleDateFormat date12Format = new SimpleDateFormat("hha");
			SimpleDateFormat date24Format = new SimpleDateFormat("HH");
			
			System.out.println("Name"+"                "+"Address");
			while ((line = rd.readLine()) != null)
			{
				// Formatting of the string for proper JSON parsing
				if(line.charAt(0)=='[')
					line = line+']';
				else if(line.charAt(0)==',')
					line = '['+line.substring(1)+']';
				else if(line.charAt(line.length()-1)==']')
					line = line+'[';
				else
					line = '['+line+']';

				JSONArray array = new JSONArray(line);  
				for(int i=0; i < array.length(); i++)   
				{  
					JSONObject object = array.getJSONObject(i);  
					String Starttime = object.getString("starttime");
					String Endtime = object.getString("endtime");
					//Filter the result based on current time 
					if(String.valueOf(currentTime).compareTo(date24Format.format(date12Format.parse(Starttime)))>=1 && 
							String.valueOf(currentTime).compareTo(date24Format.format(date12Format.parse(Endtime)))<0)
					{		
						System.out.println(object.getString("applicant")+"         "+object.getString("location"));
					}
				}  
			}
			rd.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}



