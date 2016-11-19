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
	//val jsonObject =   Json.prettyPrint(Json.parse(jsontext))
    val jsValue:JsValue = Json.parse(jsontext)


	 val str = """{"name": "miyatin","age": 21,"lang": ["Scala", "C#", "JavaScript", "PHP"],"univ": {"name": "Kobe Univ","major": "Engineering","grade": 4}}"""
	val profile: Profile = Json.parse(str).as[Profile]
	val profileJson = Json.toJson(profile)
	//Ok("hello")
	Ok(profileJson)

  }

  def getQuery(name:String,age:Int) = Action{

  	Ok(name+" "+age)
  }

  def getDetail(place_id:String) = Action{
  	val url = "https://maps.googleapis.com/maps/api/place/details/json?key=Your API Key"
	  val place = "&placeid=" + place_id
	  val api = url + place
	  val source = Source.fromURL(api,"utf-8")
	  val str = source.getLines.mkString
	  val jsValue:JsValue = Json.parse(str)



    var formatted_phone_number = "Null"
    if(!(jsValue \ "result" \ "formatted_phone_number").isInstanceOf[JsUndefined]){
      formatted_phone_number = (jsValue \ "result" \ "formatted_phone_number").as[String]
    }

    var formatted_address = "Null"
    if(!(jsValue \ "result" \ "formatted_address").isInstanceOf[JsUndefined]){
      formatted_address = (jsValue \ "result" \ "formatted_address").as[String]
    }

    val returnJson = Json.toJson(
      Map[String,String](
        "formatted_phone_number" -> formatted_phone_number,
        "formatted_address" -> formatted_address
      )
    )
    //println(Json.prettyPrint(returnJson))
    Ok(Json.prettyPrint(returnJson)).as("application/json; charset=utf-8")
    //*/
	   //Ok(url)
  }

  def getArea(latitude:Double,longitude:Double,genre:String) = Action{
    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=Your API Key"
    val location = "&location="+latitude+","+longitude
	  val types = "&types="+genre
	  val rankby = "&rankby=distance"
	  val api = url + location + types + rankby
	  val source = Source.fromURL(api,"utf-8")
    val str = source.getLines.mkString
    //println(api)
    val jsValue:JsValue = Json.parse(str)
	  val results = (jsValue \ "results")
    val size = (jsValue \ "results" \\ "name").size
    //println(size)
    //println((jsValue \ "results" \\ "rating").read[Seq[Double]] | Reads.pure(Seq.empty[Option[String]]))
    //println((results(0) \ "rating").read[Double] | Reads.pure(Seq.empty[JsDefined]))
    //println((results(1) \ "rating").getClass)
    //val result1 = (results(1) \ "rating")
    //println(result1.isInstanceOf[JsUndefined])
    //val result16 = (results(16) \ "rating")
    //println(result16.isInstanceOf[JsUndefined])
    //val result15 = (results(15) \ "rating")

    //val buf = scala.collection.mutable.ListBuffer.empty[JsNumber]
    //buf += JsNumber((results(15) \ "rating").as[Double])
    //buf += JsNumber((results(15) \ "rating").as[Double])
    //println(buf)
    //println(buf(0).getClass)
    //println(buf.getClass)
    //println(JsString((results(0) \ "name").as[String]).getClass)
    //val names = scala.collection.mutable.ListBuffer.empty[JsString]
    //names += JsString("oha")
    val rating = scala.collection.mutable.ListBuffer.empty[JsNumber]
    val price_level = scala.collection.mutable.ListBuffer.empty[JsNumber]
    val photo_reference = scala.collection.mutable.ListBuffer.empty[JsString]

    for (i <- 0 to size-1) {
      //ratingの話
      if ((results(i) \ "rating").isInstanceOf[JsUndefined]){
        rating += JsNumber(100)
      }else {
        rating += JsNumber((results(i) \ "rating").as[Double])
      }

      if((results(i) \ "price_level").isInstanceOf[JsUndefined]){
        price_level += JsNumber(100)
      }else{
        price_level += JsNumber(6 - (results(i) \ "price_level").as[Int])
      }


      if(((results(i) \ "photos")).isInstanceOf[JsUndefined]){
        photo_reference += JsString("icon")
      }else{
        photo_reference += JsString((((results(i) \ "photos"))(0) \ "photo_reference").as[String])
      }


      //photo_reference += JsString((((results(i) \ "photos"))(0) \ "photo_reference").as[String])

    }
    //println((results(4) \ "name"))
    //println((((results(4) \ "photos"))(0) \ "photo_reference"))

    //println((jsValue \ "results" \\ "name")(0).getClass)


    //println((jsValue \ "results" \\ "rating"))
    //println((jsValue \ "results" \\ "rating")(0).getClass)
    //println((jsValue \ "results" \\ "rating").getClass)
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
		      "price_level" -> (jsValue \ "results" \\ "pricelevel"),
		      //"rating" -> (jsValue \ "results" \\ "rating"),
          "rating" -> rating,
          "price_level" -> price_level,
          "photo_reference" -> photo_reference
		      //"reference" -> (jsValue \ "results" \\ "reference")
      )
    )
	//println(Json.prettyPrint(returnJson))
	Ok(Json.prettyPrint(returnJson)).as("application/json; charset=utf-8")


  }


/* 妥協策
  def getArea(latitude:Double,longitude:Double,genre:String) = Action{
    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyD--z9mp1nmbN6HYWuugG-YjiUgb9hXFE8"
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
*/

}
