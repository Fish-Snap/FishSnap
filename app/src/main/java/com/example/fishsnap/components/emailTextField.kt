package com.example.fishsnap.components

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.fishsnap.R

class emailTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var handler = Handler(Looper.getMainLooper())
    private val emailValidationRunnable = Runnable {
        val text = text.toString()

        if (text.length > 5 && !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            error = context.getString(R.string.error_email)
        }
    }

    private var isFieldFilled = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isFieldFilled = s.toString().isNotEmpty()
                handler.removeCallbacks(emailValidationRunnable)
                handler.postDelayed(emailValidationRunnable, 500)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        return super.onTouchEvent(event)
    }
}