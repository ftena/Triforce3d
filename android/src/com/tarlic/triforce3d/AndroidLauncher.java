package com.tarlic.triforce3d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

public class AndroidLauncher extends AndroidLiveWallpaperService {
	
    @Override
    public void onCreateApplication () {
        super.onCreate();
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
        initialize(new Triforce3d(), cfg);
    }
	
	public ApplicationListener createListener () {
		return new Triforce3d();
	}

	public AndroidApplicationConfiguration createConfig () {
		return new AndroidApplicationConfiguration();
	}

	public void offsetChange (ApplicationListener listener, float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
		int xPixelOffset, int yPixelOffset) {
		Gdx.app.log("LiveWallpaper", "offset changed: " + xOffset + ", " + yOffset);
	}
}
