package com.example.storyapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var showHideButtonImage: Drawable

    private var isShow = false

    private fun init() {

        showHideButtonImage = ContextCompat.getDrawable(
            context,
            R.drawable.show_password
        ) as Drawable

        setOnTouchListener(this)

        transformationMethod = PasswordTransformationMethod.getInstance()

        setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) showButton() else hideButton()
                if (lessThanSix() && !(text.isNullOrEmpty()))
                    error = "Password harus lebih dari 6 karakter"
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

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        val showHideButtonStart: Float
        val showHideButtonEnd: Float
        var isShowHideButtonClicked = false
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            showHideButtonEnd = (showHideButtonImage.intrinsicWidth + paddingStart).toFloat()
            when {
                event.x < showHideButtonEnd -> isShowHideButtonClicked = true
            }
        } else {
            showHideButtonStart =
                (width - paddingEnd - showHideButtonImage.intrinsicWidth).toFloat()
            when {
                event.x > showHideButtonStart -> isShowHideButtonClicked = true
            }
        }
        if (isShowHideButtonClicked) {
            if (event.action == MotionEvent.ACTION_UP) {
                changePasswordVisibility()
                isShow = !isShow

                return true
            }
            return true
        }
        return false
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Masukkan password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = showHideButtonImage)

    }

    private fun hideButton() {
        setButtonDrawables()
    }

    private fun changePasswordVisibility() {
        if (isShow) {
            showHideButtonImage = ContextCompat.getDrawable(
                context,
                R.drawable.show_password
            ) as Drawable
            transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            showHideButtonImage = ContextCompat.getDrawable(
                context,
                R.drawable.hide_password
            ) as Drawable
            transformationMethod = HideReturnsTransformationMethod.getInstance()

        }
    }

    private fun lessThanSix(): Boolean {
        val password = text?:""
        val length = password.length
        return length < 6
    }
}