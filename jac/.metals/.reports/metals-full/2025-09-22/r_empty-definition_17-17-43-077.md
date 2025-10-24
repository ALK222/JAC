error id: file://<WORKSPACE>/app/controllers/CommitInfoController.scala:`<none>`.
file://<WORKSPACE>/app/controllers/CommitInfoController.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -play/api/mvc/ControllerComponents#
	 -play/api/libs/json/ControllerComponents#
	 -ControllerComponents#
	 -scala/Predef.ControllerComponents#
offset: 384
uri: file://<WORKSPACE>/app/controllers/CommitInfoController.scala
text:
```scala
package controllers
import play.api.mvc.{BaseController, ControllerComponents}
import javax.inject.Inject
import play.api.mvc.{AnyContent, Action}
import javax.inject.Singleton
import scala.collection.mutable
import models.CommitInfo
// import models.CommitStatus
import play.api.libs.json._

@Singleton
class CommitInfoController @Inject()(val controllerComponents: ControllerCompone@@nts) extends BaseController{
    private val todoList = new mutable.ListBuffer[CommitInfo]()
    todoList += CommitInfo(1, "test", "testDesc", 0)
    def getAll(): Action[AnyContent] = Action {
        if (todoList.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(todoList))
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.