package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import eu.pl.snk.senseibunny.trelloclone.R
import models.Board

open class BoardAdapter(private val context: Context, private val list: ArrayList<Board> ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()


    private var clickListner: onClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide.with(context)
                .load(model.image)
                .apply(requestOptions)
                .into(holder.itemView.findViewById(R.id.imageViewBoard))

            holder.itemView.findViewById<TextView>(R.id.name_board).text=model.name.toString()
            holder.itemView.findViewById<TextView>(R.id.created_by_biard).text="Created by "+ model.createdBy.toString()

            holder.itemView.setOnClickListener{
                if(clickListner!=null){
                    clickListner!!.onClick(position,model)
                }
            }
        }
    }

    interface onClickListener{
        fun onClick(position: Int, model:Board)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}