package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.util.parsing.json._
import scala.io.Source
import play.api.libs.functional.syntax._

class Application extends Controller {

  def index = Action {
    Ok("Hello world")
  }

  def test = Action{
  	val jsontext = """{"name": "kakakakakku", "intval": 10000, "doubleval": 1234.5,"location":{"lat":12.53214,"lng":14.612341}}"""	
    val jsValue:JsValue = Json.parse(jsontext)
	

	 val str = """{"name": "miyatin","age": 21,"lang": ["Scala", "C#", "JavaScript", "PHP"],"univ": {"name": "Kobe Univ","major": "Engineering","grade": 4}}"""
	val profile: Profile = Json.parse(str).as[Profile]
	val profileJson = Json.toJson(profile)
	Ok(profileJson)

  }

  def getQuery(name:String,age:Int) = Action{

  	Ok(name+" "+age)
  }

  def getDetail(place_id:String) = Action{
  	val url = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyD--z9mp1nmbN6HYWuugG-YjiUgb9hXFE8"
	val place = "&place_id" + place_id
	val api = url + place
	//val source = Source.fromURL(api,"utf-8")
	//val str = source.getLines.mkString
	//val jsonObject = Json.prettyPrint(Json.parse(str))
	//val jsValue:JsValue = Json.parse(str)
	Ok(url)
  }

  def getArea(latitude:Double,longitude:Double,genre:String) = Action{
    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=YOUR KEY"
    val location = "&location="+latitude+","+longitude
	val types = "&types="+genre
	val rankby = "&rankby=distance"
	val api = url + location + types + rankby
	val source = Source.fromURL(api,"utf-8")
    val str = source.getLines.mkString
    val jsValue:JsValue = Json.parse(str)
	val results = (jsValue \ "results")
    val returnJson = Json.toJson(
      Map(
	    "geometry" -> (jsValue \ "results" \\ "geometry"),
		//"lat" -> (results(0) \ "geometry" \ "location" \ "lat"),
		//"lng" -> (results(0) \ "geometry" \ "location" \ "lng").as[Any],
        "names" -> (jsValue \ "results" \\ "name"),
		//"name" -> (results(0) \ "name")
		"place_id" -> (jsValue \ "results" \\ "place_id"),
		"icon" -> (jsValue \ "results" \\ "icon"),
		"genre" -> (jsValue \ "results" \\ "types"),
		//"price_level" -> (jsValue \ "results" \\ "pricelevel"),
		//"rating" -> (jsValue \ "results" \\ "rating"),
		"reference" -> (jsValue \ "results" \\ "reference")
      )
    )
	println(Json.prettyPrint(returnJson))
	Ok(Json.prettyPrint(returnJson)).as("application/json; charset=utf-8")


  }

}
