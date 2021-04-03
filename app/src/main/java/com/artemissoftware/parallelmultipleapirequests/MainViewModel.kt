package com.artemissoftware.parallelmultipleapirequests

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemissoftware.parallelmultipleapirequests.api.models.Crypto
import com.artemissoftware.parallelmultipleapirequests.repository.CryptoCurrencyRepository
import com.artemissoftware.parallelmultipleapirequests.repository.JsonPlaceHolderRepository
import com.artemissoftware.parallelmultipleapirequests.util.DispatcherProvider
import com.artemissoftware.parallelmultipleapirequests.util.extensions.asyncAll
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        viewModelScope.launch(dispatcherProvider.io) {
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

                //clean code

//                var result = ""
//                val listCrypto = mutableListOf<String>("LTC", "DOGE")
//
//                asyncAll(listCrypto) { cryptoCurrencyRepository.getCoinData(it) }.awaitAll().forEach { result += "${it.success} has  ${it.ticker.markets.size} markets  "}
//
//                _mainEvent.value = MainEvent.Success(result)

            }
        }
    }



    fun parallelRequestError() {

        viewModelScope.launch(dispatcherProvider.io) {
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

                    //clean code

//                var result = ""
//                val listCrypto = mutableListOf<String>("LTC", "DOGE")
//                asyncAll(listCrypto) { cryptoCurrencyRepository.getError() }.awaitAll().forEach { result += "${it.success} has  ${it.ticker.markets.size} markets  "}
//                _mainEvent.value = MainEvent.Success(result)
                }

            }
            catch (e: Exception) {
                _mainEvent.value = MainEvent.Failure(e.message!!)
            }
        }
    }



    fun parallelRequestSurviveError() {

        viewModelScope.launch(dispatcherProvider.io) {
            supervisorScope {


                val call1 = async { cryptoCurrencyRepository.getCoinData("LTC") }
                val call2 = async { cryptoCurrencyRepository.getCoinData("DOGE") }
                val call3 = async { cryptoCurrencyRepository.getError() }

                val ltc = try {
                    call1.await()
                } catch (ex: Exception) {
                    null
                }
                val doge = try {
                    call2.await()
                } catch (ex: Exception) {
                    null
                }
                val error = try {
                    call3.await()
                } catch (ex: Exception) {
                    ex.message
                }

                _mainEvent.value = MainEvent.Success("LTC has  ${ltc?.ticker?.markets?.size} markets and DOGE has  ${doge?.ticker?.markets?.size} markets and ERROR ${error}")
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