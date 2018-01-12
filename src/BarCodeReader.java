package slm;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

interface BarcodeListener{
    void onBarcodeRead(String barcode);
}


public class BarcodeReader{
    private static final long THRESHOLD = 100;
    private static final int MIN_BARCODE_LENGTH = 8;

    private final StringBuffer barcode = new StringBuffer();
    private final List<BarcodeListener> listeners = new CopyOnWriteArrayList<BarcodeListener>();
    private long lastEventTimeStamp = 0L;

    public BarcodeReader() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e){
                if (e.getID() != KeyEvent.KEY_RELEASED) {
                    return false;
                }

                if (e.getWhen() - lastEventTimeStamp > THRESHOLD) {
                    barcode.delete(0, barcode.length());
                }

                lastEventTimeStamp = e.getWhen();

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (barcode.length() >= MIN_BARCODE_LENGTH) {
                        fireBarcode(barcode.toString());
                    }
                    barcode.delete(0, barcode.length());
                } else {
                    barcode.append(e.getKeyChar());
                }
                return false;
            }
        });

    }

    protected void fireBarcode(String barcode) {
        for (BarcodeListener listener : listeners) {
            listener.onBarcodeRead(barcode);
        }
    }

    public void addBarcodeListener(BarcodeListener listener) {
        listeners.add(listener);
    }

    public void removeBarcodeListener(BarcodeListener listener) {
        listeners.remove(listener);
    }

}