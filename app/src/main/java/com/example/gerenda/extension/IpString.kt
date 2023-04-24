package com.example.gerenda.extension

import java.util.regex.Pattern

fun String.isValidIp():Boolean{
    if (this.isEmpty())
            return false
        val zeroTo255= "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        val regex = "$zeroTo255\\.$zeroTo255\\.$zeroTo255\\.$zeroTo255"
        val p = Pattern.compile(regex)
        return p.matcher(this).matches()
}