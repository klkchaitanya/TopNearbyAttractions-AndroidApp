package siri.project.topnearby;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivity_SearchFragmentToPlaceDetailFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init()
    {
        mainActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Search()).commit();
    }

    @Test
    public void TestSearchFragmentToPlaceDetailFragment()
    {
        onView(withId(R.id.imgBtnLocate)).perform(click());
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.etSearch)).check(matches(not(withText(""))));
        onView(withId(R.id.imgBtnSearch)).perform(click());
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(isRoot()).perform(ViewActions.pressBack());
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnFav)).check(matches(isDisplayed()));

    }

}
