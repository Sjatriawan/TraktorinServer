package com.mobile.util

sealed class MethodPayment(val type:Int){
    object Cash:MethodPayment(0)
    object Bank:MethodPayment(1)
    object Ewallet:MethodPayment(3)
}
