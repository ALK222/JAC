package controllers

import managers.CommitManager
import play.api.libs.json.{Json, __}
import play.api.mvc.*

import javax.inject.*
import persistence.entities.{CommitDTO, FileDTO}
import errors.*
import java.io.File
import scala.concurrent.ExecutionContext

@Singleton
case class CommitController @Inject() (
                                        commitManager: CommitManager,
                                        val controllerComponents: ControllerComponents
                                      )(implicit val ec: ExecutionContext) extends BaseController {

  /** Helper to handle Either[AppError, T] responses */
  private def handleResult[T](result: Either[ManagerError,T])(onSuccess: T => Result): Result =
    result.fold(
      error => BadRequest(error.message),
      onSuccess
    )

  /** GET a single commit by ID */
  def get(id: String): Action[AnyContent] = Action {
    commitManager.get(id).fold(
      error => BadRequest(error.message),
      {
        case Some(commit) => Ok(Json.toJson(commit))
        case None         => NoContent
      }
    )
  }

  /** GET all commits */
  def getAll: Action[AnyContent] = Action {
    handleResult(commitManager.getAll) { commits =>
      Ok(Json.toJson(commits))
    }
  }

  /** PUT (update) a commit */
  def update: Action[CommitDTO] = Action(parse.json[CommitDTO]) { implicit request =>
    handleResult(commitManager.update(request.body)) { _ =>
      NoContent
    }
  }

  /** POST (save) a new commit */
  def save: Action[CommitDTO] = Action(parse.json[CommitDTO]) { implicit request =>
    handleResult(commitManager.save(request.body)) { commit =>
      Ok(Json.toJson(commit))
    }
  }

  def download:Action[FileDTO] = Action(parse.json[FileDTO]){ implicit request =>
    handleResult(commitManager.download(request.body)) { file =>
      Ok.sendFile(new File(file.path))
    }
  }
}
