package com.artemissoftware.parallelmultipleapirequests

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemissoftware.parallelmultipleapirequests.repository.CryptoCurrencyRepository
import com.artemissoftware.parallelmultipleapirequests.repository.JsonPlaceHolderRepository
import com.artemissoftware.parallelmultipleapirequests.util.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

            } catch (e: Exception) {

                _mainEvent.value = MainEvent.Failure(e.message!!)
            }

        }
    }


    fun getError() {

        viewModelScope.launch(dispatcherProvider.io) {

            _mainEvent.value = MainEvent.Loading

            try {
                cryptoCurrencyRepository.getError()
                _mainEvent.value = MainEvent.Empty
            }
            catch (e:Exception){
                _mainEvent.value = MainEvent.Failure( e.message!! + " - " + e.cause?.message)
            }
        }
    }




    fun parallelRequest() {
        viewModelScope.launch {
            coroutineScope {

                _mainEvent.value = MainEvent.Loading

                val call1 = async {  cryptoCurrencyRepository.getCoinData("LTC") }
                val call2 = async { cryptoCurrencyRepository.getCoinData("DOGE") }

                try {
                    val ltc = call1.await()
                    val doge = call2.await()

                    _mainEvent.value = MainEvent.Success("LTC has  ${ltc.ticker.markets.size} markets and DOGE has  ${doge.ticker.markets.size} markets")

                } catch (e: Exception) {
                    _mainEvent.value = MainEvent.Failure(e.message!!)
                }

            }
        }
    }



    fun parallelRequestError() {

        viewModelScope.launch {
            try {
                coroutineScope {

                    _mainEvent.value = MainEvent.Loading

                    val call1 = async { cryptoCurrencyRepository.getCoinData("LTC") }
                    val call2 = async { cryptoCurrencyRepository.getCoinData("DOGE") }
                    val call3 = async { cryptoCurrencyRepository.getError() }

                    try {
                        val ltc = call1.await()
                        val doge = call2.await()
                        val error = call3.await()

                        _mainEvent.value = MainEvent.Success("LTC has  ${ltc.ticker.markets.size} markets and DOGE has  ${doge.ticker.markets.size} markets")

                    } catch (e: Exception) {
                        _mainEvent.value = MainEvent.Failure(e.message!!)
                    }
                }

            }
            catch (e: Exception) {
                _mainEvent.value = MainEvent.Failure(e.message!!)
            }
        }
    }


    sealed class MainEvent {
        class Success(val resultText: String): MainEvent()
        class Failure(val errorText: String): MainEvent()
        object Loading : MainEvent()
        object Empty : MainEvent()
    }


}