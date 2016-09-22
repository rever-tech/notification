#@namespace scala ms.notification.service
include "NotificationDT.thrift"

service TNotificationService {

    string sendNotification(
        1: required NotificationDT.Notification notification
    )

    i32 getNumUnread(
        1: required string user
        2: optional string notifyType
    )

    list<NotificationDT.Notification> getUnread(
        1: required string user
        2: optional string notifyType
        3: optional i32 page
        4: optional i32 size
    )

    void markRead(
        1: required string user
        2: required string notifyId
    )

    void markReadAll(
        1: required string user
        2: optional string notifyType
    )

    void markUnread(
        1: required string user
        2: required string notifyId
    )

    list<NotificationDT.Notification> getNotification(
        1: required string user
        2: optional i32 page
        3: optional i32 size
        4: optional string notifyType
        5: optional string sender
    )
}