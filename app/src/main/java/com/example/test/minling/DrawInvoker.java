package com.example.test.minling;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class DrawInvoker {

    private List<DrawPath> drawList = Collections.synchronizedList(new ArrayList<>());

    private List<DrawPath> redoList = Collections.synchronizedList(new ArrayList<DrawPath>());

    public void add(DrawPath command) {
        redoList.clear();
        drawList.add(command);
    }

    public void undo() {
        if (drawList.size() > 0) {
            DrawPath undo = drawList.get(drawList.size() - 1);
            drawList.remove(drawList.size() - 1);
            undo.undo();
            redoList.add(undo);
        }
    }

    public void redo() {
        if (redoList.size() > 0) {
            DrawPath redoCommand = redoList.get(redoList.size() - 1);
            redoList.remove(redoList.size() - 1);
            drawList.add(redoCommand);
        }
    }

    public void execute(Canvas canvas) {
        if (drawList != null) {
            for (DrawPath tmp : drawList) {
                tmp.draw(canvas);
            }
        }
    }

    public boolean canRedo() {
        return redoList.size() > 0;
    }

    public boolean canUndo() {
        return drawList.size() > 0;
    }

}
