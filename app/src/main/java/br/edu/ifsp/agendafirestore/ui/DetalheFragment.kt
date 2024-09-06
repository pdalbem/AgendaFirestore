package br.edu.ifsp.agendafirestore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendafirestore.R
import br.edu.ifsp.agendafirestore.databinding.FragmentDetalheBinding
import br.edu.ifsp.agendafirestore.model.Contato
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null
    private val binding get() = _binding!!

    lateinit  var nomeEditText: EditText
    lateinit var foneEditText: EditText
    lateinit var emailEditText: EditText


    val db = Firebase.firestore
    lateinit var idContato: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetalheBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nomeEditText = binding.commonLayout.editTextNome
        foneEditText = binding.commonLayout.editTextFone
        emailEditText = binding.commonLayout.editTextEmail

        idContato = requireArguments().getString("idContato").toString()

        db.collection("contatos")
            .document(idContato)
            .addSnapshotListener { value, error ->
                if (value!=null)
                {
                    val c = value.toObject<Contato>()

                    val nome = view.findViewById<EditText>(R.id.editTextNome)
                    val fone = view.findViewById<EditText>(R.id.editTextFone)
                    val email = view.findViewById<EditText>(R.id.editTextEmail)

                    nome?.setText(c?.nome.toString())
                    fone?.setText(c?.fone.toString())
                    email?.setText(c?.email.toString())

                }
            }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.detalhe_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_alterarContato -> {

                        val contato= Contato()
                        contato.nome=nomeEditText.text.toString()
                        contato.fone=foneEditText.text.toString()
                        contato.email=emailEditText.text.toString()

                        db.collection("contatos")
                            .document(idContato)
                            .set(contato)

                        Snackbar.make(binding.root, "Contato alterado", Snackbar.LENGTH_SHORT).show()

                        findNavController().popBackStack()
                        true
                    }
                    R.id.action_excluirContato ->{
                        db.collection("contatos")
                            .document(idContato).delete()

                        Snackbar.make(binding.root, "Contato apagado", Snackbar.LENGTH_SHORT).show()

                        findNavController().popBackStack()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

}