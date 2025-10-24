package modules

import com.google.inject.{AbstractModule, Inject}
import persistence.MongoDBPersistence

class StartModule @Inject extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[MongoDBPersistence]).asEagerSingleton()
  }
}
