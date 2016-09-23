#@namespace scala ms.notification.domain.thrift
struct Notification {
    1: required string sender
    2: required list<string> receiver
    3: required string data
    4: optional string notifyId
    5: optional string notifyType
    6: optional i64 createdTime
}