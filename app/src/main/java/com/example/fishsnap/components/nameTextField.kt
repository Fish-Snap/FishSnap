package com.example.fishsnap.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.fishsnap.R

class nameTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    private var handler = Handler(Looper.getMainLooper())
    private val nameValidationRunnable = Runnable {
        val text = text.toString()
        if (text.isEmpty()) {
            error = context.getString(R.string.error_null)
        }
    }

    private var isFieldFilled = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(nameValidationRunnable)
                handler.postDelayed(nameValidationRunnable, 500)
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