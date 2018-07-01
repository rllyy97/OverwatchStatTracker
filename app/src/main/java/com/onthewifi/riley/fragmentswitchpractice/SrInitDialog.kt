package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class SrInitDialog: DialogFragment()  {
    interface OnInputListener {
        fun sendInput(input: Int)
    }
    private lateinit var onInputListener: OnInputListener

    private lateinit var srEdit: EditText
    private lateinit var submitButton: ImageButton
    private var sr: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sr_init_dialog, container, false)
        onInputListener = targetFragment as SrInitDialog.OnInputListener

        srEdit = view.findViewById(R.id.srEdit)
        submitButton = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener(submit)

        dialog.requestWindowFeature(STYLE_NO_TITLE)
        isCancelable = false

        return view
    }

    private val submit = View.OnClickListener {
        if (!srEdit.text.isEmpty()) {
            val newSr = srEdit.text.toString().toInt()
            if (newSr > 5000 || newSr < 0) Toast.makeText(context, "Please input a valid SR", Toast.LENGTH_SHORT).show()
            else {
                onInputListener.sendInput(newSr)
                dialog.dismiss()
            }
        }
    }
}