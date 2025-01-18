package com.example.finalprojectpam.ui.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.finalprojectpam.databinding.FragmentAssetBinding

class AssetFragment : Fragment() {

private var _binding: FragmentAssetBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val dashboardViewModel =
        ViewModelProvider(this).get(AssetViewModel::class.java)

    _binding = FragmentAssetBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textAsset
    dashboardViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}