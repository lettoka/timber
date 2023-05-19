package com.example.gerenda.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenda.adapter.OrderAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.extension.md5
import com.example.gerenda.model.*
import com.example.gerenda.model.userData
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.coroutines.*

class ProductionTrackingViewModel : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val orders = mutableStateOf<List<ProductionTrackingOrder>>(listOf())
    val processes = mutableStateOf<List<ProductProcess>>(listOf())
    val selecteProcess = mutableStateOf<ProductProcess?>(null)
   // val isLoading = mutableStateOf(false)
    var isPullRefreshing by mutableStateOf(false)
    val loginShown = mutableStateOf(false)
    var dropDownExpanded = mutableStateOf(false)
    var isDropDownRefreshing by mutableStateOf(false)


    fun logout(){
        user.value = null
        orders.value = listOf()
    }


    fun loadProcesses(){
        isDropDownRefreshing = true
        processes.value = listOf()
        viewModelScope.launch(Dispatchers.IO) {

            KotlinDatabase.executeRawQuery<ProductProcess>(
                ProductProcess,
                ProductProcess.getListQuery(), mustReturnOneRow = true
                , onSuccess =  { list ->
                    viewModelScope.launch(Dispatchers.Main) {
                        processes.value = list
                        dropDownExpanded.value = true
                        isDropDownRefreshing = false
                    }

                }, onError = {
                    viewModelScope.launch(Dispatchers.Main) {
                        // isLoading.value = false
                        loginShown.value = true
                        isDropDownRefreshing = false
                    }
                })

        }
    }

    fun selectProcess(process: ProductProcess){
        selecteProcess.value = process
        dropDownExpanded.value = false
        loadOrdersForProcess(processID = process.id)
    }

    fun login(username:String,password:String){
        //isLoading.value = true
        loginShown.value = false
        viewModelScope.launch(Dispatchers.IO) {

            KotlinDatabase.executeRawQuery<UserRoleResponse>(
                UserRoleResponse,
                UserRoleResponse.getQuery(username,password.uppercase().md5()), mustReturnOneRow = true
                , onSuccess =  { list ->
                    viewModelScope.launch(Dispatchers.Main) {
                        user.value = list.userData()
                        loadOrders()
                    }

                }, onError = {
                    viewModelScope.launch(Dispatchers.Main) {
                       // isLoading.value = false
                        loginShown.value = true
                    }
                })

        }
    }
    fun pullRefresh(){
        viewModelScope.launch(Dispatchers.IO) {
            // async{
            isPullRefreshing = true

            reloadUser()

//
           // isPullRefreshing = false

        }
    }


     fun reloadUser(){
        user.value?.let {
            KotlinDatabase.executeRawQuery<UserRoleResponse>(
                UserRoleResponse,
                UserRoleResponse.getReloadQuery(it.id), mustReturnOneRow = true
                , onSuccess =  { list ->
                    viewModelScope.launch(Dispatchers.Main) {
                        user.value = list.userData()
                        loadOrders()
                    }

                }, onError = {
                    viewModelScope.launch(Dispatchers.Main) {
                       // isLoading.value = false
                        isPullRefreshing = false
                        loginShown.value = true
                    }
                })
        }
    }

//    fun reloadUser(){
//        isLoading.value = true
//        viewModelScope.launch(Dispatchers.IO) {
//
//                KotlinDatabase.executeRawQuery<UserRoleResponse>(
//                    UserRoleResponse,
//                    UserRoleResponse.getQuery()
//                , onSuccess =  { list ->
//                    viewModelScope.launch(Dispatchers.Main) {
//                        user.value = list.userData()
//                        loadOrders()
//                    }
//
//                }, onError = {
//                        viewModelScope.launch(Dispatchers.Main) {
//                            isLoading.value = false
//                        }
//                    })
//
//
//        }
//    }

    fun loadOrdersForProcess(processID : Int){
        orders.value = listOf()
        // isLoading.value = true
        isPullRefreshing = true

            viewModelScope.launch(Dispatchers.IO) {

                KotlinDatabase.executeRawQuery<ProductionTrackingOrder>(
                    ProductionTrackingOrder,
                    ProductionTrackingOrder.getListForProcess(processID), onSuccess = { list ->
                        viewModelScope.launch(Dispatchers.Main) {

                            orders.value = list
                            //isLoading.value = false
                            isPullRefreshing = false
                        }
                    }, onError = {
                        viewModelScope.launch(Dispatchers.Main) {
                            // isLoading.value = false
                            isPullRefreshing = false
                        }
                    })

            }

    }

    fun loadOrders(){
        if (user.value == null){ orders.value = listOf() ;return}
        orders.value = listOf()
       // isLoading.value = true
        isPullRefreshing = true
        user.value?.id?.let {userID->
            viewModelScope.launch(Dispatchers.IO) {

                KotlinDatabase.executeRawQuery<ProductionTrackingOrder>(
                    ProductionTrackingOrder,
                    ProductionTrackingOrder.getQuery(userID), onSuccess = { list ->
                        viewModelScope.launch(Dispatchers.Main) {

                            orders.value = list
                            //isLoading.value = false
                            isPullRefreshing = false
                        }
                    }, onError = {
                        viewModelScope.launch(Dispatchers.Main) {
                           // isLoading.value = false
                            isPullRefreshing = false
                        }
                    })

            }
        }
    }

}