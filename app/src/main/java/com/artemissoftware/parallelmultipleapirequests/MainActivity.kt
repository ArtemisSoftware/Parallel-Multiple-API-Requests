package com.artemissoftware.parallelmultipleapirequests

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.artemissoftware.parallelmultipleapirequests.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingleCall.setOnClickListener {
            viewModel.getCoinData("BTC")
        }

        binding.btnSingleCallError.setOnClickListener {
            viewModel.getError()
        }


        binding.btnParallelCall.setOnClickListener {
            viewModel.parallelRequest()
        }

        binding.btnParallelCallError.setOnClickListener {
            viewModel.parallelRequestError()
        }

        lifecycleScope.launchWhenStarted {

            viewModel.mainEvent.collect { event ->
                when(event) {

                    is MainViewModel.MainEvent.Success -> {
                        binding.progress.isVisible = false
                        binding.txtResult.setTextColor(Color.BLACK)
                        binding.txtResult.text = event.resultText
                    }

                    is MainViewModel.MainEvent.Failure -> {
                        binding.progress.isVisible = false
                        binding.txtResult.setTextColor(Color.RED)
                        binding.txtResult.text = event.errorText
                    }

                    is MainViewModel.MainEvent.Loading -> {
                        binding.progress.isVisible = true
                    }
                    else ->{
                        binding.progress.isVisible = false
                    }
                }
            }
        }
    }
}