package com.example.finalprojectpam.ui.pengeluaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalprojectpam.databinding.FragmentPendapatanBinding
import com.example.finalprojectpam.databinding.FragmentPengeluaranBinding
import com.example.finalprojectpam.ui.pendapatan.PendapatanViewModel

class PengeluaranFragment : Fragment() {

    private var _binding: FragmentPengeluaranBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(PengeluaranViewModel::class.java)

        _binding = FragmentPengeluaranBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPengeluaran
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}