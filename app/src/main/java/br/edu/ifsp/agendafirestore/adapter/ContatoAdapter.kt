package br.edu.ifsp.agendafirestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.agendafirestore.databinding.ContatoCelulaBinding
import br.edu.ifsp.agendafirestore.model.Contato
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ContatoAdapter(options: FirestoreRecyclerOptions<Contato>)
    : FirestoreRecyclerAdapter<Contato, ContatoAdapter.ContatoViewHolder>(options)
{
    var listener: ContatoListener?=null

    private lateinit var binding: ContatoCelulaBinding

    fun setClickListener(listener: ContatoListener)
    {
        this.listener = listener
    }

    inner class ContatoViewHolder(view: ContatoCelulaBinding): RecyclerView.ViewHolder(view.root)
    {
        val nomeVH = view.nome
        val foneVH = view.fone

        init {
            view.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContatoViewHolder {

        binding = ContatoCelulaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  ContatoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int, model: Contato) {
        holder.nomeVH.text = model.nome
        holder.foneVH.text = model.fone
    }

    interface ContatoListener
    {
        fun onItemClick(pos: Int)
    }


}

