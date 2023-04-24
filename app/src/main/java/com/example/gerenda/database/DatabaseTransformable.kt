package com.example.gerenda.database

import com.example.gerenda.model.OrderModel
import java.sql.ResultSet

 interface DatabaseTransformable <T>{
     public fun TransformData(set:ResultSet) :T


 }