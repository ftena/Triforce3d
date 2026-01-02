package com.tarlic.triforce3d;

import com.badlogic.gdx.InputProcessor;

public class TriforceInputProcessor  implements InputProcessor {

	Triforce3d triforce3d;

	public TriforceInputProcessor(Triforce3d triforce3d) {
		this.triforce3d = triforce3d;
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		// Gdx.app.log(Triforce3d.LOG, "touchDown");

		if ( triforce3d.gameState() == Triforce3d.PAUSED || 
				triforce3d.rotation() )
		{
			triforce3d.gameState(Triforce3d.RUNNING);
		}
		else
		{
			triforce3d.gameState(Triforce3d.PAUSED);
		}
		
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		// Gdx.app.log(Triforce3d.LOG, "touchUp");
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
