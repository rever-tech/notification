package com.rever.controller

import com.rever.Server
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.finatra.thrift.ThriftClient
import com.twitter.inject.server.FeatureTest

/**
  * Created by PhuongLT on 9/21/16.
  */
class NotificationControllerTest extends FeatureTest {
  override protected def server = new EmbeddedHttpServer(twitterServer = new Server) with ThriftClient

}
