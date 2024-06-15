package hu.bme.aut.fna1a3.shoppinglist2.network

import hu.bme.aut.fna1a3.shoppinglist2.model.Money
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoneyAPI {
    @GET("/latest")
    fun getMoney(@Query("from") base: String) : Call<Money>

}