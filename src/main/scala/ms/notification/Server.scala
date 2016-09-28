package ms.notification


import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter
import ms.notification.controller.{http, thrift}
import ms.notification.module.NotificationModule
import ms.notification.util.ZConfig

/**
 * Created by SangDang on 9/8/
 **/
object MainApp extends Server

class Server extends HttpServer with ThriftServer {

  override protected def defaultFinatraHttpPort: String = ZConfig.getString("server.http.port", ":8001")

  override protected def defaultFinatraThriftPort: String = ZConfig.getString("server.thrift.port", ":9001")

  override val modules = Seq(NotificationModule)

  override protected def disableAdminHttpServer: Boolean = ZConfig.getBoolean("server.admin.disable", default = true)

  override protected def configureHttp(router: HttpRouter): Unit = {
    router.filter[CommonFilters]
      .add[http.NotificationController]
  }

  override protected def configureThrift(router: ThriftRouter): Unit = {
    router
      .add[thrift.NotificationController]
  }
}
