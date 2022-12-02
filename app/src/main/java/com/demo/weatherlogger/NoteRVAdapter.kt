package com.demo.weatherlogger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRVAdapter(
    val context: Context,
    val noteClickDeleteInterface: NoteClickDeleteInterface,
    val noteClickInterface: NoteClickInterface
) :
    RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private val allWeathers = ArrayList<Weather>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTV = itemView.findViewById<TextView>(R.id.idTVNote)
        val dateTV = itemView.findViewById<TextView>(R.id.idTVDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.setText("Temperature : "+allWeathers.get(position).temp + " \u212A")
        holder.dateTV.setText("Date : "+allWeathers.get(position).date)


        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allWeathers.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allWeathers.size
    }

    fun updateList(newList: List<Weather>) {
        allWeathers.clear()
        allWeathers.addAll(newList)
        notifyDataSetChanged()
    }

}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(weather: Weather)
}

interface NoteClickInterface {
    fun onNoteClick(weather: Weather)
}