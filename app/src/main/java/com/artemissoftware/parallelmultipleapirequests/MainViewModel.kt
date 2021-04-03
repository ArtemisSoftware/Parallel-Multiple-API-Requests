package com.artemissoftware.parallelmultipleapirequests

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemissoftware.parallelmultipleapirequests.repository.CryptoCurrencyRepository
import com.artemissoftware.parallelmultipleapirequests.repository.JsonPlaceHolderRepository
import com.artemissoftware.parallelmultipleapirequests.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
    private val cryptoCurrencyRepository: CryptoCurrencyRepository,
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val dispatcherProvider: DispatcherProvider
): ViewModel(){



    private val _mainEvent = MutableStateFlow<MainEvent>(MainEvent.Empty)
    val mainEvent: StateFlow<MainEvent> = _mainEvent



    fun getCoinData(coin: String) {

        viewModelScope.launch(dispatcherProvider.io) {

            _mainEvent.value = MainEvent.Loading

            try {
                val result = cryptoCurrencyRepository.getCoinData(coin)
                _mainEvent.value = MainEvent.Success("$coin has  ${result.ticker.markets.size} markets")


//                when(val ratesResponse = repository.getRates(toCurrency)) {
//
//                    is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
//
//                    is Resource.Success -> {
//
//                        val rates = ratesResponse.data!!.rates
//                        val rate: Double = getRateForCurrency(toCurrency, rates) as Double
//
//                        if(rate == null) {
//                            _conversion.value = CurrencyEvent.Failure("Unexpected error")
//                        } else {
//
//                            val convertedCurrency = round((fromAmount * rate * 100.0)) / 100
//                            _conversion.value = CurrencyEvent.Success("$fromAmount = $convertedCurrency $toCurrency")
//                        }
//                    }
//
//                }





                //users.postValue(Resource.success(usersFromApi))
            } catch (e: Exception) {
                _mainEvent.value = MainEvent.Failure(e.message!!)
            }





//            _conversion.value = CurrencyEvent.Loading
//
//            when(val ratesResponse = repository.getRates(toCurrency)) {
//
//                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
//
//                is Resource.Success -> {
//
//                    val rates = ratesResponse.data!!.rates
//                    val rate: Double = getRateForCurrency(toCurrency, rates) as Double
//
//                    if(rate == null) {
//                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
//                    } else {
//
//                        val convertedCurrency = round((fromAmount * rate * 100.0)) / 100
//                        _conversion.value = CurrencyEvent.Success("$fromAmount = $convertedCurrency $toCurrency")
//                    }
//                }
//
//            }
        }
    }


    sealed class MainEvent {
        class Success(val resultText: String): MainEvent()
        class Failure(val errorText: String): MainEvent()
        object Loading : MainEvent()
        object Empty : MainEvent()
    }


}