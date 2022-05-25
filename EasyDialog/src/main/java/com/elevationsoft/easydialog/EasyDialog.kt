package com.elevationsoft.easydialog

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

@Suppress("unused")
class EasyDialog private constructor(private val ctx: Context) {
    private var alertDialog: AlertDialog? = null
    private var bg: Int
    private var fg: Int
    private var primaryButtonBg: Int
    private var primaryButtonFg: Int
    private var secondaryButtonBg: Int
    private var secondaryButtonFg: Int
    private var showTitle = true
    private var showMsg = true
    private var showBtn = true
    private var showBtnSecondary = false
    private var cancelable = false
    private var txtTitle = ""
    private var txtMsg = ""
    private var txtBtn = ""
    private var txtBtnSecondary = ""
    private var dismissListener: OnDismissListener? = null
    private var primaryButtonClickListener: OnButtonClickListener? = null


    init {
        bg = ContextCompat.getColor(ctx, R.color.easy_dialog_bg)
        fg = ContextCompat.getColor(ctx, R.color.easy_dialog_text_color_primary)
        primaryButtonBg = ContextCompat.getColor(ctx, R.color.easy_dialog_button_bg_primary)
        primaryButtonFg = ContextCompat.getColor(ctx, R.color.easy_dialog_text_color_secondary)
        secondaryButtonBg = ContextCompat.getColor(ctx, R.color.easy_dialog_button_bg_secondary)
        secondaryButtonFg = ContextCompat.getColor(ctx, R.color.easy_dialog_text_color_secondary)
    }

    fun setTheme(bg: Int, fg: Int, pb_bg: Int, pb_fg: Int, sb_bg: Int, sb_fg: Int): EasyDialog {
        this.bg = bg
        this.fg = fg
        this.primaryButtonBg = pb_bg
        this.primaryButtonFg = pb_fg
        this.secondaryButtonBg = sb_bg
        this.secondaryButtonFg = sb_fg
        return this
    }

    fun setTitle(show: Boolean, title: String): EasyDialog {
        showTitle = show
        txtTitle = title
        return this
    }

    fun setMessage(show: Boolean, msg: String): EasyDialog {
        showMsg = show
        txtMsg = msg
        return this
    }

    fun setPrimaryButton(show: Boolean, label: String): EasyDialog {
        showBtn = show
        txtBtn = label
        return this
    }

    fun setSecondaryButton(show: Boolean, label: String): EasyDialog {
        showBtnSecondary = show
        txtBtnSecondary = label
        return this
    }

    fun setCancelable(cancelable: Boolean): EasyDialog {
        this.cancelable = cancelable
        return this
    }

    fun setOnDismissListener(dismissListener: OnDismissListener?): EasyDialog {
        this.dismissListener = dismissListener
        return this
    }

    fun setOnButtonClickListener(primaryButtonClickListener: OnButtonClickListener?): EasyDialog {
        this.primaryButtonClickListener = primaryButtonClickListener
        return this
    }

    fun show(): AlertDialog {
        val builder = AlertDialog.Builder(ctx)
        val layout = LayoutInflater.from(ctx).inflate(R.layout.layout_dialog, null)
        builder.setView(layout)
        builder.setCancelable(cancelable)
        alertDialog = builder.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val clParent = layout.findViewById<ConstraintLayout>(R.id.cl_parent)
        clParent.background.setColorFilter(bg, PorterDuff.Mode.SRC_IN)
        val tvTitle = layout.findViewById<TextView>(R.id.tv_title)
        tvTitle.setTextColor(fg)
        val tvMsg = layout.findViewById<TextView>(R.id.tv_message)
        tvMsg.setTextColor(fg)
        val btnYes = layout.findViewById<Button>(R.id.btn_yes)
        val btnNo = layout.findViewById<Button>(R.id.btn_no)
        if (showTitle) {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = txtTitle
        } else {
            tvTitle.visibility = View.GONE
        }
        if (showMsg) {
            tvMsg.visibility = View.VISIBLE
            tvMsg.text = txtMsg
        } else {
            tvMsg.visibility = View.GONE
        }
        if (showBtn) {
            btnYes.visibility = View.VISIBLE
            btnYes.text = txtBtn
            btnYes.setTextColor(primaryButtonFg)
            btnYes.background.setColorFilter(primaryButtonBg, PorterDuff.Mode.SRC_IN)
            btnYes.setOnClickListener {
                if (primaryButtonClickListener != null) {
                    primaryButtonClickListener!!.onPrimaryButtonClick()
                }
                alertDialog!!.dismiss()
            }
        } else {
            btnYes.visibility = View.GONE
        }
        if (showBtnSecondary) {
            btnNo.visibility = View.VISIBLE
            btnNo.text = txtBtnSecondary
            btnNo.setTextColor(secondaryButtonFg)
            btnNo.background.setColorFilter(secondaryButtonBg, PorterDuff.Mode.SRC_IN)
            btnNo.setOnClickListener {
                if (primaryButtonClickListener != null) {
                    primaryButtonClickListener!!.onSecondaryButtonClick()
                }
                alertDialog!!.dismiss()
            }
        } else {
            btnNo.visibility = View.GONE
        }
        alertDialog!!.setOnDismissListener {
            if (dismissListener != null) {
                dismissListener!!.onDismiss()
            }
        }
        alertDialog!!.show()
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return alertDialog!!
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    interface OnButtonClickListener {
        fun onPrimaryButtonClick()
        fun onSecondaryButtonClick()
    }

    companion object {
        fun with(ctx: Context): EasyDialog {
            return EasyDialog(ctx)
        }
    }

}