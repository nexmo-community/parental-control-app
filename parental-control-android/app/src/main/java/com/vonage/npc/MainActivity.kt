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
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(
            "MainActivity",
            "onActivityResult requestCode: $requestCode resultCode:$resultCode data:$data"
        )
        updateUI()
    }

    fun updateUI() {

        binding.stateCallProxy.isEnabled = false
        binding.enableRoleCallProxy.isEnabled = false

        binding.phoneNumberInput.setText(getOwnerPhoneNumber(this))
        if (!isRedirection()) {
            binding.enableRoleCallProxy.isEnabled = true
            binding.enableRoleCallProxy.setOnClickListener {
                setOwnerPhoneNumber(binding.phoneNumberInput.text.toString(), this)
                roleAcquire(RoleManager.ROLE_CALL_REDIRECTION)
                setRedirectState(true, this)
                updateUI()
            }
            return
        }

        binding.stateCallProxy.isEnabled = true
        binding.stateCallProxy.isChecked = getRedirectState(this)
        binding.stateCallProxy.setOnClickListener {
            setRedirectState(binding.stateCallProxy.isChecked, this)
            updateUI()
        }
    }

    // Is our app registered as the call redirection service
    private fun isRedirection(): Boolean {
        return isRoleHeldByApp(RoleManager.ROLE_CALL_REDIRECTION)
    }

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
