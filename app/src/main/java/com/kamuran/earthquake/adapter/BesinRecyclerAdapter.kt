package com.kamuran.earthquake.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kamuran.earthquake.R
import com.kamuran.earthquake.model.Besin
import com.kamuran.earthquake.util.gorselIndir
import com.kamuran.earthquake.util.placeHolderYap
import com.kamuran.earthquake.view.BesinListesiFragmentDirections
import kotlinx.android.synthetic.main.besin_recycler_row.view.*
import kotlinx.android.synthetic.main.fragment_besin_detayi.view.*

class BesinRecyclerAdapter(val besinListesi:ArrayList<Besin>):RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>(){

    class BesinViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
val inflater= LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.besin_recycler_row,parent,false)
        return BesinViewHolder(view)
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        holder.itemView.isim.text= besinListesi.get(position).besinIsim
        holder.itemView.kalori.text= besinListesi.get(position).besinKalori
        //görsel eklenecek

        holder.itemView.setOnClickListener {
            val action=BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinDetayiFragment(besinListesi.get(position).uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.itemView.imageView.gorselIndir(besinListesi.get(position).besinGorsel,
            placeHolderYap(holder.itemView.context)
        )
    }

    override fun getItemCount(): Int {
return besinListesi.size   }


    fun besinListesiniGuncelle(yeniBesinListesi:List<Besin>){
        besinListesi.clear()
        besinListesi.addAll(yeniBesinListesi)
        notifyDataSetChanged()

    }
}