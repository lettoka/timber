package com.example.gerenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.extension.md5
import com.example.gerenda.model.ProcessTracking.TrackingItem
import com.example.gerenda.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class LoadingState{
    IDLE,
    LOADED,
    LOADING,

}

class TrackingOrderViewModel : ViewModel(){

    fun doneItem(orderID:String, userID:Int, item: TrackingItem, onSuccess: (List<TrackingItem>) -> Unit, onError: (String) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val success = withContext(Dispatchers.IO) {
                return@withContext KotlinDatabase.executeUpdate(query = TrackingItem.getUpdateQuery(item))
            }
            if (success.not()){ viewModelScope.launch(Dispatchers.Main){onError("Sikertelen művelet")}}
           // loadItems(orderID,userID, onSuccess = {onSuccess(it)}, onError = {onError("A rendelések újratöltése sikertelen")})
            loadItemsForProcess(orderID, processID = item.processID,onSuccess = {onSuccess(it)}, onError = {onError("A rendelések újratöltése sikertelen")})
        }
    }

    fun doneItem(orderID: String,password:String,item: TrackingItem,onSuccess: (List<TrackingItem>) -> Unit, onError: (String) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

                KotlinDatabase.executeRawQuery(User,User.getUserQueryForpassword(password.uppercase().md5()), mustReturnOneRow = true, onSuccess = {
                    it.firstOrNull()?.let {user ->
                        doneItem(orderID,user.id,item, onSuccess = onSuccess, onError = onError)
                    }
                }, onError = {
                    viewModelScope.launch(Dispatchers.Main){
                    onError("Nem található felhasználó")
                    }
                })
        }
    }

    fun loadItemsForProcess(orderID:String, processID:Int, onSuccess: (List<TrackingItem>)->Unit, onError:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            KotlinDatabase.executeRawQuery(
                TrackingItem,
                TrackingItem.getQueryForProcess(orderID,processID), onError = {
                    onError()
                }, onSuccess = {
                    viewModelScope.launch(Dispatchers.Main) {
                        onSuccess(it)
                    }
                })
        }
    }
//    fun loadItems(orderID:String, userID : Int, onSuccess: (List<TrackingItem>)->Unit, onError:()->Unit){
//        viewModelScope.launch(Dispatchers.IO) {
//            KotlinDatabase.executeRawQuery(
//                TrackingItem,
//                TrackingItem.getQuery(orderID,userID), onError = {
//                    viewModelScope.launch(Dispatchers.Main){
//                onError()
//                }
//            }, onSuccess = {
//                viewModelScope.launch(Dispatchers.Main) {
//                    onSuccess(it)
//                }
//            })
//        }
//    }
}