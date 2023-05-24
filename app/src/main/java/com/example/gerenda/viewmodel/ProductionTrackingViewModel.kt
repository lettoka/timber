package com.example.gerenda.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.extension.md5
import com.example.gerenda.model.*
import com.example.gerenda.model.ProcessTracking.ProcessGroup
import com.example.gerenda.model.ProcessTracking.ProductionTrackingOrder
import com.example.gerenda.model.userData
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ProductionTrackingViewModel : ViewModel() {
    val user = mutableStateOf<UserWithRoles?>(null)
    val orders = mutableStateOf<List<ProductionTrackingOrder>>(listOf())
    val processGroups = mutableStateOf<List<ProcessGroup>>(listOf())
    val selectedProcessGroup = mutableStateOf<ProcessGroup?>(null)
   // val isLoading = mutableStateOf(false)
    var isPullRefreshing by mutableStateOf(false)
    val loginShown = mutableStateOf(false)
    var dropDownExpanded = mutableStateOf(false)
    var isDropDownRefreshing by mutableStateOf(false)
    var onGotPassword by mutableStateOf<Pair<Boolean,((String?)->Unit)>?>(null)


    fun logout(){
        user.value = null
        orders.value = listOf()
    }


    fun loadProcessGroups(){
        isDropDownRefreshing = true
        processGroups.value = listOf()
        viewModelScope.launch(Dispatchers.IO) {

            KotlinDatabase.executeRawQuery<ProcessGroup>(
                ProcessGroup,
                ProcessGroup.getListQuery(), mustReturnOneRow = true
                , onSuccess =  { list ->
                    viewModelScope.launch(Dispatchers.Main) {
                        processGroups.value = list
                        dropDownExpanded.value = true
                        isDropDownRefreshing = false
                    }

                }, onError = {
                    viewModelScope.launch(Dispatchers.Main) {
                        // isLoading.value = false
                        //loginShown.value = true
                        dropDownExpanded.value = false
                        isDropDownRefreshing = false
                    }
                })

        }
    }

    fun selectProcess(processGroup: ProcessGroup){
        selectedProcessGroup.value = processGroup
        dropDownExpanded.value = false
        loadOrdersForProcess(processGroupID = processGroup.id)
    }

    fun processPassword(password : String?){
        onGotPassword?.second?.invoke(password)
        onGotPassword = null//dismiss the prompt
    }

    fun askForPassword(undo:Boolean,onGotPassword : (String?)->Unit){
        this.onGotPassword = Pair(undo,onGotPassword)
    }

//    fun login(username:String,password:String){
//        //isLoading.value = true
//        loginShown.value = false
//        viewModelScope.launch(Dispatchers.IO) {
//
//            KotlinDatabase.executeRawQuery<UserRoleResponse>(
//                UserRoleResponse,
//                UserRoleResponse.getQuery(username,password.uppercase().md5()), mustReturnOneRow = true
//                , onSuccess =  { list ->
//                    viewModelScope.launch(Dispatchers.Main) {
//                        user.value = list.userData()
//                        loadOrders()
//                    }
//
//                }, onError = {
//                    viewModelScope.launch(Dispatchers.Main) {
//                       // isLoading.value = false
//                        loginShown.value = true
//                    }
//                })
//
//        }
//    }
    fun pullRefresh(){
        viewModelScope.launch(Dispatchers.IO) {
            // async{

            selectedProcessGroup.value?.let {
                isPullRefreshing = true
                loadOrdersForProcess(it.id)

            }


//
           // isPullRefreshing = false

        }
    }


//     fun reloadUser(){
//        user.value?.let {
//            KotlinDatabase.executeRawQuery<UserRoleResponse>(
//                UserRoleResponse,
//                UserRoleResponse.getReloadQuery(it.id), mustReturnOneRow = true
//                , onSuccess =  { list ->
//                    viewModelScope.launch(Dispatchers.Main) {
//                        user.value = list.userData()
//                        loadOrders()
//                    }
//
//                }, onError = {
//                    viewModelScope.launch(Dispatchers.Main) {
//                       // isLoading.value = false
//                        isPullRefreshing = false
//                        loginShown.value = true
//                    }
//                })
//        }
//    }

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


    fun loadOrdersForProcess(processGroupID : Int){
        orders.value = listOf()
        // isLoading.value = true
        isPullRefreshing = true

        var formatter = SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH)
        var formattedDate =  formatter.format(Calendar.getInstance().time)
            viewModelScope.launch(Dispatchers.IO) {

                KotlinDatabase.executeRawQuery<ProductionTrackingOrder>(
                    ProductionTrackingOrder,
                    ProductionTrackingOrder.getListForProcessGroup(processGroupID,formattedDate), onSuccess = { list ->
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

//    fun loadOrders(){
//        if (user.value == null){ orders.value = listOf() ;return}
//        orders.value = listOf()
//       // isLoading.value = true
//        isPullRefreshing = true
//        user.value?.id?.let {userID->
//            viewModelScope.launch(Dispatchers.IO) {
//
//                KotlinDatabase.executeRawQuery<ProductionTrackingOrder>(
//                    ProductionTrackingOrder,
//                    ProductionTrackingOrder.getQuery(userID), onSuccess = { list ->
//                        viewModelScope.launch(Dispatchers.Main) {
//
//                            orders.value = list
//                            //isLoading.value = false
//                            isPullRefreshing = false
//                        }
//                    }, onError = {
//                        viewModelScope.launch(Dispatchers.Main) {
//                           // isLoading.value = false
//                            isPullRefreshing = false
//                        }
//                    })
//
//            }
//        }
//    }

}