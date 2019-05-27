package rkr.simplekeyboard.inputmethod.keystroke;

import org.json.JSONObject;

public class KeyboardInput implements KeystrokeActionListener {

    private final KeystrokeManager ksManger;

    public KeyboardInput(KeystrokeManager ksManger) {
        this.ksManger = ksManger;
    }
    private long press_key_time = 0;
    private long release_key_time = 0;
    private JSONObject jo;

    @Override
    public void onPressKey(int primaryCode, int repeatCount, boolean isSinglePointer) {
        jo = new JSONObject();
        press_key_time = System.nanoTime();
        try {
            jo.put("NoKeyPressDelay", (press_key_time - release_key_time) / 1e6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReleaseKey(int primaryCode, boolean withSliding) {
        release_key_time = System.nanoTime();
        try {
            jo.put("KeyPressDelay", (release_key_time - press_key_time) / 1e6);
            ksManger.addJsonElem(jo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeInput(int primaryCode, int x, int y, boolean isKeyRepeat) {
        try {
            jo.put("primaryCode", primaryCode)
                    .put("x", x)
                    .put("y", y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTextInput(String rawText) {

    }

    @Override
    public void onFinishSlidingInput() {

    }

    @Override
    public boolean onCustomRequest(int requestCode) {
        return false;
    }

    @Override
    public void onMovePointer(int steps) {

    }

    @Override
    public void onDeletePointer(int steps) {

    }
}
