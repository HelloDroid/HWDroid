package com.hw.hwdroid.foundation.app

import android.app.Activity
import android.support.annotation.StringRes
import android.widget.Toast
import com.hw.hwdroid.dialog.BaseDialogFragment
import com.hw.hwdroid.dialog.DialogCallBackContainer
import com.hw.hwdroid.dialog.HWDialogManager
import com.hw.hwdroid.dialog.model.DialogType
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel
import com.hw.hwdroid.dialog.utils.StringUtils
import com.hw.hwdroid.foundation.utils.GUIDUtils
import com.hw.hwdroid.foundation.utils.toast.ToastUtils
import android.support.v4.app.DialogFragment as SupportDialogFragment
import android.support.v4.app.Fragment as SupportFragment
import android.support.v4.app.FragmentActivity as SupportFragmentActivity


/**
 * Activity Toast扩展
 */
fun Activity.toast(message: CharSequence, dur: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(applicationContext, message, duration = dur)
}

/**
 * Activity Toast扩展
 */
fun Activity.toast(@StringRes messageResId: Int, dur: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(applicationContext, messageResId = messageResId, duration = dur)
}

/**
 * FragmentActivity 扩展 Dialog
 *
 * @param fragment
 * @param callBack
 * @param type                      类型
 * @param tag                       tag
 * @param negative                  取消文案
 * @param positive                  确定文案
 * @param title                     标题
 * @param content                   提示文案
 * @param cancelable                返回可点击
 * @param canceledOnTouchOutside    空白可点击
 * @return
 */
fun SupportFragmentActivity.showDialog(type: DialogType, tag: String = GUIDUtils.guid(),
                                       negative: CharSequence = String(), positive: CharSequence, title: CharSequence = String(), content: CharSequence = String(),
                                       cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false, fragment: SupportFragment? = null, callBack: DialogCallBackContainer? = null): BaseDialogFragment {
    val builder = DialogExchangeModel.DialogExchangeModelBuilder(type, tag)

    // 双按钮框
    if (type == DialogType.EXCUTE) {
        builder.setPositiveText(positive).setNegativeText(negative)
    }
    // 单按钮框
    else if (type == DialogType.SINGLE) {
        builder.setSingleText(negative)
    }

    // 文本消息
    builder.setDialogContext(content)

    // 标题，back可点击，空白可点击
    builder.setDialogTitle(title).setBackable(cancelable).setSpaceable(canceledOnTouchOutside).setHasTitle(!StringUtils.isEmptyOrNull(title))

    return HWDialogManager.showDialogFragment(supportFragmentManager, builder.create(), callBack, fragment, this)
}

/**
 * show Dialog
 * 返回和空白可点击
 * @param type              类型
 * @param tag               tag
 * @param negative          取消文案
 * @param positive          确定文案
 * @param title             标题
 * @param content           提示文案
 * @return
 */
fun SupportFragmentActivity.showDialogBackAble(type: DialogType, tag: String = GUIDUtils.guid(),
                                               negative: CharSequence = String(), positive: CharSequence = String(), title: CharSequence = String(), content: CharSequence = String()): BaseDialogFragment {
    return showDialog(type, tag, negative, positive, title, content, true, true)
}


/**
 * 单按钮弹出框
 *
 * @param tag                       tag
 * @param positive                  按钮文案
 * @param title                     标题
 * @param content                   提示文案
 * @param cancelable                返回可点击
 * @param canceledOnTouchOutside    空白可点击
 * @return
 */
fun SupportFragmentActivity.showSingleDialog(tag: String = GUIDUtils.guid(), positive: CharSequence = String(),
                                             title: CharSequence = String(), content: CharSequence = String(), cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): BaseDialogFragment {
    return showDialog(DialogType.SINGLE, tag, String(), positive, title, content, cancelable, canceledOnTouchOutside)
}


/**
 * Fragment 扩展 Dialog
 */
fun SupportFragment.showDialog(type: DialogType, tag: String = GUIDUtils.guid(),
                               negative: CharSequence = String(), positive: CharSequence, title: CharSequence = String(), content: CharSequence = String(),
                               cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false, callBack: DialogCallBackContainer? = null): BaseDialogFragment {
    val builder = DialogExchangeModel.DialogExchangeModelBuilder(type, tag)

    // 双按钮框
    if (type == DialogType.EXCUTE) {
        builder.setPositiveText(positive).setNegativeText(negative)
    }
    // 单按钮框
    else if (type == DialogType.SINGLE) {
        builder.setSingleText(negative)
    }

    // 文本消息
    builder.setDialogContext(content)

    // 标题，back可点击，空白可点击
    builder.setDialogTitle(title).setBackable(cancelable).setSpaceable(canceledOnTouchOutside).setHasTitle(!StringUtils.isEmptyOrNull(title))
    return HWDialogManager.showDialogFragment(fragmentManager, builder.create(), callBack, this)
}


/**
 * Fragment 扩展 Dialog
 */
fun SupportFragment.showDialogBackAble(type: DialogType, tag: String = GUIDUtils.guid(),
                                       negative: CharSequence = String(), positive: CharSequence = String(), title: CharSequence = String(), content: CharSequence = String()): BaseDialogFragment {
    return showDialog(type, tag, negative, positive, title, content, true, true)
}


fun SupportFragment.showSingleDialog(tag: String = GUIDUtils.guid(), positive: CharSequence = String(),
                                     title: CharSequence = String(), content: CharSequence = String(), cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): BaseDialogFragment {
    return showDialog(DialogType.SINGLE, tag, String(), positive, title, content, cancelable, canceledOnTouchOutside)
}

