package ms.notification.repository.elasticsearch

import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress

/**
  * Created by phuonglam on 9/23/16.
  **/
case class ESClient(nodes: Seq[String], cluster: String) {
  val client = new TransportClient(ImmutableSettings.settingsBuilder.put("cluster.name", cluster).build)
  nodes.foreach(node => {
    val split = node.split(":")
    client.addTransportAddress(new InetSocketTransportAddress(split(0), split(1).toInt))
  })

  def getClient = client
  def stop() = client.close()

}
