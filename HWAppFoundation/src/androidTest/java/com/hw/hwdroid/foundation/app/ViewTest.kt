package com.hw.hwdroid.foundation.app

import android.content.Context
import android.test.AndroidTestCase
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class ViewTest : AndroidTestCase() {

    fun testCast() {
        class Example(context: Context) : FrameLayout(context) {
            val name: TextView by bindView(1)
        }

        val example = Example(context)
        example.addView(textViewWithId(1))
        assertNotNull(example.name)
    }

    fun testFindCached() {
        class Example(context: Context) : FrameLayout(context) {
            val name: View by bindView(1)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        assertNotNull(example.name)
        example.removeAllViews()
        assertNotNull(example.name)
    }

    fun testOptional() {
        class Example(context: Context) : FrameLayout(context) {
            val present: View? by bindOptionalView(1)
            val missing: View? by bindOptionalView(2)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        assertNotNull(example.present)
        assertNull(example.missing)
    }

    fun testOptionalCached() {
        class Example(context: Context) : FrameLayout(context) {
            val present: View? by bindOptionalView(1)
            val missing: View? by bindOptionalView(2)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        assertNotNull(example.present)
        assertNull(example.missing)
        example.removeAllViews()
        example.addView(viewWithId(2))
        assertNotNull(example.present)
        assertNull(example.missing)
    }

    fun testMissingFails() {
        class Example(context: Context) : FrameLayout(context) {
            val name: TextView? by bindView(1)
        }

        val example = Example(context)
        try {
            example.name
        } catch (e: IllegalStateException) {
            assertEquals("View ID 1 for 'name' not found.", e.message)
        }
    }

    fun testList() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        example.addView(viewWithId(2))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
    }

    fun testListCaches() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        example.addView(viewWithId(2))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
        example.removeAllViews()
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
    }

    fun testListMissingFails() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        try {
            example.name
        } catch (e: IllegalStateException) {
            assertEquals("View ID 2 for 'name' not found.", e.message)
        }
    }

    fun testOptionalList() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindOptionalViews(1, 2, 3)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
    }

    fun testOptionalListCaches() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindOptionalViews(1, 2, 3)
        }

        val example = Example(context)
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
        example.removeAllViews()
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
    }

    private fun viewWithId(id: Int): View {
        val view = View(context)
        view.id = id
        return view
    }

    private fun textViewWithId(id: Int): View {
        val view = TextView(context)
        view.id = id
        return view
    }

}
