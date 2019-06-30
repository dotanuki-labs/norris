package io.dotanuki.norris.facts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_fact.view.*

class FactsAdapter(
    private val presentation: FactsPresentation,
    private val shareAction: (FactDisplayRow) -> Unit
) : RecyclerView.Adapter<FactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_fact, parent, false)
        return FactHolder(view)
    }

    override fun getItemCount() = presentation.facts.size

    override fun onBindViewHolder(holder: FactHolder, position: Int) {
        holder.bind(presentation.facts[position], shareAction)
    }
}

class FactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(row: FactDisplayRow, action: (FactDisplayRow) -> Unit) {
        itemView.run {

            val appearance =
                if (row.displayWithSmallerFontSize) R.style.BigFact
                else R.style.SmallFact

            factLabel.setTextAppearance(appearance)
            factLabel.text = row.fact

            setOnClickListener { action.invoke(row) }
        }
    }
}