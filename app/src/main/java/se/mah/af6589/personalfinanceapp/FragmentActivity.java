package se.mah.af6589.personalfinanceapp;

import android.app.Fragment;

/**
 * Created by Gustaf Bohlin on 12/09/2017.
 */

public interface FragmentActivity {
    void setFragment(Fragment fragment, boolean backstack);
    void restartFragment(Fragment fragment);
    DataFragment addDataFragment();
}
