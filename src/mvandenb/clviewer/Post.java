package mvandenb.clviewer;

public class Post {

	private String title, imgUrl, date, city, id, url, map, pic,price;
	
	//data type to hold post info
	public Post(){}
	
	public Post(String title, String imgUrl, String date, String city, String id, String url, String map, String pic, String price){
		this.title = title;
		this.imgUrl = imgUrl;
		this.date = date;
		this.city = city;
		this.id = id;
		this.url = url;
		this.map = map;
		this.pic = pic;
		this.price	= price;
		
	}
	public String getTitle(){
		return title;
	}
	public String getImgUrl(){
		return imgUrl;
	}
	public String getDate(){
		return date;
	}
	public String getCity(){
		return city;
	}
	public String getId(){
		return date;
	}
	public String getUrl(){
		return url;
	}
	public String getPic(){
		return pic;
	}
	public String getMap(){
		return map;
	}
	public String getPrice(){
		return price;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setImgUrl(String url){
		this.imgUrl = url;
	}
	public void setDate(String date){
		this.date = date;
	}
	public void setCity(String city){
		this.city = city;
	}
	public void setId(String id){
		this.id = id;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public void setPic(String pic){
		this.pic = pic;
	}
	public void setMap(String map){
		this.map = map;
	}
	public void setPrice(String price){
		this.price = price;
	}
}
