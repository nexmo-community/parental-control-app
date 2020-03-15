package com.vonage.vpc

import android.content.Context
import android.net.Uri
import android.telecom.PhoneAccountHandle
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


fun setPhoneNumberProxy(
    destination: String, context: Context, initialPhoneAccount: PhoneAccountHandle,
    callback: CallRedirectionServiceImplementation
) {

    val jsonBody = JSONObject()
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    jsonBody.put("from", getOwnerPhoneNumber(context))
    jsonBody.put(
        "to",
        if (destination.length > 10)  destination else
        PhoneNumberUtils.formatNumberToE164(
            destination,
            telephonyManager.networkCountryIso.toUpperCase()
        ).replace("+", "")
    )

    val requestBody = jsonBody.toString()

    Volley.newRequestQueue(context).add(
        object : StringRequest(Request.Method.POST,
            "${context.getString(R.string.server_address)}/proxy",
            Response.Listener<String> {
                Log.d("setPhoneNumberProxy", "success $it")
                val newPhone = Uri.fromParts("tel", JSONObject(it).optString("number"), "")
                callback.redirectToVonage(newPhone, initialPhoneAccount)
            },
            Response.ErrorListener {
                Log.d("setPhoneNumberProxy ", "error $it")
                callback.redirectToVonageFail()
            }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray(Charsets.UTF_8);
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
    )
}

/**
 * Owner phone number
 */

fun setOwnerPhoneNumber(number: String, context: Context) {
    context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).edit()
        .putString("PHONE_NUMBER", number).apply()
}

fun getOwnerPhoneNumber(context: Context): String? {
    return context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE)
        .getString("PHONE_NUMBER", null)
}

/**
 * Redirect State Handlers
 */

fun setRedirectState(newState: Boolean, context: Context) {
    context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE).edit()
        .putBoolean("STATE", newState).apply()
}

fun getRedirectState(context: Context): Boolean {
    return context.getSharedPreferences(context.getString(R.string.sp_name), Context.MODE_PRIVATE)
        .getBoolean("STATE", false)
}