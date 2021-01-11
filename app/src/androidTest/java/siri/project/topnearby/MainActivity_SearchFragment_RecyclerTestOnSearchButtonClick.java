package siri.project.topnearby;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivity_SearchFragment_RecyclerTestOnSearchButtonClick {



    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init()
    {
        mainActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Search()).commit();
    }

    @Test
    public void TestSearchFragmentRecyclerNotEmpty()
    {
        //onView(withId(R.id.adView)).check(matches(isDisplayed()));
        //onView(withId(R.id.btnFabShare)).check(matches(isDisplayed()));
        //onView(withId(R.id.etSearch)).check(matches(withText("abc")));
        // etSearch.setText("Chicago");
        //onView(withId(R.id.etSearch)).perform(clearText(), typeText());

        onView(withId(R.id.imgBtnLocate)).perform(click());
        try
        {
            Thread.sleep(10000);
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
        onView(withId(R.id.recycler)).check(new RecyclerViewCountAssertion());

    }
}


class RecyclerViewCountAssertion implements ViewAssertion
{
    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {

        RecyclerView recyclerView = (RecyclerView)view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        Assert.assertNotEquals(adapter.getItemCount(), is(0));
    }
}