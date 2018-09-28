package util;

public class ApiURL {

	public static final String apiKey = "6t2zqkd4fe3n5z26wjw758eb";
	public static final String ITEM = "http://api.walmartlabs.com/v1/search?apiKey="+apiKey+"&query=%s&sort=price&order=asc";
	public static final String ITEM_PRICE = "http://api.walmartlabs.com/v1/search?apiKey="+apiKey+"&query=%s&facet=on&facet.range=price:[0 TO %s]&sort=price&order=asc";
	public static final String ADD_TO_CART = "http://affil.walmart.com/cart/buynow?items=%s";
	public static final String REVIEW = "http://api.walmartlabs.com/v1/reviews/%s?apiKey="+apiKey+"&format=json";
}
