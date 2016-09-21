struct Notification {
    1: optional string notifyId
    2: required string sender
    3: required list<string> receiver
    4: required string data
    5: optional string notifyType
}