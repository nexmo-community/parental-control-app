package com.vonage.vpc

import android.net.Uri
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallRedirectionServiceImplementation : CallRedirectionService() {

    override fun onPlaceCall(
        handle: Uri,
        initialPhoneAccount: PhoneAccountHandle,
        allowInteractiveResponse: Boolean
    ) {
        Log.d(
            "CallRedirectionServiceImplementation",
            " handle:$handle , initialPhoneAccount:$initialPhoneAccount , allowInteractiveResponse:$allowInteractiveResponse"
        )

        //Check if the service should redirect the call
        if (getRedirectState(applicationContext)) {
            //Update the web hook the current call destination
            setPhoneNumberProxy(
                handle.encodedSchemeSpecificPart,
                applicationContext,
                initialPhoneAccount,
                this
            )
        } else {
            placeCallUnmodified()
        }
    }

    fun redirectToVonage(newPhone: Uri, initialPhoneAccount: PhoneAccountHandle) {
        redirectCall(newPhone, initialPhoneAccount, false)
    }

    fun redirectToVonageFail(){
        //placeCallUnmodified()
        //or
        cancelCall()
    }
}