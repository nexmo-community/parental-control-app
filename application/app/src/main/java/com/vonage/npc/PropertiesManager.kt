package com.vonage.npc

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

fun getOwnerPhoneNumber(context: Context): String? {
    return context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).getString("PHONE_NUMBER", null)
}

fun getProxyPhoneNumber(context: Context): String {
    return context.getString(R.string.proxy_phone_number)
}

fun setOwnerPhoneNumber(phoneNumber: String, context: Context){
    context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).edit().putString("PHONE_NUMBER", phoneNumber).apply()
    setOwnerPhoneNumberServer(phoneNumber, context)
}

fun setOwnerPhoneNumberServer(phoneNumber: String, context: Context){
    val fixPhoneNumber = fixPhoneNumber(phoneNumber)
    Volley.newRequestQueue(context).add(StringRequest(Request.Method.POST,
        "${context.getString(R.string.server_address)}/phoneToOwner?phone_number=$fixPhoneNumber",
        Response.Listener<String> {
            Log.d("setOwnerPhoneNumberServer","success $it")
        },
        Response.ErrorListener {
            Log.d("setOwnerPhoneNumberServer","error $it")}))
}
fun setPhoneCallServer(phoneNumber: String, context: Context) {
    val fixPhoneNumber = fixPhoneNumber(phoneNumber)
    Volley.newRequestQueue(context).add(StringRequest(Request.Method.POST,
        "${context.getString(R.string.server_address)}/phoneToCall?phone_number=$fixPhoneNumber",
        Response.Listener<String> {
            Log.d("setPhoneCallServer", "success $it")
        },
        Response.ErrorListener {
            Log.d("setPhoneCallServer ", "error $it")
        }))
}

fun setPhoneNumberProxy(phoneNumber: String, context: Context) {
    val fixPhoneNumber = fixPhoneNumber(phoneNumber)
    Volley.newRequestQueue(context).add(StringRequest(Request.Method.POST,
        "${context.getString(R.string.server_address)}/proxyPhoneNumber?phone_number=$fixPhoneNumber",
        Response.Listener<String> {
            Log.d("setPhoneNumberProxy", "success $it")
        },
        Response.ErrorListener {
            Log.d("setPhoneNumberProxy ", "error $it")
        }))
}
fun setState(newState:Boolean, context: Context){
    context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).edit().putBoolean("STATE", newState).apply()
}

fun getState(context: Context):Boolean{
    return context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).getBoolean("STATE",false)
}

private fun fixPhoneNumber(encodedSchemeSpecificPart: String?): String {
    if (encodedSchemeSpecificPart.isNullOrEmpty()){
        return ""
    }
    if(!encodedSchemeSpecificPart.isNullOrEmpty() && encodedSchemeSpecificPart[0] == '0'){
        return "972${encodedSchemeSpecificPart.substring(1, encodedSchemeSpecificPart.length)}"
    }
    return encodedSchemeSpecificPart
}