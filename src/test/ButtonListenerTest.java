package test;

import lib.ButtonListener;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.Assert.assertEquals;

/**
 * Created by tales on 17/02/16.
 */
public class ButtonListenerTest{

    @Test
    public void testAderir(){
        ButtonListener button = new ButtonListener();

        assertEquals(0,button.getNumber());

    }
}
