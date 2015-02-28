package treehousecareerprojects.readie;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dan on 2/27/2015.
 */
public class ReadieApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public ReadieApplication() {
        context = this;
    }
}
