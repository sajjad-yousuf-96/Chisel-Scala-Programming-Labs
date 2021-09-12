package labscala2
object Main {

  object Demonstration {
    def demonstrate() = {
      val some: Option[Int] = Some(7)

      println("[OPTION] : isDefined & get (bad practice)")
      if (some.isDefined) {
        println(s"  -- very bad way way to extract value from ${some} : ${some.get}")
      }

      println("[OPTION] : map & foreach")
      val multiplyByTwo: Int => Int = i => i * 2 //is this function pure ?
      val display: Int => String = i => s"  -- RESULT : $i" // does this function have any knowledge about context?

      some.map(multiplyByTwo).foreach(i => println(s"  -- multiplied by two : $i"))
      some.map(multiplyByTwo).map(display).foreach(println)

      println("[OPTION] : filter & getOrElse & orElse")
      val default = some.filter(_ > 10).getOrElse(-1)
      println(s"  -- default value $default")
      some.filter(_ > 10).orElse(Some(-1)).map(i => s" -- filter(remove) & orElse -1 : i=$i").foreach(println)
      some.filter(_ < 10).orElse(Some(-1)).map(i => s" -- filter & orElse : i=$i").foreach(println)

      println("[OPTION] : pattern matching")
      some match {
        case Some(value) => println(s"  -- pattern matching value : $value")
        case None => println(s"  --  pattern matching is empty")
      }

      println("[OPTION] : fold")
      val onSuccess: Int => String = i => s"business value $i"
      val onError: String = "raise an error"
      val result = some.fold(onError)(onSuccess)
      println(s"  -- fold result : $result")
    }
  }

  object Exercise {

    object MeetupDomain {

      case class User(id: Int, name: String, email: String)

    }

    object UsersDAO {

      import MeetupDomain._
      private val database = Map(1 -> User(1, "MSY", "sajjad.yousuf.96@gmail.com"))
      println(s"database: $database")
      def find(id: Int): Option[User] = database.get(id)
    }

    object Conversions {

      import FrontEnd._
      import MeetupDomain._

      def userToHTML(u: User): HTML = s"""<a href="mailto:${u.email}">${u.name}</a>"""
    }

    object FrontEnd {
      type HTML = String
      def displayPage(body: String): HTML =s"""<html><body>$body</body></html>"""
      def displayError(content: String): HTML =s"""<h1>THERE IS AN ERROR : $content</h1>"""
    }

  }

  def main(args: Array[String]) {
    //DEMONSTRATION
    Demonstration.demonstrate()

    //EXERCISE
    println("\n\n-------------------EXERCISE---------------------")
    import Exercise._
    import Conversions._
    import FrontEnd._
    ///   code  vvvv  ///
    val html1 = UsersDAO.find(1).map(userToHTML).fold(displayError("id 1 not find"))(displayPage)
    val html2 = UsersDAO.find(2).map(userToHTML).fold(displayError("no id 2 in db"))(displayPage)
    // map return a list , fold return a single value
    println("- one line with domain transformation")
    UsersDAO.find(2).map(userToHTML).map(displayPage).orElse(Some(displayError("id 2 not found"))).foreach(println)
    println(s"[ID=1] : $html1")
    println(s"[ID=2] : $html2")

    ////  code  ^^^^   ////
}
}