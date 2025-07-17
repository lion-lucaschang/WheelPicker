package sh.tyy.wheelpicker

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import sh.tyy.wheelpicker.core.BaseWheelPickerView
import sh.tyy.wheelpicker.core.TextWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView
import sh.tyy.wheelpicker.databinding.LongTriplePickerViewBinding
import java.time.LocalDate
import java.util.Locale

class YmdTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseWheelPickerView.WheelPickerViewListener {

    interface Listener {
        fun didSelectData(date: LocalDate, hour: Int, minute: Int)
    }

    private val highlightView: View = run {
        val view = View(context)
        view.background = ContextCompat.getDrawable(context, R.drawable.text_wheel_highlight_bg)
        view
    }
    private val datePickerView: TextWheelPickerView
    private val hourPickerView: TextWheelPickerView
    private val minutePickerView: TextWheelPickerView

    private var listener: Listener? = null

    var startDate: LocalDate = LocalDate.of(LocalDate.now().year, 1, 1)
        set(value) {
            field = value
            updateDateAdapter()
        }
    var endDate: LocalDate = LocalDate.of(LocalDate.now().year, 12, 31)
        set(value) {
            field = value
            updateDateAdapter()
        }

    private fun updateDateAdapter() {
        dateAdapter.values = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .map { TextWheelPickerView.Item(it.toString(), it.toYmdTimeString()) }
            .toList()
        datePickerView.selectedIndex = 0
    }

    fun setWheelListener(listener: Listener) {
        this.listener = listener
    }

    var date: LocalDate
        set(value) {
            val index = dateAdapter.values.indexOfFirst { it.id == value.toString() }
            if (index != -1) {
                datePickerView.selectedIndex = index
            }
        }
        get() {
            val item = dateAdapter.values.getOrNull(datePickerView.selectedIndex)
            return item?.let { LocalDate.parse(it.id) } ?: LocalDate.now()
        }

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
            datePickerView.isCircular = value
            hourPickerView.isCircular = value
            minutePickerView.isCircular = value
        }

    private val dateAdapter = TextWheelAdapter()
    private val hourAdapter = TextWheelAdapter()
    private val minuteAdapter = TextWheelAdapter()

    private val binding: LongTriplePickerViewBinding =
        LongTriplePickerViewBinding.inflate(LayoutInflater.from(context), this)

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled)
        datePickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
        hourPickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
        minutePickerView.isHapticFeedbackEnabled = hapticFeedbackEnabled
    }

    init {
        datePickerView = binding.leftPicker
        datePickerView.setAdapter(dateAdapter)
        dateAdapter.values = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .map { TextWheelPickerView.Item(it.toString(), it.toYmdTimeString()) }
            .toList()
        datePickerView.selectedIndex = 0

        hourPickerView = binding.midPicker
        hourPickerView.setAdapter(hourAdapter)
        hourAdapter.values = (1..24).map {
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
            width = LayoutParams.MATCH_PARENT
            height =
                context.resources.getDimensionPixelSize(R.dimen.text_wheel_picker_item_height)
            gravity = Gravity.CENTER_VERTICAL
        }

        datePickerView.setWheelListener(this)
        hourPickerView.setWheelListener(this)
        minutePickerView.setWheelListener(this)
    }

    // region BaseWheelPickerView.WheelPickerViewListener
    override fun didSelectItem(picker: BaseWheelPickerView, index: Int) {
        listener?.didSelectData(date, hour, minute)
    }
    // endregion
}

private fun LocalDate.toYmdTimeString(): String =
    String.format(
        Locale.US,
        "%04d/%02d/%02d",
        this.year,
        this.monthValue,
        this.dayOfMonth
    )