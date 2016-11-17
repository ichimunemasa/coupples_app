package controllers

import play.api.libs.json.Json

case class University(name:String,major:String,grade:Int)
object University{
  implicit def jsonWrites = Json.writes[University]
  implicit def jsonReads = Json.reads[University]
}

case class Profile(name:String,age:Int,lang:List[String],univ:University)
object Profile{
  implicit def jsonWrites = Json.writes[Profile]
  implicit def jsonReads = Json.reads[Profile]
}

