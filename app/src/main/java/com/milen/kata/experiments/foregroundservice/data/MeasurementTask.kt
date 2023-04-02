package com.milen.kata.experiments.foregroundservice.data

data class MeasurementTask(
    val id: String = "0",
    val measurementTime: Int = 60000,
    val measurementInterval: Int = 1000,
    val measurementData: MutableList<MeasurementEntity> = mutableListOf()
)