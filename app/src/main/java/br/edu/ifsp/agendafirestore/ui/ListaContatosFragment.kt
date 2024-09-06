package br.edu.ifsp.agendafirestore.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.agendafirestore.R
import br.edu.ifsp.agendafirestore.adapter.ContatoAdapter
import br.edu.ifsp.agendafirestore.databinding.FragmentListaContatosBinding
import br.edu.ifsp.agendafirestore.model.Contato
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListaContatosFragment : Fragment(){

    private var _binding: FragmentListaContatosBinding? = null

    private val binding get() = _binding!!

    lateinit var contatoAdapter: ContatoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListaContatosBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_listaContatosFragment_to_cadastroFragment) }

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {

                        lateinit var query:Query

                        if (p0!="") {
                            query = Firebase.firestore
                                .collection("contatos")
                                .whereGreaterThanOrEqualTo("nome", p0.toString())
                                .whereLessThanOrEqualTo("nome",p0.toString()+"\uf8ff")
                        }
                        else
                        {
                             query= Firebase.firestore.collection("contatos").orderBy("nome")

                        }

                        val newOptions: FirestoreRecyclerOptions<Contato> = FirestoreRecyclerOptions
                            .Builder<Contato>()
                            .setQuery(query, Contato::class.java).build()

                        contatoAdapter.updateOptions(newOptions)
                        contatoAdapter.notifyDataSetChanged()

                        return true
                    }

                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                TODO("Not yet implemented")
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    private fun configureRecyclerView()
    {

        val db = Firebase.firestore
        val query: Query = db.collection("contatos").orderBy("nome")
        val options: FirestoreRecyclerOptions<Contato> =
            FirestoreRecyclerOptions.Builder<Contato>()
                .setLifecycleOwner(this)
                .setQuery(query, Contato::class.java).build()

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        contatoAdapter = ContatoAdapter(options)
        recyclerView.adapter = contatoAdapter

        val listener = object : ContatoAdapter.ContatoListener {
            override fun onItemClick(pos: Int) {
                val bundle = bundleOf("idContato" to contatoAdapter.snapshots.getSnapshot(pos).id)

                findNavController().navigate(
                    R.id.action_listaContatosFragment_to_detalheFragment,
                    bundle
                )

            }
        }
        contatoAdapter.setClickListener(listener)
           }

}


