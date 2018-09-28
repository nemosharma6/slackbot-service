package api;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import util.ApiURL;
import util.Mapper;
import util.RestClient;

@Path("/v1")
public class SlackService {

	@Path("/item")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItems(String msg) 
	{

		JSONObject object = Mapper.map(msg);
		String text = (String) object.get("text");
		String item_name, amount = null;
		if (text.contains("<")) 
		{
			String arr[] = text.split("<");
			item_name = arr[0];
			amount = arr[1].substring(1);
		}
		else
			item_name = text;

		RestClient restClient = RestClient.getInstance();
		JSONObject jsonObject = null;
		try {
			jsonObject = (amount == null) ? restClient.get(String.format(ApiURL.ITEM, item_name))
					: restClient.get(String.format(ApiURL.ITEM_PRICE, item_name, amount));
		} catch (ClientProtocolException e) {
			System.out.println("Error");
		} catch (IOException e) {
			System.out.println("Error");
		}

		if(jsonObject.has("message")){
			return Response.status(200).entity("No Such Item Found").build();
		}
		
		String result = "";
		int count = jsonObject.getInt("numItems");
		int i = 0;
		JSONArray jsonArray = jsonObject.getJSONArray("items");
		String pattern = "Id: %s, Name: %s, Price: $%s, Stock: %s";

		while (i < count) 
		{
			JSONObject jObject = jsonArray.getJSONObject(i);
			String id = jsonArray.getJSONObject(0).getBigInteger("itemId").toString();
			String name = jObject.getString("name");
			String price = jsonArray.getJSONObject(0).getBigDecimal("salePrice").toString();
			String stock = jObject.getString("stock");
			result = result + String.format(pattern, id, name, price, stock);
			result = result + "\n";
			i++;
		}

		return Response.status(200).entity(result).build();
	}
	
	@Path("/addToCart")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToCart(String msg)
	{
		JSONObject object = Mapper.map(msg);
		String items = (String) object.get("text");
		String result = null;
		try {
			result = RestClient.getInstance().getTiny(ApiURL.ADD_TO_CART + items);
		} catch (IOException e) {
			System.out.println("Error");
		}
		return Response.status(200).entity(result).build();
	}
	
	@Path("/review")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReviews(String msg)
	{
		String result;
		JSONObject object = Mapper.map(msg);
		String productId = (String) object.get("text");
		RestClient restClient = RestClient.getInstance();
		
		JSONObject jsonObject = null;
		try {
			jsonObject = restClient.get(String.format(ApiURL.REVIEW, productId));
		} catch (ClientProtocolException e) {
			System.out.println("Error");
		} catch (IOException e) {
			System.out.println("Error");
		}
		
		if(jsonObject.has("errors")){
			return Response.status(200).entity("Product-Id provided was Invalid").build();
		}
		
		String item_name = jsonObject.getString("name");
		result = item_name + "\n";
		JSONArray jsonArray = jsonObject.getJSONArray("reviews");
		
		if(jsonArray.length() == 0){
			return Response.status(200).entity("No Reviews were found for " + item_name).build();
		}
		else{
			int i = 0;
			while(i < 5)
			{
				JSONObject jObject = jsonArray.getJSONObject(i);
				if(jObject == null)
					break;
				String comment = jObject.getString("reviewText");
				result = result + comment + "\n";
				i++;
			}
		}
		
		return Response.status(200).entity(result).build();
	}
}
