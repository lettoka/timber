package com.example.gerenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.model.TrackingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class LoadingState{
    IDLE,
    LOADED,
    LOADING,

}

class TrackingOrderViewModel : ViewModel(){

    fun loadItems(trackingID:Int,onSuccess: (List<TrackingItem>)->Unit,onError:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            KotlinDatabase.executeRawQuery(TrackingItem,TrackingItem.getQuery(trackingID), onError = {
                onError()
            }, onSuccess = {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess(it)
                }
            })
        }
    }
}