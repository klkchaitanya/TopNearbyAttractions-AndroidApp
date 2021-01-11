package siri.project.topnearby;

import android.app.Fragment;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.view.SupportActionModeWrapper;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivity_SearchFragment_EditTextNotNullOnLocateButtonClick {

    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init()
    {
        mainActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Search()).commit();

    }

    @Test
    public void TestSearchFragmentEdittext()
    {
        //onView(withId(R.id.adView)).check(matches(isDisplayed()));
        //onView(withId(R.id.btnFabShare)).check(matches(isDisplayed()));
        //onView(withId(R.id.etSearch)).check(matches(withText("abc")));
       // etSearch.setText("Chicago");
        onView(withId(R.id.imgBtnLocate)).perform(click());
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etSearch)).check(matches(not(withText(""))));




    }
}
