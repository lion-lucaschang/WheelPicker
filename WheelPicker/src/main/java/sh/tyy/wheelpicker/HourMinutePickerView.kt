package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import sh.tyy.wheelpicker.core.BaseWheelPickerView
import sh.tyy.wheelpicker.core.TextWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView
import sh.tyy.wheelpicker.databinding.DoublePickerViewBinding
import java.util.Locale

class HourMinutePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener {

    interface Listener {
        fun didSelectData(hour: Int, minute: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
    private val hourPickerView: TextWheelPickerView
    private val minutePickerView: TextWheelPickerView

    private var listener: Listener? = null

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    var hour: Int
        set(value) {
            hourPickerView.selectedIndex = value
        }
        get() = hourPickerView.selectedIndex

    var minute: Int
        set(value) {
            minutePickerView.selectedIndex = value
        }
        get() = minutePickerView.selectedIndex

    var isCircular: Boolean = false
        set(value) {
            field = value
            hourPickerView.isCircular = value
            minutePickerView.isCircular = value
        }

    private val hourAdapter = TextWheelAdapter()
    private val minuteAdapter = TextWheelAdapter()

    private val binding: DoublePickerViewBinding =
        DoublePickerViewBinding.inflate(LayoutInflater.from(context), this)

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        hourPickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
        minutePickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    init {
        hourPickerView = binding.leftPicker
        hourPickerView.setAdapter(hourAdapter)
        hourAdapter.values = (0 until 24).map {
            TextWheelPickerView.Item(
                "$it",
                String.format(Locale.US, "%02d", it)
            )
        }
        hourPickerView.selectedIndex = 0

        minutePickerView = binding.rightPicker
        minutePickerView.setAdapter(minuteAdapter)
        minuteAdapter.values =
            (0 until 60).map {
                TextWheelPickerView.Item(
                    "$it",
                    String.format(Locale.US, "%02d", it)
                )
            }
        minutePickerView.selectedIndex = 0

        addView(highlightView, 0)
        (highlightView.layoutParams as? LayoutParams)?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }

        hourPickerView.setWheelListener(this)
        minutePickerView.setWheelListener(this)
    }

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        listener?.didSelectData(hour, minute)
    }
    // endregion
}