package pictureshow

trait Config { self: IO =>
  /** name of config file */
  def configName = "conf.js"
  /** title of show */
  def showTitle = provided.title.getOrElse("picture show")
  /** list of sections to render */
  def sections = provided.sections.getOrElse("test" :: Nil)
  /** ??? */
  def resourceBase = ""
  
  case class Config(title: Option[String], sections: Option[List[String]])
  private val provided = {
    slurp(configName) map { contents =>
      val json = scala.util.parsing.json.JSON.parse(contents)
      def extract[T](name: String)(f: Any => T) = json flatMap {
        case head :: (k, v) :: Nil if(k == name) => Some(f(v))
        case (k, v) :: tail if(k == name) => Some(f(v))
        case _ => None
      }
      Config(extract("title") { _.toString }, extract("sections") { v => v.asInstanceOf[List[String]] })
    } getOrElse Config(None, None)
  }
}