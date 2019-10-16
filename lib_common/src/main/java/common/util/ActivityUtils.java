package common.util;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class ActivityUtils {
    private static ActivityUtils instance = null;

    public static ActivityUtils getInstance(Context paramContext) {
        if(instance == null) { instance = new ActivityUtils(paramContext); }
        return instance;
    }

    private ArrayList<Activity> acts = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();

    public void addActivity1(Activity paramActivity) {
        activities.add(paramActivity);
    }

    public void closeActs() {
        for(int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
        activities.clear();
    }

    public void removeActivity1(Activity paramActivity) {
        if((this.activities != null) && (this.activities.lastIndexOf(paramActivity) >= 0)) {
            this.activities.remove(paramActivity);
        }
    }

    private ArrayList<Activity> activities1 = new ArrayList<>();

    public void addPage(Activity paramActivity) {
        activities1.add(paramActivity);
    }

    public void closePages() {
        for(int i = 0; i < activities1.size(); i++) {
            activities1.get(i).finish();
        }
        activities1.clear();
    }

    public void removePage(Activity paramActivity) {
        if((this.activities1 != null) && (this.activities1.lastIndexOf(paramActivity) >= 0)) {
            this.activities1.remove(paramActivity);
        }
    }

    private ActivityUtils(Context paramContext) {
    }

    public ArrayList<Activity> getActs() {
        return acts;
    }

    public void clear() {
        acts.clear();
    }

    public void addActivity(Activity paramActivity) {
        this.acts.add(paramActivity);
    }

    public void removeActivity(Activity paramActivity) {
        if((this.acts != null) && (this.acts.lastIndexOf(paramActivity) >= 0)) {
            this.acts.remove(paramActivity);
        }
    }

    public void close() {
        for(int i = 0; i < acts.size(); i++) {
            try {
                acts.get(i).finish();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        acts.clear();
    }

    public int getSize() {
        return acts.size();
    }

    /**
     * 是否存在某个Activity
     * @return
     */
    public boolean isExistActivity(Class paramClass) {
        for(Activity activity : acts) {
            if(activity.getClass().getSimpleName().equals(paramClass.getSimpleName())) {
                return true;
            }
        }
        return false;
    }
}
