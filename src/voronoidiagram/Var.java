/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoidiagram;

/**
 *
 * @author shiechenghan
 */


import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.DefaultListModel;


public class Var {
    
    // Line class
    static class Line{
        Point p1; // point 1
        Point p2; // point 2
        Point pmid; // the point between the p1 and p2 
        double m; // slope
        double k; // constant of the function of line 
        Line(Point p1,Point p2){
            if(ComparePoint(p1,p2)<0)
            {
                this.p1 = p1;
                this.p2 = p2; 
            }
            else
            {
                this.p1 = p2;
                this.p2 = p1;
            }
            pmid = new Point((p1.x+p2.x)/2,(p1.y+p2.y)/2);
            m = (double)(p1.y-p2.y)/(double)(p1.x-p2.x);
            k = (double)(p1.y-m*p1.x);
            if(Double.isInfinite(Math.abs(m))==true) k=-(double)p1.x;
            if(m==0) k=p1.y;
        }
        public String getName(){
            return ("("+p1.x+","+p1.y+"),("+p2.x+","+p2.y+")");
        }
        
        // whether the point is on the line segment
        public boolean PointOnLineSegment(Point p){ 
            if(this.Point_Line(p)==0){
                if(((this.p1.x<=p.x && p.x<=p2.x)||(this.p2.x<=p.x && p.x<=p1.x))&&((this.p1.y<=p.y && p.y<=p2.y)||(this.p1.y<=p.y && p.y<=p2.y))) return true;
            }
            return false;
        }
        
        // the point is upper,down or on the line
        public int Point_Line(Point p){
            double value = m*p.x+k-p.y;
            if(m==0){
                if(p.y>p1.y){return 1;}
                if(p.y==p1.y){return 0;}
                if(p.y<p1.y){return -1;}
            }
            if(Double.isInfinite(Math.abs(m))==true){
                if(p.x>p1.x){return 1;}
                if(p.x==p1.x){return 0;}
                if(p.x<p1.x){return -1;}
            }
            if(value>0)return 1;
            else if(value==0) return 0;
            return -1;
        }
        
        //calculate the slope of the line
        public double Slope(Point p1,Point p2){
            return (double)(p1.y-p2.y)/(double)(p1.x-p2.x);
        }
        
        // whether the two lines are same slope
        public static boolean isSameSlope(Line l1,Line l2){
            if(l1.m == l2.m)return true;
            return false;
        }
        
        // the point that two lines are cross
        public static Point Cross(Line l1,Line l2){
            int x,y;
            if(Math.abs(l1.m) == 0.0){                
                y = l1.p1.y;
                if(Math.abs(l2.m) == 0.0|Math.abs(l2.m) == Double.POSITIVE_INFINITY){
                    x = l2.p1.x;
                }else{
                    x = (int)((y-l2.k)/l2.m);
                }
            }else if(Math.abs(l2.m) == 0.0){                
                y = l2.p1.y;  
                if(Math.abs(l1.m) == 0.0|Math.abs(l1.m) == Double.POSITIVE_INFINITY){
                    x = l1.p1.x;
                }else{
                    x = (int)((y-l1.k)/l1.m);
                }
            }else if(Math.abs(l1.m) == Double.POSITIVE_INFINITY){
                x = l1.p1.x;
                if(Math.abs(l2.m) == 0.0|Math.abs(l2.m) == Double.POSITIVE_INFINITY){
                    y = l2.p1.y;
                }else{
                    y = (int)(x*l2.m+l2.k);
                }
                
            }else if(Math.abs(l2.m) == Double.POSITIVE_INFINITY){
                x = l2.p1.x;
                if(Math.abs(l1.m) == 0.0|Math.abs(l1.m) == Double.POSITIVE_INFINITY){
                    y= l1.p1.y;
                }else{
                    y = (int)(x*l1.m+l1.k);
                }
               
            }else{
                x = (int)Math.round((double)(l1.pmid.x*l1.m - l2.pmid.x*l2.m -l1.pmid.y+l2.pmid.y) / (double)(l1.m-l2.m));
                y = (int)Math.round(l1.m*(x-l1.pmid.x) +l1.pmid.y);
            }            
            return new Point(x,y);
        }
        
