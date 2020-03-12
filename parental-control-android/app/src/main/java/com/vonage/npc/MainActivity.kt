package com.vonage.npc

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    private fun isRoleHeldByApp(roleName: String): Boolean {
        var roleManager: RoleManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            roleManager = getSystemService(RoleManager::class.java)
            return roleManager.isRoleHeld(roleName)
        }
        return false
    }

    private fun roleAcquire(roleName: String) {
        var roleManager: RoleManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (roleAvailable(roleName)) {
                roleManager = getSystemService(RoleManager::class.java)
                val intent =
                    roleManager.createRequestRoleIntent(roleName)
                startActivityForResult(intent, 1)
            }else{
                Toast.makeText(this, "Redirection call with role in not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun roleAvailable(roleName: String):Boolean{
        var roleManager: RoleManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            roleManager = getSystemService(RoleManager::class.java)
            return roleManager.isRoleAvailable(roleName)
        }
        return false
    }
}
