package com.kamuran.earthquake.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kamuran.earthquake.model.Besin
import com.kamuran.earthquake.servis.BesinAPIservis
import com.kamuran.earthquake.servis.BesinDatabase
import com.kamuran.earthquake.util.OzelSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application):BaseViewModel(application) {

    val besinler= MutableLiveData<List<Besin>>()
    val besinHataMesaji= MutableLiveData<Boolean>()
    val besinYukleniyor= MutableLiveData<Boolean>()
    private var guncellemeZamani=0.2*60*1000*1000*1000L

    private val besinApiServis= BesinAPIservis()
    private val disposable= CompositeDisposable()  //kullan at
    private val ozelSharedPreferences= OzelSharedPreferences(getApplication())


    fun refreshData(){

        val kaydedilmeZamani=ozelSharedPreferences.ZamaniAl()
        if (kaydedilmeZamani !=null && kaydedilmeZamani !=0L && System.nanoTime()-kaydedilmeZamani<guncellemeZamani)
        {//sqlite den al
            verileriSqlitedenAl()
            }else{
            verileriInternettenAl()
        }

    }

    fun refreshFromInternet(){
        verileriInternettenAl()
    }

    private fun verileriSqlitedenAl(){

        besinYukleniyor.value=true
        launch {
            val besinListesi= BesinDatabase(getApplication()).besinDao().getAllBesin()

            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"sqliteden aldık",Toast.LENGTH_LONG).show()
        }
    }

    private fun verileriInternettenAl(){
        besinYukleniyor.value=true

        disposable.add(
            besinApiServis.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Besin>>(){
                    override fun onSuccess(t: List<Besin>) {
                        //başarı
                        sqLiteSakla(t)
                        Toast.makeText(getApplication(),"internetten aldık",Toast.LENGTH_LONG).show()


                       /*
                        besinler.value=t
                        besinHataMesaji.value=false
                        besinYukleniyor.value=false
                        */
                    }

                    override fun onError(e: Throwable) {
                        //hata
                        besinHataMesaji.value=true
                        besinYukleniyor.value=false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun besinleriGoster(besinlerListesi: List<Besin>){
        besinler.value=besinlerListesi
        besinHataMesaji.value= false
        besinYukleniyor.value= false
    }

    private fun sqLiteSakla(besinListesi: List<Besin>){

        launch {
            val dao= BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
         val uuidListesi=   dao.insertAll(*besinListesi.toTypedArray())

            var i=0
            while (i<besinListesi.size){
                besinListesi[i].uuid= uuidListesi[i].toInt()
                i=i+1

            }
            besinleriGoster(besinListesi)
        }
        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }
}