package com.crowdar.core.pageObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public abstract class WinPageBase extends PageBase {
    /**
     * On the windows system open file dialog, this function put a text a press
     * accept
     *
     * @param filepath            String of text that represent the file path that is
     *                            required to load
     * @param windowsOpenFileName String of text that represent the system
     *                            dialog windows name
     */
    public void systemOpenFileDialog(String filepath, String windowsOpenFileName) {
        HWND hwnd = User32.INSTANCE.FindWindow(null, windowsOpenFileName);

        if (hwnd != null) {
            User32.INSTANCE.SetForegroundWindow(hwnd);

            setTextToClipboard(filepath);

            try {
                Robot r = new Robot();

                r.delay(40);
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_CONTROL);

                r.delay(40);
                r.keyPress(KeyEvent.VK_ENTER);
                r.keyRelease(KeyEvent.VK_ENTER);

            } catch (AWTException e) {
                e.printStackTrace();
            }

        }
    }

}
