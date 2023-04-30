package com.example.gerenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.model.TrackingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class LoadingState{
    IDLE,
    LOADED,
    LOADING,

}

class TrackingOrderViewModel : ViewModel(){

    fun doneItem(orderID:String,userID:Int,item:TrackingItem, onSuccess: (List<TrackingItem>) -> Unit, onError: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val success = withContext(Dispatchers.IO) {
                return@withContext KotlinDatabase.executeUpdate(query = TrackingItem.getUpdateQuery(item))
            }
            if (success.not()){ onError()}
            loadItems(orderID,userID, onSuccess = {onSuccess(it)}, onError = {onError()})
        }
    }
    fun loadItems(orderID:String,userID : Int,onSuccess: (List<TrackingItem>)->Unit,onError:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            KotlinDatabase.executeRawQuery(TrackingItem,TrackingItem.getQuery(orderID,userID), onError = {
                onError()
            }, onSuccess = {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess(it)
                }
            })
        }
    }
}