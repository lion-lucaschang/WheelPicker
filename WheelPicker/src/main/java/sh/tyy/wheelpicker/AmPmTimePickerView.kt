package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import sh.tyy.wheelpicker.core.BaseWheelPickerView
import sh.tyy.wheelpicker.core.TextWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView
import sh.tyy.wheelpicker.databinding.TriplePickerViewBinding
import java.util.Locale

class AmPmTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener {

    interface Listener {
        fun didSelectData(isAm: Boolean, hour: Int, minute: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
    private val amPmPickerView: TextWheelPickerView
    private val hourPickerView: TextWheelPickerView
    private val minutePickerView: TextWheelPickerView

    private var listener: Listener? = null

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    var isAm: Boolean
        set(value) {
            amPmPickerView.selectedIndex = if (value) 0 else 1
        }
        get() = amPmPickerView.selectedIndex == 0

    var hour: Int
        set(value) {
            hourPickerView.selectedIndex = value - 1
        }
        get() = hourPickerView.selectedIndex + 1

    var minute: Int
        set(value) {
            minutePickerView.selectedIndex = value
        }
        get() = minutePickerView.selectedIndex

    var isCircular: Boolean = false
        set(value) {
            field = value
            amPmPickerView.isCircular = value
            hourPickerView.isCircular = value
            minutePickerView.isCircular = value
        }

    private val amPmAdapter = TextWheelAdapter()
    private val hourAdapter = TextWheelAdapter()
    private val minuteAdapter = TextWheelAdapter()

    private val binding: TriplePickerViewBinding =
        TriplePickerViewBinding.inflate(LayoutInflater.from(context), this)

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        amPmPickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
        hourPickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
        minutePickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    init {
        amPmPickerView = binding.leftPicker
        amPmPickerView.setAdapter(amPmAdapter)
        amPmAdapter.values =
            listOf(
                TextWheelPickerView.Item("am", "AM"),
                TextWheelPickerView.Item("pm", "PM")
            )
        amPmPickerView.selectedIndex = 0

        hourPickerView = binding.midPicker
        hourPickerView.setAdapter(hourAdapter)
        hourAdapter.values = (1..12).map {
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

        amPmPickerView.setWheelListener(this)
        hourPickerView.setWheelListener(this)
        minutePickerView.setWheelListener(this)
    }

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        listener?.didSelectData(isAm, hour, minute)
    }
    // endregion
}