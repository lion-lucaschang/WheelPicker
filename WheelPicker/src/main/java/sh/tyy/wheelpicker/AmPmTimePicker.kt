package sh.tyy.wheelpicker

import android.content.Context
import sh.tyy.wheelpicker.core.WheelPickerActionSheet

class AmPmTimePicker(context: Context) : WheelPickerActionSheet<AmPmTimePickerView>(context) {
    init {
        setPickerView(AmPmTimePickerView(context))
    }
}