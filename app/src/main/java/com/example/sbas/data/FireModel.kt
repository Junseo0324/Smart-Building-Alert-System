package com.example.sbas.data

data class FireModel(
    var id: Long,
    var flame: String, //불꽃
    var temperature: String, //온도
    var sensorTime: String  // 시간
)
data class GasModel(
    var id: Long,
    var flammable: String,
    var co: String,
    var sensorTime: String
)

data class QuakeModel(
    var id: Long,
    var vibrationSensor: String,
    var sensorTime: String
)

data class WarningModel(
    var token: String
)

