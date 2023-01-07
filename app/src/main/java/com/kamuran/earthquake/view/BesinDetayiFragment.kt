package com.kamuran.earthquake.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kamuran.earthquake.R
import com.kamuran.earthquake.util.gorselIndir
import com.kamuran.earthquake.util.placeHolderYap
import com.kamuran.earthquake.viewmodel.BesinDetayiViewModel
import kotlinx.android.synthetic.main.fragment_besin_detayi.*


class BesinDetayiFragment : Fragment() {
private lateinit var viewModel:BesinDetayiViewModel
    private var besinId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_detayi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            besinId= BesinDetayiFragmentArgs.fromBundle(it).besinId

        }

        viewModel=ViewModelProviders.of(this).get(BesinDetayiViewModel::class.java)
        viewModel.roomVerisiniAl(besinId)


observeLiveData()
    }

    fun observeLiveData(){
        viewModel.besinLiveData.observe(viewLifecycleOwner, Observer {
            besin->
            besin.let {
                besinIsim.text=it.besinIsim
                besinkalori.text= it.besinKalori
                besinKarbonhidrat.text= it.besinKarbonhidrat
                besinProtein.text=it.besinProtein
                besinyag.text= it.besinYag

                context?.let {
                    besinImage.gorselIndir(besin.besinGorsel, placeHolderYap(it))
                }
            }
        })
    }

}