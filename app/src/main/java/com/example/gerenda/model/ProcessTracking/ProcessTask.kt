package com.example.gerenda.model.ProcessTracking

data class ProcessTask (
    val onSuccess : (userID : Int) ->Unit,
    val onError : ()->Unit
)