package com.hw.hwdroid.dialog;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.hw.hwdroid.dialog.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by ChenJ on 16/4/5.
 */
public class HFragmentExchangeController {

    /**
     * replace Fragment
     *
     * @param fragmentManager
     * @param targetFragment
     * @param tag
     */
    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        replaceFragment(fragmentManager, targetFragment, android.R.id.content, tag);
    }

    /**
     * replace Fragment
     *
     * @param fragmentManager
     * @param targetFragment
     * @param tag
     * @param containerViewId
     */
    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, @IdRes int containerViewId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.setCustomAnimations(R.anim.common_anim_fragment_in, R.anim.common_anim_fragment_out, R.anim
        // .common_anim_fragment_close_in, R.anim.common_anim_fragment_close_out);
        transaction.replace(containerViewId, targetFragment, tag);
        //		transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        initFragment(fragmentManager, targetFragment, android.R.id.content, tag);
    }

    /**
     * add Fragment
     *
     * @param fragmentManager
     * @param targetFragment
     * @param tag
     * @param containerViewId
     */
    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, @IdRes int containerViewId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerViewId, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * add Fragment
     *
     * @param fragmentManager
     * @param targetFragment
     * @param tag
     * @param postion
     * @param animIn
     * @param animOut
     * @param animCloseIn
     * @param animCloseOut
     */
    public static void addFragment(Context context, FragmentManager fragmentManager,
                                   Fragment targetFragment, String tag, @IdRes int postion, int animIn, int animOut, int animCloseIn, int animCloseOut) {
        if (!getAnimationSetting(context)) {
            animIn = 0;
            animOut = 0;
            animCloseIn = 0;
            animCloseOut = 0;
        }

        if (animIn < 0) {
            animIn = 0;
        }

        if (animOut < 0) {
            animOut = 0;
        }

        if (animCloseIn < 0) {
            animCloseIn = 0;
        }

        if (animCloseOut < 0) {
            animCloseOut = 0;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, animCloseIn, animCloseOut);
        transaction.add(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 获取动画设置
     *
     * @param context
     * @return
     */
    private static boolean getAnimationSetting(@NonNull Context context) {
        ContentResolver cv = context.getContentResolver();
        String animation = Settings.System.getString(cv, Settings.System.TRANSITION_ANIMATION_SCALE);
        return parseDouble(animation, 0) > 0;
    }

    /**
     * to Double
     *
     * @param s
     * @param defaultV
     * @return
     */
    private static double parseDouble(String s, double defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Double.parseDouble(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    /**
     * add Fragment
     *
     * @param supportFragmentManager
     * @param baseDialogFragment
     * @param tag
     */
    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, String tag) {
        addFragment(supportFragmentManager, baseDialogFragment, Window.ID_ANDROID_CONTENT, tag);
    }

    /**
     * add Fragment
     *
     * @param supportFragmentManager
     * @param baseDialogFragment
     * @param content
     * @param tag
     */
    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        try {
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.common_anim_fragment_in, R.anim.common_anim_fragment_out, R.anim
                    .common_anim_fragment_close_in, R.anim.common_anim_fragment_close_out);
            Fragment fragment = supportFragmentManager.findFragmentById(content);
            if (fragment != null) {
                if (fragment instanceof FragmentManager.OnBackStackChangedListener) {
                    supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) fragment);
                }
                transaction.hide(fragment);
            }
            transaction.add(content, baseDialogFragment, tag);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    /**
     * add Fragment
     *
     * @param supportFragmentManager
     * @param baseDialogFragment
     * @param content
     * @param tag
     * @param animIn
     * @param animOut
     * @param animCloseIn
     * @param animCloseOut
     */
    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment,
                                   @IdRes int content, String tag, int animIn, int animOut, int animCloseIn, int animCloseOut) {
        if (animIn < 0) {
            animIn = 0;
        }
        if (animOut < 0) {
            animOut = 0;
        }
        if (animCloseIn < 0) {
            animCloseIn = 0;
        }
        if (animCloseOut < 0) {
            animCloseOut = 0;
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, animCloseIn, animCloseOut);
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if (fragment != null) {
            if (fragment instanceof FragmentManager.OnBackStackChangedListener) {
                supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) fragment);
            }
            transaction.hide(fragment);
        }
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }


    public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, String tag) {
        addWithoutAnimFragment(supportFragmentManager, baseDialogFragment, android.R.id.content, tag);
    }

    public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, @IdRes int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentWithOutBackStack(FragmentManager supportFragmentManager, Fragment baseDialogFragment, @IdRes int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }


    public static void addFragmentImmediately(FragmentManager supportFragmentManager, Fragment baseDialogFragment, @IdRes int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    public static void addWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, @IdRes int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addWithAnimWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, @IdRes int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.common_anim_fragment_bottom_in, 0, 0, 0);
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if (fragment != null) {
            if (fragment instanceof FragmentManager.OnBackStackChangedListener) {
                supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) fragment);
            }
            transaction.hide(fragment);
        }
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 移除Fragment
     *
     * @param fragmentManager
     * @param tag
     * @param tag
     */
    public static void removeFragment(FragmentManager fragmentManager, String tag) {
        if (fragmentManager != null) {
            try {
                if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                    fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            } catch (Exception e) {
            }

            Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
            if (targetFragment != null) {
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }
    }

    /**
     * 移除Fragment
     *
     * @param fragmentManager
     * @param targetFragment
     */
    public static void removeFragment(FragmentManager fragmentManager, Fragment targetFragment) {
        if (fragmentManager != null) {
            String tag = targetFragment.getTag();
            try {
                try {
                    if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } catch (Exception e) {
                }
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();

                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment != null) {
                    // FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                    localFragmentTransaction.remove(fragment);
                    localFragmentTransaction.commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                }
            } catch (Exception e) {
            }
        }
    }

    public static int getStatuBar(@NonNull Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Fragment> getAllFragments(FragmentActivity fragmentActivity) {
        ArrayList<Fragment> fragments = null;
        if (fragmentActivity == null) {
            return fragments;
        }

        FragmentManager fragmentMgr = fragmentActivity.getSupportFragmentManager();
        if (fragmentMgr == null) {
            return fragments;
        }

        try {
            Class<?> clazz = fragmentMgr.getClass();
            Field field = clazz.getDeclaredField("mAdded");
            field.setAccessible(true);
            fragments = new ArrayList<>();
            if (field.get(fragmentMgr) != null) {
                fragments.addAll((ArrayList<Fragment>) field.get(fragmentMgr));
            }

        } catch (Exception e) {
            Logger.e(e);
        }

        return fragments;
    }

}
