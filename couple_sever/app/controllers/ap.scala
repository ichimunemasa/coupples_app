package controllers

import javax.inject._
import play.api._
import play.api.mvc._

object ap extends Controller {

  def index = Action {
    Ok("Hello world")
  }

}
