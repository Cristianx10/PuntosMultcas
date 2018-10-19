package com.example.ecosistemas.puntosmultcas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DrawView extends View {

    ArrayList<Point> punticos;
    int ID;

    TextView textView;

    public DrawView(Context context) {
        super(context);
        punticos= new ArrayList<>();

    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        punticos= new ArrayList<>();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        punticos= new ArrayList<>();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        punticos= new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Point puntoA= new Point(200,600);
        Paint brocha= new Paint();
        brocha.setColor(Color.rgb(255,0,0));
        brocha.setStrokeWidth(20);



       //canvas.drawText(textView.getX(), );

        //canvas.drawPoint(puntoA.x,puntoA.y,brocha);


        for(int i=0; i<punticos.size(); i++){
            canvas.drawPoint(punticos.get(i).x, punticos.get(i).y, brocha);
        }
    }

    public void addPoint(float x, float y) {
        punticos.add(new Point((int) x, (int) y));
        invalidate();
    }

    public void actualizarPos(int ID, float x, float y){
        punticos.get(ID).x = (int)x;
        punticos.get(ID).y = (int)y;
        invalidate();
    }

    public void identificar(int ID){
        this.ID = ID;
    }

    public void OnRecieved(String input){

    }

    public ArrayList<Point> getPunticos() {
        return punticos;
    }


}