        //whether the 2 lines is cross
        public static boolean  isCross(Line l1 ,Line l2){
            if(l1.m == l2.m)return false;
            return true;
        }
        
        // whether the 2 lines are same vector
        public static boolean isSameVector(Point p0,Point p1,Point p2){
            if(Var.Angle(p0,p1,p2)>0)return true;
            return false;
        }
        
    }
    
    // save VD and CH every step
    static boolean isFinish = false; //is finish execute VD2
    static ArrayList<ArrayList<Var.Line>> VD_Line_Set = new ArrayList<ArrayList<Var.Line>>(); // voronoi diagram
    static ArrayList<ArrayList<Point>>CH_Line_Set = new ArrayList<ArrayList<Point>>(); // convex hull
    static ArrayList<ArrayList<Var.Line>>HP_Line_Set = new ArrayList<ArrayList<Var.Line>>(); // hyper plane
    static int count_VD =0;
    
    //ReadFile
    static boolean ReadFileSuccess = false; 
    static boolean isShowVornoiFromFile = false;
    static ArrayList<ArrayList<Point>> File_Point_Set = new  ArrayList<ArrayList<Point>>();
    static ArrayList<ArrayList<Var.Line>> File_Line_Set = new  ArrayList<ArrayList<Var.Line>>();    
    static int number_of_data = 1;
    static int data_size = 0;
    
    //OutputFile
    static boolean OutputFileSuccess = false;
    
    //SIZE
    static int Panel_Size_X = 600;
    static int Panel_Size_Y = 600;
    static int Point_Size = 6;
    static int Line_Size = 5;
    
    //Point and Line
    static ArrayList<Point> Point_in_Canvas = new  ArrayList<Point>();
    static ArrayList<Point> Cross_Point_in_Canvas = new  ArrayList<Point>();
    
    static ArrayList<Line> Line_in_Canvas = new  ArrayList<Line>(); 
    static ArrayList<Line> Line_in_Canvas2 = new  ArrayList<Line>(); 
    static ArrayList<Line> Dive_Line_in_Canvas = new  ArrayList<Line>();
    static ArrayList<Line> Convex_Hull_Line_in_Canvas = new  ArrayList<Line>();
    static ArrayList<Line> Convex_Hull_Line_in_Canvas2 = new  ArrayList<Line>();
    static ArrayList<Line> Hyper_Plane_in_Canvas = new  ArrayList<Line>();
    static ArrayList<Line> Hyper_Plane_in_Canvas2 = new  ArrayList<Line>();
    
    static ArrayList<Polygon> Polygon_in_Canvas = new  ArrayList<Polygon>(); 
    static boolean[][] point_status = new boolean[Panel_Size_X][Panel_Size_Y];    
    static DefaultListModel Point_DLM = new DefaultListModel();
    
    //COLOR
    static Color Point_Color = Color.RED;
    
    static Color VD_Line_Color1 = Color.BLACK;
    static Color VD_Line_Color2 = Color.BLACK;
    static Color CH_Line_Color1 = Color.GREEN;
    static Color CH_Line_Color2 = Color.GREEN;
    static Color HP_Line_Color1 = Color.ORANGE;
    static Color HP_Line_Color2 = Color.PINK;
                  
    // clear all variable
    static void clearAllVar(){
        count_VD =0;
        isFinish = false;
        VD_Line_Set.clear(); // voronoi diagram
        CH_Line_Set.clear(); // convex hull
        HP_Line_Set.clear(); // hyper plane
    
        Point_in_Canvas.clear();
        Cross_Point_in_Canvas.clear();

        Line_in_Canvas.clear();
        Line_in_Canvas2.clear();
        Dive_Line_in_Canvas.clear();
        Hyper_Plane_in_Canvas.clear();
        Hyper_Plane_in_Canvas2.clear();
        Convex_Hull_Line_in_Canvas.clear();
        Convex_Hull_Line_in_Canvas2.clear();
        
        Polygon_in_Canvas.clear(); 
        point_status = new boolean[Panel_Size_X][Panel_Size_Y];    
        Point_DLM.clear();
        
        
    }
    
