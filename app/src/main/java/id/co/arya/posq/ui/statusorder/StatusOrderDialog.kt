package id.co.arya.posq.ui.statusorder

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import kotlinx.android.synthetic.main.fragment_status_payment_dialog.*

@AndroidEntryPoint
class StatusOrderDialog(val strukdata: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_status_payment_dialog, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        img_status_order.setImageDrawable(activity?.resources?.getDrawable(R.drawable.ic_success, null))
        struk.text = strukdata
        close.setOnClickListener {
            dismiss()
            activity?.finish()
        }
        button_done.setOnClickListener {
            dismiss()
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

}