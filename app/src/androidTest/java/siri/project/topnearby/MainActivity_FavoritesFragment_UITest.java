package siri.project.topnearby;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivity_FavoritesFragment_UITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init()
    {
        mainActivityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Favorites()).commit();
    }

    @Test
    public void TestFavoritesFragmentUI()
    {
        onView(withId(R.id.spinnerCities)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler)).check(matches(isDisplayed()));
    }
}
