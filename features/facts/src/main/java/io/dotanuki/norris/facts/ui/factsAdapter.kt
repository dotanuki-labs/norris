package io.dotanuki.norris.facts.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.dotanuki.norris.facts.R
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation

class FactsAdapter(
    private val presentation: FactsPresentation,
    private val shareAction: (FactDisplayRow) -> Unit
) : RecyclerView.Adapter<FactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_fact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = presentation.facts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(presentation.facts[position], shareAction)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(row: FactDisplayRow, action: (FactDisplayRow) -> Unit) {
            itemView.run {

                val factLabel = itemView.findViewById<TextView>(R.id.factLabel)

                val appearance =
                    if (row.displayWithSmallerFontSize) R.style.BigFact
                    else R.style.SmallFact

                factLabel.setTextAppearance(appearance)
                factLabel.text = row.fact

                setOnClickListener { action.invoke(row) }
            }
        }
    }
}
