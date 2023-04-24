package com.example.gerenda.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenda.adapter.OrderAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.model.*
import com.example.gerenda.model.userData
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductionTrackingViewModel : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val orders = mutableStateOf<List<ProductionTrackingOrder>>(listOf())
    val isLoading = mutableStateOf(false)

    fun logout(){
        user.value = null
        orders.value = listOf()
    }

    fun reloadUser(){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {

                KotlinDatabase.executeRawQuery<UserRoleResponse>(
                    UserRoleResponse,
                    UserRoleResponse.getQuery()
                , onSuccess =  { list ->
                    viewModelScope.launch(Dispatchers.Main) {
                        user.value = list.userData()
                        loadOrders()
                    }

                }, onError = {
                        viewModelScope.launch(Dispatchers.Main) {
                            isLoading.value = false
                        }
                    })

        }
    }

    fun loadOrders(){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {

            KotlinDatabase.executeRawQuery<ProductionTrackingOrder>(
                ProductionTrackingOrder,
                ProductionTrackingOrder.getQuery()
                , onSuccess =  { list ->
                    orders.value = list
                    isLoading.value = false
                }, onError = {
                    viewModelScope.launch(Dispatchers.Main) {
                        isLoading.value = false
                    }
                })

        }
    }

}