    // whitch is the nearest the point (0,0)
    static Point NearestToTheOrigin(ArrayList<Point> p){
        Point o = new Point(0,0);
        Point result = p.get(0);
        double tmp = TwoPointDistance(o,p.get(0)),tmp2;
        for(int i=1;i<p.size();i++){
            tmp2 = TwoPointDistance(o,p.get(i));
            if(tmp > tmp2){
                tmp = tmp2;
                result = p.get(i);
            }
        }
        return result;
    }
    
    static double TwoPointDistance(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
    }
    
    //copy the point set
    static ArrayList<Point> PointSet_Clone(ArrayList<Point> p){
        ArrayList<Point> result = new ArrayList<Point>();
        for (int i=0;i<p.size();i++){
            result.add(new Point(p.get(i).x,p.get(i).y));
        }
        return result;
    }
    
    
    static boolean isSameside(Point p0,Point p1,Line l){
        // y=mx+k
        if(l.Point_Line(p0)!=l.Point_Line(p1))return false;
        return true;
    }
    
    static int Side(Point p,Line l){
        // y=mx+k
        double k = l.p1.y-(l.m*l.p1.x);
        double m = l.m;
        double f = p.x*m+k-p.y; 
        
        if(f==0.0)return 0;
        else if(f>0.0)return 1;
        else return -1;
    }
    
    // if >0 , oa to ob==> reverse 
    static int CrossProduct(Point o,Point a,Point b){
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }
    static void SortPoint(ArrayList<Point> p){
        Collections.sort(p, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
              return ComparePoint(o1,o2);
            }
        }); 
    }
    static void SortLine(ArrayList<Line> l){
        Collections.sort(l, new Comparator<Line>() {
            @Override
            public int compare(Line l1, Line l2) {
              return CompareLine(l1,l2);
            }
        });
    }
    static void SortAngle(Point o,ArrayList<Point> p){
        Collections.sort(p, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
              if (CompareAngle(o,p1,p2)==true)return 1;
              return -1;
            }
        });
    }
    static int ComparePoint(Point o1, Point o2){
        if(o1.x > o2.x) return 1;
        else if((o1.x < o2.x)) return -1;
        else{
            if(o1.y > o2.y) return 1;
            else if((o1.y < o2.y)) return -1;
            else{
              return 0;
            }
        }
    }
    static int CompareLine(Line l1,Line l2){
        if(ComparePoint(l1.p1, l2.p1)==1) return 1;
        else if(ComparePoint(l1.p1, l2.p1)==-1) return -1;
        else{
            if(ComparePoint(l1.p2, l2.p2)==1) return 1;
            else if(ComparePoint(l1.p2, l2.p2)==-1) return -1;
            else{
              return 0;
            }
        }
    }
    static boolean CompareAngle(Point o,Point p1,Point p2){
        int c = CrossProduct(o, p1, p2);
        return c > 0 || (c == 0 && TwoPointDistance(o, p1) < TwoPointDistance(o, p2));
    }
    static double Angle(Point p0 ,Point p1,Point p2){
        // return cosine value
        int v1x = p1.x-p0.x;
        int v1y = p1.y-p0.y;
        int v2x = p2.x-p0.x;
        int v2y = p2.y-p0.y;
        double a =(v1x*v2x+v1y*v2y)/(Math.sqrt(v1x*v1x+v1y*v1y)*Math.sqrt(v2x*v2x+v2y*v2y));
        return (v1x*v2x+v1y*v2y)/(Math.sqrt(v1x*v1x+v1y*v1y)*Math.sqrt(v2x*v2x+v2y*v2y));
    }
}
