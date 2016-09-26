package ms.notification.repository

import javax.inject.Inject

import com.twitter.util.{Future, FuturePool}
import ms.notification.domain.NotificationInfo
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.{InetSocketTransportAddress, TransportAddress}
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}

/**
 * Created by phuonglam on 9/23/16.
 **/
@unchecked
case class NotificationRepositoryESImpl @Inject()(
  nodes: Seq[AnyRef],
  cluster: String,
  index: String,
  typ: String,
  maxNotification: Int = 1000
) extends NotificationRepository {

  val Sender = "sender"
  val Receiver = "receiver"
  val Data = "data"
  val NotifyType = "notify_type"
  val CreatedTime = "created_time"
  val UpdatedTime = "updated_time"
  val IsRead = "is_read"

  /**
   *  Define & init es client with parameters:
   *    - nodes:
   *        + String object: es service host:port. Ex: 127.0.0.1:9200
   *        + TransportAddress object
   *    - cluster: cluster name
   *    - index: es
   */
  val client = new TransportClient(
    ImmutableSettings.settingsBuilder
      .put("cluster.name", cluster)
      .build)
  nodes.foreach {
    case (s: String) =>
      val split = s.split(":")
      client.addTransportAddress(new InetSocketTransportAddress(split(0), split(1).toInt))
    case transport: TransportAddress => client.addTransportAddress(_)
  }

  client.admin.indices.preparePutMapping(index).setType(typ).setSource(getMapping(typ)).execute.actionGet

  private lazy val futurePool = FuturePool.unboundedPool

  private def update(id: String, xContentBuilder: XContentBuilder): UpdateResponse = {
    client.prepareUpdate
      .setIndex(index)
      .setType(typ)
      .setId(id)
      .setDoc(xContentBuilder)
      .setRefresh(true)
      .execute()
      .actionGet()
  }

  private def buildQuery(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String],
    isRead: Option[Boolean]
  ): QueryBuilder = {
    val queryBuilder = QueryBuilders.boolQuery()
    queryBuilder.must(QueryBuilders.termQuery(Receiver, user))
    fromSender match {
      case Some(s) => queryBuilder.must(QueryBuilders.termQuery(Sender, s))
      case None =>
    }
    notifyType match {
      case Some(s) => queryBuilder.must(QueryBuilders.termQuery(NotifyType, s))
      case None =>
    }
    isRead match {
      case Some(s) => queryBuilder.must(QueryBuilders.termQuery(IsRead, s))
      case None =>
    }
    queryBuilder
  }

  private def buildUpdateReadContent(isRead: Boolean): XContentBuilder = {
    XContentFactory
      .jsonBuilder
      .startObject
      .field(IsRead, true)
      .field(UpdatedTime, System.currentTimeMillis())
      .endObject
  }

  private def search(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String],
    isRead: Option[Boolean],
    page: Int,
    size: Int
  ): Seq[NotificationInfo] = {
    client
      .prepareSearch(index)
      .setTypes(typ)
      .setQuery(buildQuery(user, fromSender, notifyType, isRead))
      .setFrom((page - 1) * size)
      .setSize(size)
      .execute
      .actionGet
  }

  /**
   *  Foreach receiver, create a notification object
   */
  override def create(infos: Seq[NotificationInfo]): Future[Seq[String]] = futurePool {
    for (info <- infos) yield {
      client
        .prepareIndex(index, typ)
        .setSource(Info2XContent(info))
        .setRefresh(true)
        .execute
        .actionGet
        .getId
    }
  }

  /**
   *    GET
   */
  override def get(notifyId: String): Future[NotificationInfo] = futurePool {
    client
      .prepareGet(index, typ, notifyId)
      .execute()
      .actionGet()
  }

  /**
   *    DELETE
   */
  override def delete(notifyId: String): Future[Boolean] = futurePool {
    client
      .prepareDelete(index, typ, notifyId)
      .execute
      .actionGet
      .isFound
  }

  /**
   *    Find all notification object with paging
   */
  override def findAll(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String],
    page: Int,
    size: Int
  ): Future[Seq[NotificationInfo]] = futurePool {
    search(user, fromSender, notifyType, None, page, size)
  }

  /**
   *  Find all notification object with isRead = false & paging
   */
  override def findUnread(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String],
    page: Int,
    size: Int
  ): Future[Seq[NotificationInfo]] = futurePool {
    search(user, fromSender, notifyType, Option(false), page, size)
  }

  /**
   *  return total of notification object with isRead = false
   */
  override def numUnread(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String]
  ): Future[Long] = futurePool {
    client
      .prepareCount(index)
      .setTypes(typ)
      .setQuery(buildQuery(user, fromSender, notifyType, Option(false)))
      .execute
      .actionGet
      .getCount
  }

  override def markRead(user: String, notifyId: String): Future[Boolean] = futurePool {
    !update(notifyId, buildUpdateReadContent(false)).isCreated
  }

  /**
   *  Update all notification object belong to specified USER with value isRead = true
   */
  override def markReadAll(
    user: String,
    fromSender: Option[String],
    notifyType: Option[String]
  ): Future[Boolean] = futurePool {
    val searchResponse = client
      .prepareSearch(index)
      .setTypes(typ)
      .setQuery(buildQuery(user, fromSender, notifyType, Option(false)))
      .setFetchSource(false)
      .setSize(maxNotification)
      .execute
      .actionGet
    val updateContent = buildUpdateReadContent(true)
    searchResponse.getHits.getHits.foreach(hit => {
      update(hit.getId, updateContent)
    })
    true
  }

  override def markUnread(user: String, notifyId: String): Future[Boolean] = futurePool {
    !update(notifyId, buildUpdateReadContent(true)).isCreated
  }

  private def getMapping(typ: String): XContentBuilder = {
    XContentFactory.jsonBuilder
      .startObject
      .startObject(typ)
      .startObject("properties")
      .startObject(Sender).field("type", "string").field("index", "not_analyzed").endObject
      .startObject(Receiver).field("type", "string").field("index", "not_analyzed").endObject
      .startObject(Data).field("type", "string").field("index", "no").endObject
      .startObject(NotifyType).field("type", "string").field("index", "not_analyzed").endObject
      .startObject(CreatedTime).field("type", "long").endObject
      .startObject(UpdatedTime).field("type", "long").endObject
      .startObject(IsRead).field("type", "boolean").endObject
      .endObject
      .endObject
      .endObject
  }

  implicit def Info2XContent(info: NotificationInfo): XContentBuilder = {
    XContentFactory.jsonBuilder().startObject()
      .field(Sender, info.sender)
      .field(Receiver, info.receiver)
      .field(Data, info.data)
      .field(NotifyType, info.notifyType match {
        case Some(s) => s
        case None => null
      })
      .field(CreatedTime, info.createdTime match {
        case Some(s) => s
        case None => System.currentTimeMillis()
      })
      .field(UpdatedTime, info.updatedTime match {
        case Some(s) => s
        case None => System.currentTimeMillis()
      })
      .field(IsRead, info.isRead match {
        case Some(s) => s
        case None => false
      })
  }

  implicit def GetResponse2Info(response: GetResponse): ms.notification.domain.NotificationInfo = {
    val map = response.getSource
    ms.notification.domain.NotificationInfo(
      notifyId = Option(response.getId),
      sender = map.get(Sender).asInstanceOf[String],
      receiver = map.get(Receiver).asInstanceOf[String],
      data = map.get(Data).asInstanceOf[String],
      notifyType = Option(map.get(NotifyType).asInstanceOf[String]),
      createdTime = Option(map.get(CreatedTime).asInstanceOf[Long]),
      updatedTime = Option(map.get(UpdatedTime).asInstanceOf[Long]),
      isRead = Option(map.get(IsRead).asInstanceOf[Boolean]))
  }

  implicit def SearchResponse2Seq(response: SearchResponse): Seq[ms.notification.domain.NotificationInfo] = {
    for (res <- response.getHits.getHits) yield {
      val map = res.sourceAsMap
      ms.notification.domain.NotificationInfo(
        notifyId = Option(res.getId),
        sender = map.get(Sender).asInstanceOf[String],
        receiver = map.get(Receiver).asInstanceOf[String],
        data = map.get(Data).asInstanceOf[String],
        notifyType = Option(map.get(NotifyType).asInstanceOf[String]),
        createdTime = Option(map.get(CreatedTime).asInstanceOf[Long]),
        updatedTime = Option(map.get(UpdatedTime).asInstanceOf[Long]),
        isRead = Option(map.get(IsRead).asInstanceOf[Boolean]))
    }
  }
}
