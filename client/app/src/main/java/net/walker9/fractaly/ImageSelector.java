package net.walker9.fractaly;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by joel on 2/27/17.
 */

public class ImageSelector extends Activity {
    static final int PICK_IMAGES_REQUEST = 1;

    public void open() {
        ArrayList<String> active_list = MainActivity.get_persistent_data().get_active_images();

        MultiImageSelector.create()
                .showCamera(true)
                .count(1000000)
                .origin(active_list)
                .start(this, PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> active_list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                MainActivity.get_persistent_data().set_active_images(active_list);
            }
        }
    }
}
