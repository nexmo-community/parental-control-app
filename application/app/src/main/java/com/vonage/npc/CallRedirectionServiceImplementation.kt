package com.vonage.npc

import android.net.Uri
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallRedirectionServiceImplementation : CallRedirectionService() {

    override fun onPlaceCall(handle: Uri, initialPhoneAccount: PhoneAccountHandle, allowInteractiveResponse: Boolean) {
        Log.d("CallRedirectionServiceImplementation", " handle:$handle , initialPhoneAccount:$initialPhoneAccount , allowInteractiveResponse:$allowInteractiveResponse")

        //Check if the service should redirect the call
        if (getState(applicationContext)) {
            //Update the web hook the current call destination
            setPhoneCallServer(handle.encodedSchemeSpecificPart, applicationContext)
            setPhoneNumberProxy(applicationContext.getString(R.string.proxy_phone_number), applicationContext)
            //Change the outgoing call to Nexmo number
            val newPhone = Uri.fromParts("tel", getProxyPhoneNumber(applicationContext), "")
            Log.d("CallRedirectionServiceImplementation", " newPhone:$newPhone")
            //Redirect the call without any user interaction
            redirectCall(newPhone, initialPhoneAccount, false)
        }else{
            placeCallUnmodified()
        }
    }


}