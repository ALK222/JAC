package persistence

import org.bson.codecs.pojo.PojoCodecProvider
import persistence.entities.CommitDTO
import org.mongodb.scala._
import org.mongodb.scala.model.Filters.equal
import javax.inject._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistry

@Singleton
class MongoDBPersistence @Inject() extends Persistence[CommitDTO, String] {

  private val timeout = 30.second

  // Codec registry for CommitDTO
  private val codecRegistry = fromRegistries(
    fromProviders(PojoCodecProvider.builder()
      .register(classOf[CommitDTO])
      .build()),
    MongoClient.DEFAULT_CODEC_REGISTRY
  )

  // MongoDB setup
  private val connectionString =
    "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.5.8"

  private val mongoClient: MongoClient = MongoClient(
    MongoClientSettings.builder()
      .applyConnectionString(ConnectionString(connectionString))
      .codecRegistry(codecRegistry)
      .build()
  )
  
  /*private val databaseTest = mongoClient.getDatabase("admin")
  private val ping = databaseTest.runCommand(Document("ping" -> 1)).head()
  Await.result(ping, 10.seconds)
  System.out.println("Pinged your deployment. You successfully connected to MongoDB!")

*/
  private val database = mongoClient.getDatabase("jac")
  private val collection: MongoCollection[CommitDTO] = database.getCollection("commits")

  println(collection.namespace.toString)

  override def insert(commit: CommitDTO): CommitDTO = {
    Await.result(collection.insertOne(commit).toFuture(), timeout)
    commit
  }

  override def find(id: String): Option[CommitDTO] = {
    val filter = equal("id", id)
    val future = collection.find(filter).first().toFutureOption()
    Await.result(future, timeout)
  }

  override def update(commit: CommitDTO): Unit = {
    val filter = equal("id", commit.id.getOrElse(""))
    Await.result(
      collection.replaceOne(filter, commit).toFuture(),
      timeout
    )
  }

  override def delete(id: String): Unit = {
    val filter = equal("id", id)
    Await.result(collection.deleteOne(filter).toFuture(), timeout)
  }

  override def findAll(): List[CommitDTO] = {
    val future = collection.find().toFuture()
    Await.result(future, timeout).toList
  }
}
