package com.example.storyapp.ui

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText : AppCompatEditText {

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isEmailValid() && !(text.isNullOrEmpty()))
                    error = "Email Tidak Valid"
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun isEmailValid(): Boolean {
        if (!(text.toString().trim { it <= ' ' }.matches(emailPattern.toRegex())))
            return false
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Masukkan email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}