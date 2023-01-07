package com.kamuran.earthquake.servis

import com.kamuran.earthquake.model.Besin
import io.reactivex.Single
import retrofit2.http.GET

interface BesinApı {


    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    fun getBesin():Single<List<Besin>>   //single rxjava için
    //fun getBesin():Call<List<Besin>> böylede olurdu
}