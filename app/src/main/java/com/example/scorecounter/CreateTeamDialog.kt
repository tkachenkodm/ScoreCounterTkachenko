package com.example.scorecounter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.scorecounter.databinding.CreateTeamDialogFragmentBinding
import kotlinx.android.synthetic.main.create_team_dialog_fragment.*

class CreateTeamDialog : DialogFragment() {
    lateinit var binding: CreateTeamDialogFragmentBinding
    private var callback: ((String, Int) -> (Unit))? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateTeamDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setTextWatcher()
    }


    fun setCallback(callback: (String, Int) -> (Unit)) {
        this.callback = callback
    }

    private fun setTextWatcher() {
        ti_teamName.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    text?.trim()?.let {
                        if (it.isEmpty()) {
                            binding.tilTeamName.error = getString(R.string.no_name_error)
                        } else {
                            binding.tilTeamName.error = null
                        }

                        binding.btnCreateTeam.isEnabled =
                            it.length <= binding.tilTeamName.counterMaxLength
                    }
                }

            }
        )
    }

    private fun setClickListeners() {
        binding.btnCreateTeam.setOnClickListener {
            binding.tiTeamName.text?.trim()?.let {
                if (it.isNotEmpty()) {
                    val name = it.toString()
                    val color = binding.colorSlider.selectedColor
                    callback?.invoke(name, color)

                    dismiss()
                } else {
                    binding.tilTeamName.error = getString(R.string.no_name_error)
                }
            }
        }

    }
}