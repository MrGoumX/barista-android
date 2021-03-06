package com.example.stsisko.helloworldgradle;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.stsisko.helloworldgradle.activities.BatteryTestActivity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import gr.aueb.android.barista.core.annotations.BatteryOptions;
import gr.aueb.android.barista.core.inline.Barista;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class BatteryTests {

    @Rule
    public ActivityTestRule<BatteryTestActivity> activityActivityTestRule = new ActivityTestRule<BatteryTestActivity>(BatteryTestActivity.class);

    @Ignore
    @Test
    @BatteryOptions(level = 80, plugged = true)
    public void testLowBattery(){

        onView(withId(R.id.battery_layout)).check(matches(isDisplayed()));
        ColorDrawable backgroundColor = (ColorDrawable) BatteryTestActivity.getInstance().findViewById(R.id.battery_layout).getBackground();
        assertNotEquals(backgroundColor.getColor(), Color.GRAY);

        Barista.setBatteryState(40, false);
        backgroundColor = (ColorDrawable) BatteryTestActivity.getInstance().findViewById(R.id.battery_layout).getBackground();
        assertEquals(backgroundColor.getColor(), Color.GRAY);



    }




}
