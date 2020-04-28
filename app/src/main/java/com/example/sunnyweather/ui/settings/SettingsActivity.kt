package com.example.sunnyweather.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.EventMessage
import com.example.sunnyweather.logic.dao.PlaceDao
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*
import org.greenrobot.eventbus.EventBus

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel=ViewModelProvider(this).get(SettingsViewModel::class.java)

        if (viewModel.isSavedUpdate()){
            switchBtn.isChecked=viewModel.getUpdate()
        }

        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.saveUpdate(isChecked)
        }

        viewModel.isChecked.observe(this, Observer {
            if (it){
                EventBus.getDefault().post(EventMessage("StartService"))
            }else{
                if (viewModel.isSavedUpdate()){
                    EventBus.getDefault().post(EventMessage("StopService"))
                }
            }
        })
    }
}
