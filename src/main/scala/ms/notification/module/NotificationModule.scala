package ms.notification.module

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import ms.notification.repository.{NotificationRepository, NotificationRepositoryESImpl}
import ms.notification.service.{NotificationServiceImpl, NotificationService}
import ms.notification.util.ZConfig

/**
 * @author phuonglam
 *         created on 9/19/16.
 */
object NotificationModule extends TwitterModule {

  @Singleton
  @Provides
  def provideNotificationRepository(): NotificationRepository = {
    NotificationRepositoryESImpl(
      Seq(ZConfig.getString("es.nodes")),
      ZConfig.getString("es.cluster"),
      ZConfig.getString("es.index"),
      ZConfig.getString("es.type"),
      ZConfig.getInt("es.max_notification", 1000)
    )
  }

  @Singleton
  @Provides
  def provideNotificationService(repo: NotificationRepository): NotificationService = NotificationServiceImpl(repo)
}
