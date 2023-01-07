package com.kamuran.earthquake.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamuran.earthquake.R
import com.kamuran.earthquake.adapter.BesinRecyclerAdapter
import com.kamuran.earthquake.viewmodel.BesinListesiViewModel
import kotlinx.android.synthetic.main.fragment_besin_listesi.*


class BesinListesiFragment : Fragment() {

    private lateinit var viewModel: BesinListesiViewModel
    private val recyclerBesinAdapter= BesinRecyclerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_listesi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProviders.of(this).get(BesinListesiViewModel::class.java)
        viewModel.refreshData()

        besinListrecyclerView.layoutManager= LinearLayoutManager(context)
        besinListrecyclerView.adapter=recyclerBesinAdapter

        swipeRefreshLayout.setOnRefreshListener {
            besinYukleniyor.visibility=View.VISIBLE
            besinHatamesaji.visibility=View.GONE
            besinListrecyclerView.visibility=View.GONE
            viewModel.refreshFromInternet()
            swipeRefreshLayout.isRefreshing=false

        }

observeLiveData()
    }

    fun observeLiveData(){

        viewModel.besinler.observe(viewLifecycleOwner, Observer { besinler->
            besinler.let {
                besinListrecyclerView.visibility= View.VISIBLE
                recyclerBesinAdapter.besinListesiniGuncelle(besinler)
            }
        })

        viewModel.besinHataMesaji.observe(viewLifecycleOwner, Observer {
            hata->
            hata?.let {
                if (it){

                    besinHatamesaji.visibility= View.VISIBLE
                    besinListrecyclerView.visibility=View.GONE

                }else{
                    besinHatamesaji.visibility=View.GONE
                }
        }
        })

        viewModel.besinYukleniyor.observe(viewLifecycleOwner, Observer {
            yukleniyor->
            yukleniyor?.let {
                if (it){
besinListrecyclerView.visibility=View.GONE
                    besinHatamesaji.visibility=View.GONE
                    besinYukleniyor.visibility=View.VISIBLE
                }else{
                    besinYukleniyor.visibility= View.GONE

                }
            }
        })
    }


}