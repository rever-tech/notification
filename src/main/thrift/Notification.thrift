#@namespace scala ms.notification.service
include "NotificationDT.thrift"

service TNotificationService {

    string ping()

    list<string> sendNotification(
        1: required NotificationDT.Notification notification
    )

    i64 getNumUnread(
        1: required string user
        2: optional string notifyType
        3: optional string fromSender
    )

    bool markRead(
        1: required string user
        2: required string notifyId
    )

    bool markReadAll(
        1: required string user
        2: optional string notifyType
        3: optional string fromSender
    )

    bool markUnread(
        1: required string user
        2: required string notifyId
    )

    list<NotificationDT.Notification> getUnread(
        1: required string user
        2: optional i32 page
        3: optional i32 size
        4: optional string notifyType
        5: optional string fromSender
    )

    list<NotificationDT.Notification> getNotification(
        1: required string user
        2: optional i32 page
        3: optional i32 size
        4: optional string notifyType
        5: optional string fromSender
    )
}