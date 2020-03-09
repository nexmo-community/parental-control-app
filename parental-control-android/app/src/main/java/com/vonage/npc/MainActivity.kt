package com.vonage.npc

import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vonage.npc.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        updateServerProperties()
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult requestCode: $requestCode resultCode:$resultCode data:$data")
        updateUI()
    }

    private fun updateServerProperties(){
        if (getOwnerPhoneNumber(this) != null){
            setOwnerPhoneNumberServer(getOwnerPhoneNumber(this)!!, this)
            setPhoneNumberProxy(applicationContext.getString(R.string.proxy_phone_number), applicationContext)
        }
    }

    @SuppressLint("WrongConstant")
    fun updateUI(){
        binding.stateCallProxy.isEnabled = false
        binding.phoneNumberApply.isEnabled = false
        binding.enableRoleCallProxy.isEnabled = false
        binding.unregister.isEnabled = false
        if (!isRegister()){
            binding.phoneNumberApply.isEnabled = true
            binding.phoneNumberInput.setText(getOwnerPhoneNumber(this))
            binding.phoneNumberApply.setOnClickListener {
                if(binding.phoneNumberInput.text!!.isNotEmpty()){
                    setOwnerPhoneNumber(binding.phoneNumberInput.text.toString(), this)
                    setPhoneNumberProxy(applicationContext.getString(R.string.proxy_phone_number), applicationContext)
                    updateUI()
                }
            }
            return
        }


        binding.unregister.isEnabled = true
        binding.unregister.setOnClickListener {
            setOwnerPhoneNumber("", this)
            setState(false, this)
            updateUI()
        }

        binding.phoneNumberInput.setText(getOwnerPhoneNumber(this))
        if (!isRedirection()){
            binding.enableRoleCallProxy.isEnabled = true
            binding.enableRoleCallProxy.setOnClickListener {
                roleAcquire(RoleManager.ROLE_CALL_REDIRECTION)
                updateUI()
            }
            return
        }

        binding.stateCallProxy.isEnabled = true
        binding.stateCallProxy.isChecked = getState(this)
        binding.stateCallProxy.setOnClickListener {
            setState(binding.stateCallProxy.isChecked, this)
            updateUI()
        }


    }

    //is our app register a phone number
    private fun isRegister(): Boolean {
        return !getOwnerPhoneNumber(this).isNullOrEmpty()
    }

    //is our app register as the call redirection service
    private fun isRedirection(): Boolean {
        return isRoleHeldByApp(RoleManager.ROLE_CALL_REDIRECTION)
    }

    //is our app register as the call redirection service
    @SuppressLint("WrongConstant")
    private fun isRoleHeldByApp(roleName: String): Boolean {
        var roleManager: RoleManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            return roleManager.isRoleHeld(roleName)
        }
        return false
    }

    @SuppressLint("WrongConstant")
    private fun roleAcquire(roleName: String) {
        var roleManager: RoleManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            val intent =
                roleManager.createRequestRoleIntent(roleName)
            startActivityForResult(intent, 1)
        }
    }


}
