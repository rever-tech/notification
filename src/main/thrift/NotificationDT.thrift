#@namespace scala com.rever.domain.thrift
struct Notification {
    1: required string data
    2: required string sender
    3: required list<string> receiver
    4: optional string notifyId
    5: optional string notifyType
    6: optional i64 createdTime
}