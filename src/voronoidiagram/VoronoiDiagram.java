/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoidiagram;
import java.util.*;
import java.awt.Point;
//import javafx.scene.paint.Color;
import voronoidiagram.Var.Line;
import voronoidiagram.UI;
/**
 *
 * @author shiechenghan
 */

public class VoronoiDiagram {
     
    /**
     * @param args the command line arguments
     */
        
    // <= 3 Point Voronoi Diagram
    static ArrayList<Line> VD(ArrayList<Point> pp){  
        ArrayList<Line> l = new ArrayList<Line>();
        // =1 point
        if(pp.size()==1)return l;  
        
        // =2 point 
        if(pp.size()==2){           
            l.add(Bisection(pp.get(0),pp.get(1)));
            return l;
        }
        
        // =3 point
        boolean isObtuse = false;                             // is obtuse angle or not
        ArrayList<Line> l_b = new ArrayList<Line>(); // save bisection lines
        Point cp = new Point();                               // cross point
        boolean out = false;                                  // is exception(2 bisction is paralle or the center of circumscribed circle is out of convases range        
       
        // calculate the bisection line of any 2 point
        l_b.add(Bisection(pp.get(0),pp.get(1)));             
        l_b.add(Bisection(pp.get(1),pp.get(2)));
        l_b.add(Bisection(pp.get(0),pp.get(2)));
        System.out.println(" pppp"+l_b.get(1).getName());
        System.out.println(" "+l_b.get(2).getName());
        System.out.println(" "+l_b.get(3).getName());
        System.out.println(" "+l_b.get(4).getName());
        // ther will have normal sutuation and 2 exceptions
        
        /*
            2 exception ==> 
                1. the bisection line between any 2 points is paralle
                2. the center of circumscribed circle is out of canvases range
        */
        // 1. if 2 bisection lines is paralle
        if(Line.isSameSlope(l_b.get(0),l_b.get(1))==true && Line.isSameSlope(l_b.get(1),l_b.get(2))==true){             
            out = true;
            for (int i=0;i<2;i++){
                l.add(l_b.get(i));
            }
            return l;
        }else{
            cp = Line.Cross(l_b.get(0),l_b.get(1));  // calculate the center of circumscribed circle             
            out = CheckPoint_OutOfRange(cp);             // check whether is out of canvases range
        }    
        
        /* 
            2. 
            if the center of circumscribed circle is out of range, and it will have an obtuse angle
            if 2 bisctione lines is paralle or the center of circumscrived circle is out of canvases range
        */
        if(out==true){  
            for (int i=0;i<3;i++){
                /*  
                    calculate the cosine value of 2 line
                    if the value is [1,0] ==> the angle is between 0 degrees to 90 degrees
                    if the value is [0,-1] ==> the angle is between 90 degrees to 180 degrees
                */       
                double angle = Var.Angle(pp.get(i), pp.get((i+1)%3), pp.get((i+2)%3));                    
                // if the angle is >90 degrees and <=180 degrees, select the 2 lines
                if(angle<0.0){                    
                    l.add(Bisection(pp.get(i),pp.get((i+1)%3)));
                    l.add(Bisection(pp.get(i),pp.get((i+2)%3)));
                    break;
                }
                
            }
            return l;
        }
        
        // normal situation
        l_b.clear();
        for (int i=0;i<3;i++){
            double angle = Var.Angle(pp.get(i), pp.get((i+1)%3), pp.get((i+2)%3));
            
            // obtuse angle and Right angle
            if(angle<=0.0){
                isObtuse = true;
                l_b.add(Bisection(pp.get((i+1)%3), pp.get((i+2)%3))); 
                l_b.add(Bisection(pp.get(i), pp.get((i+1)%3))); 
                l_b.add(Bisection(pp.get(i), pp.get((i+2)%3))); 
                cp = Line.Cross(l_b.get(0),l_b.get(1));
                
                // Right Angel
                if(angle ==0.0){               
                    double tmp1 = Var.Angle(cp,l_b.get(0).p1,pp.get(i));
                    double tmp2 = Var.Angle(cp,l_b.get(0).p2,pp.get(i));                    
                    if(tmp1>tmp2){
                        l.add(new Line(cp,l_b.get(0).p2));
                    }else{
                        l.add(new Line(cp,l_b.get(0).p1));
                    }
                }else{
                    if(Line.isSameVector(cp,l_b.get(0).p1,Line.Cross(new Line(pp.get((i+1)%3),pp.get((i+2)%3)), l_b.get(0)))==false){
                        l.add(new Line(cp,l_b.get(0).p1));
                    }else{
                        l.add(new Line(cp,l_b.get(0).p2));
                    }
                }
                
                
                if(Line.isSameVector(cp,l_b.get(1).p1,Line.Cross(new Line(pp.get(i),pp.get((i+1)%3)), l_b.get(1)))==true){
                    l.add(new Line(cp,l_b.get(1).p1));
                }else{
                    l.add(new Line(cp,l_b.get(1).p2));
                }
                
                if(Line.isSameVector(cp,l_b.get(2).p1,Line.Cross(new Line(pp.get(i), pp.get((i+2)%3)), l_b.get(2)))==true){
                    l.add(new Line(cp,l_b.get(2).p1));
                }else{
                    l.add(new Line(cp,l_b.get(2).p2));
                }
                
                break;
            }
        }
        
        if(isObtuse==false){
            l_b.add(Bisection(pp.get(0), pp.get(1))); 
            l_b.add(Bisection(pp.get(1), pp.get(2))); 
            l_b.add(Bisection(pp.get(0), pp.get(2))); 
            cp = Line.Cross(l_b.get(0),l_b.get(1));
            if(Line.isSameVector(cp,l_b.get(0).p1,Line.Cross(new Line(pp.get(0),pp.get(1)), l_b.get(0)))==true){
                l.add(new Line(cp,l_b.get(0).p1));
            }else{
                l.add(new Line(cp,l_b.get(0).p2));
            }

            if(Line.isSameVector(cp,l_b.get(1).p1,Line.Cross(new Line(pp.get(1),pp.get(2)), l_b.get(1)))==true){
                l.add(new Line(cp,l_b.get(1).p1));
            }else{
                l.add(new Line(cp,l_b.get(1).p2));
            }

            if(Line.isSameVector(cp,l_b.get(2).p1,Line.Cross(new Line(pp.get(0), pp.get(2)), l_b.get(2)))==true){
                l.add(new Line(cp,l_b.get(2).p1));
            }else{
                l.add(new Line(cp,l_b.get(2).p2));
            }
        }
        
          return l;
    }
    
    
    /* 
    vd_p : voronoi diagram point set;  
    vd_l : voronoi diagram line set; 
    vd_hp: voronoi diagram hyper plane set; 
    ch_l : convex hull line set;
    */
    static void VD2(ArrayList<Point> vd_p,ArrayList<ArrayList<Line>>vd_l_s,ArrayList<ArrayList<Point>> ch_l,ArrayList<ArrayList<Line>> hp_l){                
        Var.SortPoint(vd_p);
        //convex hull line save
        
        ArrayList<Line> vd_l = new ArrayList<Line>();
        ArrayList<Line> tmp_hp_l = new ArrayList<Line>();  
        ArrayList<Point> tmp_cht = new ArrayList<Point>();
        tmp_cht = CH(vd_p);
        
        // if the point set only a point
        if(vd_p.size()==1){
            vd_l_s.add(vd_l);
            hp_l.add(tmp_hp_l);
            ch_l.add(tmp_cht);
            return;
        }
        
        // if all of the points have same coordinate x
        ArrayList<Point> tmp = Var.PointSet_Clone(vd_p);
        Point tmp_p = tmp.get(0);
        int count =1;
        for(int i=1;i<tmp.size();i++){        
            if(tmp_p.x == tmp.get(i).x){
                count+=1;
            }
        }
        if(count==tmp.size()){            
            for(int i=0;i<(vd_p.size()-1);i++){
                vd_l.add(Bisection(vd_p.get(i),vd_p.get(i+1)));
            }            
            vd_l_s.add(vd_l);
            hp_l.add(tmp_hp_l);
            ch_l.add(tmp_cht);
            return;
        }
        
        ArrayList<ArrayList<Point>> tmp_hd = Half_Dive(vd_p);
        ArrayList<ArrayList<Point>> tmp_ch = new ArrayList<ArrayList<Point>>();
        ArrayList<ArrayList<Line>> tmp_vd_l = new ArrayList<ArrayList<Line>>();   
        
        //left
        VD2(tmp_hd.get(0),vd_l_s,ch_l,hp_l);    
        tmp_vd_l.add(vd_l_s.get(vd_l_s.size()-1));
        tmp_ch.add(ch_l.get(ch_l.size()-1));
        
        for(int ii = 0;ii<tmp_hd.get(0).size();ii++){
            System.out.println(tmp_hd.get(0).get(ii).getX()+"%%"+tmp_hd.get(0).get(ii).getY());
        }
        System.out.println("----");
        for(int ii = 0;ii<tmp_hd.get(1).size();ii++){
            System.out.println(tmp_hd.get(1).get(ii).getX()+"%%%"+tmp_hd.get(1).get(ii).getY());
        }
        
        //right
        VD2(tmp_hd.get(1),vd_l_s,ch_l,hp_l); 
        tmp_vd_l.add(vd_l_s.get(vd_l_s.size()-1));
        tmp_ch.add(ch_l.get(ch_l.size()-1));
        
        //merge
        vd_l_s.add(Merge_VD(tmp_hd.get(0),tmp_hd.get(1),tmp_ch.get(0),tmp_ch.get(1),tmp_vd_l.get(0),tmp_vd_l.get(1),tmp_hp_l));
        //save hyper plane
        hp_l.add(tmp_hp_l);
        ch_l.add(tmp_cht);
    }
    
    /*
        The CCW(counter-clockwise turn) function is for Graham's scan, if the value is:
            >0 ==> turn left, return -1
            0  ==> 3 points are on the smae line, return 0
            <0 ==> turn right, return 1
    */
    static int CCW(Point p1, Point p2, Point p3){
        double value = (p2.x - p1.x)*(p3.y - p1.y) - (p2.y - p1.y)*(p3.x - p1.x);
        if(value<0)return 1;
        if(value==0)return 0;
        return -1;
    }       
       
    //Convex Hull 
    static ArrayList<Point> CH(ArrayList <Point> ps){
        ArrayList<Point> p = new ArrayList<Point>();
        p = Var.PointSet_Clone(ps);
        
        if(p.size()==1){
            //p.add(ps.get(0));
            return p;
        }
        
        Point np = Var.NearestToTheOrigin(p); //nearest to the origin
        Var.SortAngle(new Point(np.x,np.y),p);
     
        // 2 and 3 point
        if(p.size()<=3)return p;
             
        // >3 Point        
        ArrayList<Point> result = new ArrayList<Point>();
        
        int i=1;
        int m=2;
        int lr=CCW(p.get(p.size()-1),p.get(0),p.get(1));
        result.add(np);
        result.add(p.get(0));
        
        while(i!=p.size()){
            while(lr != CCW(result.get(m-2),result.get(m-1),p.get(i))){
                result.remove(m-1);
                m--;
            }
            result.add(p.get(i));
            i++;
            m++;
        }
        return result;
    }
        
    static boolean CheckPoint_OutOfRange(Point p){        
        if(p.x<0 |p.x>Var.Panel_Size_X |p.y<0 |p.y>Var.Panel_Size_Y)return true;
        return false;
    }
    
    //outer tangent
    static ArrayList<Line> OuterTangent(ArrayList<Point> cl,ArrayList<Point> cr){    
        System.out.println("OT");
        ArrayList<Line> outer_tangent=new ArrayList<Line>();
        int count =0;        
        Line tmp;
        int side =2;
        boolean out = false;        
        for(int i=0;i<cl.size();i++){
            for(int j=0;j<cr.size();j++){
                tmp = new Line(cl.get(i), cr.get(j));
                
                side = 2;
                out = false;
                for (int k=0;k<cl.size();k++){
                    if(k!=i){
                        if(side ==2 |side ==0) {
                            side = tmp.Point_Line(cl.get(k));
                            if(tmp.Point_Line(cl.get(k))==0){
                                
                                if(((tmp.p1.x <=cl.get(k).x && cl.get(k).x<=tmp.p2.x)|(tmp.p2.x <=cl.get(k).x && cl.get(k).x<=tmp.p1.x))&&((tmp.p1.y <=cl.get(k).y && cl.get(k).y<=tmp.p2.y)|(tmp.p2.y <=cl.get(k).y && cl.get(k).y<=tmp.p1.y))){
                                    out = true;
                                    break;
                                }   
                            }
                        }
                        else{                            
                            if(side != tmp.Point_Line(cl.get(k)) && tmp.Point_Line(cl.get(k))!=0){
                                out = true;
                                break;
                            }
                            if(tmp.Point_Line(cl.get(k))==0){
                                
                                if(((tmp.p1.x <=cl.get(k).x && cl.get(k).x<=tmp.p2.x)|(tmp.p2.x <=cl.get(k).x && cl.get(k).x<=tmp.p1.x))&&((tmp.p1.y <=cl.get(k).y && cl.get(k).y<=tmp.p2.y)|(tmp.p2.y <=cl.get(k).y && cl.get(k).y<=tmp.p1.y))){
                                    out = true;
                                    break;
                                }   
                            }
                        }   
                    }
                }
                
                for (int k=0;k<cr.size();k++){
                    if(k!=j){
                        if(side ==2 |side ==0){
                            side = tmp.Point_Line(cr.get(k));
                            if(tmp.Point_Line(cr.get(k))==0){
                                
                                if((tmp.p1.x <=cr.get(k).x && cr.get(k).x<=tmp.p2.x)|(tmp.p2.x <=cr.get(k).x && cr.get(k).x<=tmp.p1.x)|(tmp.p1.y <=cr.get(k).y && cr.get(k).y<=tmp.p2.y)|(tmp.p2.y <=cr.get(k).y && cr.get(k).y<=tmp.p1.y)){
                                    out = true;
                                    break;
                                }

                            }
                        }
                        else{                          
                            if(side != tmp.Point_Line(cr.get(k)) && tmp.Point_Line(cr.get(k))!=0){
                                out = true;
                                break;
                            }
                            if(tmp.Point_Line(cr.get(k))==0){
                                
                                if((tmp.p1.x <=cr.get(k).x && cr.get(k).x<=tmp.p2.x)|(tmp.p2.x <=cr.get(k).x && cr.get(k).x<=tmp.p1.x)|(tmp.p1.y <=cr.get(k).y && cr.get(k).y<=tmp.p2.y)|(tmp.p2.y <=cr.get(k).y && cr.get(k).y<=tmp.p1.y)){
                                    out = true;
                                    break;
                                }

                            }

                        }
                        
                       
                    } 
                }
                
                if(out==false){                   
                    outer_tangent.add(new Line(cl.get(i),cr.get(j)));
                }
            }
        
        }        
        return outer_tangent;
    }
    /* 
        pl,pl ==> points on the right and left side ; 
        ll,lr ==> vornoi diagram lines on the left and right side 
        cl,cr ==> Convex Hull on the left and right side
    
    */
    static ArrayList<Line> Merge_VD(ArrayList<Point>pl,ArrayList<Point> pr,ArrayList<Point>cl,ArrayList<Point> cr,ArrayList<Line> ll,ArrayList<Line> lr,ArrayList<Line> HP){

        // two voronoi diagram outer tangent
        ArrayList<Line> outer_tangent=OuterTangent(cl,cr);        
        System.out.print(outer_tangent.get(0).getName()+" /// ");
        if(outer_tangent.size()>1)System.out.println(outer_tangent.get(1).getName());
        else System.out.println();
        // merge two vd line and point
        ArrayList<Line> line_set = new ArrayList<Line>();
        ArrayList<Point> point_set = new ArrayList<Point>();
        for (int i=0;i<pl.size();i++)point_set.add(pl.get(i));
        for (int i=0;i<pr.size();i++)point_set.add(pr.get(i));        
        if(pl.size()==1 && pr.size()==1){
            line_set.add(Bisection(pl.get(0),pr.get(0)));
            return line_set;
        }
        if(outer_tangent.size()==1){
            for (int i=0;i<ll.size();i++)line_set.add(ll.get(i));
            for (int i=0;i<lr.size();i++)line_set.add(lr.get(i));
            line_set.add(Bisection(outer_tangent.get(0).p1,outer_tangent.get(0).p2));
            return line_set;
        }else if(outer_tangent.get(0).m == outer_tangent.get(1).m && outer_tangent.get(0).k == outer_tangent.get(1).k){
            //p1
            for (int i=0;i<ll.size();i++)line_set.add(ll.get(i));
            for (int i=0;i<lr.size();i++)line_set.add(lr.get(i));
            if(Var.TwoPointDistance(outer_tangent.get(0).p1, outer_tangent.get(0).p2)<Var.TwoPointDistance(outer_tangent.get(1).p1, outer_tangent.get(1).p2)){
                line_set.add(Bisection(outer_tangent.get(0).p1,outer_tangent.get(0).p2));
            }else{
                
                line_set.add(Bisection(outer_tangent.get(1).p1,outer_tangent.get(1).p2));
            }
            return line_set;
        }else{
            Point p1,p2;
            int p1_lr=0,p2_lr=0;

            p1 = outer_tangent.get(0).p1;
            p2 = outer_tangent.get(0).p2;
            for(int i=0;i<cl.size();i++){
                if(Var.ComparePoint(p1, cl.get(i))==0){
                    p1_lr = -1;
                    p2_lr = 1;

                    break;
                }else if(Var.ComparePoint(p2, cl.get(i))==0){
                    p1_lr = 1;
                    p2_lr = -1;

                    break;
                }
            }
            for(int i=0;i<cr.size();i++){
                if(Var.ComparePoint(p1, cr.get(i))==0){
                    p1_lr = -1;
                    p2_lr = 1;

                    break;
                }else if(Var.ComparePoint(p2, cr.get(i))==0){
                    p1_lr = -1;
                    p2_lr = 1;

                    break;
                }
            }
            Point c = null,c1 = null,c2=null,st,ed,cc1 = null,cc2=null;
            int i_tmp,i_tmp2,y_tmp;
            int l_r=0;
            double d_tmp1,d_tmp2;
            Var.Line b,bt; //save bisection line
            b = Bisection(p1,p2);
            if(b.p1.y>b.p2.y) ed = b.p1;
            else ed = b.p2;
            //Var.Hyper_Plane_in_Canvas.add(outer_tangent.get(0)); //upper
            //Var.Hyper_Plane_in_Canvas.add(outer_tangent.get(1));
           
            boolean[][] pp_use = new boolean[Var.Panel_Size_X][Var.Panel_Size_X]; // left point to right point already has bisection
            int t_l=0,t_r=0,t_l2=0,t_r2=0;
            for(int i=0;i<pl.size();i++){
                if(p1_lr == -1){
                    if(Var.ComparePoint(outer_tangent.get(0).p1,pl.get(i))==0) t_l =i;
                }else{
                   if(Var.ComparePoint(outer_tangent.get(0).p2,pl.get(i))==0) t_l = i;
                }
            }
            for(int i=0;i<pr.size();i++){
                if(p1_lr == 1){
                    if(Var.ComparePoint(outer_tangent.get(0).p1,pr.get(i))==0) t_r =i;
                }else{
                   if(Var.ComparePoint(outer_tangent.get(0).p2,pr.get(i))==0) t_r = i;
                }
            }
            pp_use[t_l][t_r] = true;
            boolean[] ll_use= new boolean[ll.size()];
            boolean[] lr_use= new boolean[lr.size()];
            int ii=0,ii2=0;
            Var.Line lastcross_ll=null,lastcross_lr=null;
            while(!((Var.ComparePoint(p1, outer_tangent.get(1).p1)==0 && Var.ComparePoint(p2, outer_tangent.get(1).p2)==0)||(Var.ComparePoint(p2, outer_tangent.get(1).p1)==0 && Var.ComparePoint(p1, outer_tangent.get(1).p2)==0)))
            {               
                System.out.println(p1+" : "+p2);
                b = Bisection(p1,p2);
                st = new Point(ed.x,ed.y);
                
                i_tmp = 0;
                
                for(int i=0;i<ll.size();i++){
                    c = Var.Line.Cross(ll.get(i), b);
                    //Var.Cross_Point_in_Canvas.add(c);
         
                    if((ll.get(i).p1.x<=c.x && c.x<=ll.get(i).p2.x)|(ll.get(i).p2.x<=c.x && c.x<=ll.get(i).p1.x)){
                        if((i_tmp<c.y) && (c.y<st.y)) {
                            if(lastcross_ll==null || lastcross_ll != ll.get(i)){                        
                                i_tmp=c.y;c1=c;ii=i;
                            }
                        }
                    }
                }
                i_tmp2 = 0;
                for(int i=0;i<lr.size();i++){
                    c = Var.Line.Cross(lr.get(i), b);
                    //Var.Cross_Point_in_Canvas.add(c);                    
                    if((lr.get(i).p1.x<=c.x && c.x<=lr.get(i).p2.x)|(lr.get(i).p2.x<=c.x && c.x<=lr.get(i).p1.x)){
                        if((i_tmp2<c.y) && (c.y<st.y)) {                            
                            if(lastcross_lr==null || lastcross_lr != lr.get(i)){                                
                                i_tmp2=c.y;c2=c;ii2=i;
                            }
                            
                        }
                        
                    }
                }

                if(i_tmp == i_tmp2){
                    lastcross_ll = ll.get(ii);
                    lastcross_lr=lr.get(ii2);
                    l_r = 0;
                }else if(i_tmp > i_tmp2){
                    lastcross_ll = ll.get(ii);
                    l_r = -1;
                    ed =c1;
                }else{
                    
                    l_r = 1;
                    ed =c2;
                    ii=ii2;
                    lastcross_lr=lr.get(ii);
                }
                System.out.println(st+" "+ed);
                System.out.println(i_tmp +" "+ i_tmp2+" "+l_r);
               
                // l_r ==> change side
                if(l_r==0){
                    if(p1_lr == -1){
                        //===================
                        if(Var.isSameside(p1, ll.get(ii).p1, b)){
                            line_set.add(new Var.Line(ll.get(ii).p1,c1));
                            ll_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(ll.get(ii).p2,c1));
                            ll_use[ii]=true;
                        }
                        if(Var.isSameside(p2, lr.get(ii2).p1, b)){
                            line_set.add(new Var.Line(lr.get(ii2).p1,c1));
                            lr_use[ii2]=true;
                        }else{
                            line_set.add(new Var.Line(lr.get(ii2).p2,c1));
                            lr_use[ii2]=true;
                        }
                        //===================
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pl.size();i++){
                            for(int j=0;j<pr.size();j++){
                                if(!(Var.ComparePoint(p1,pl.get(i))==0 && Var.ComparePoint(p2,pr.get(j))==0)){                                   
                                    bt = Bisection(pl.get(i),pr.get(j));
                                    c = Var.Line.Cross(bt, b);
                                    d_tmp2 = Var.TwoPointDistance(c, ed);                
                                    if(d_tmp1>=d_tmp2 && Var.ComparePoint(c, c1)!=0 && pp_use[i][j] == false){
                                        d_tmp1 = d_tmp2;
                                        p1 = pl.get(i);
                                        p2 = pr.get(j);
                                        pp_use[i][j] = true;
                                        t_l = i;
                                        t_r = j;
                                    }
                                }
                            }
                        }

                    }else{
                        //===================
                        if(Var.isSameside(p2, ll.get(ii).p1, b)){
                            line_set.add(new Var.Line(ll.get(ii).p1,c1));
                            ll_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(ll.get(ii).p2,c1));
                            ll_use[ii]=true;
                        }
                        if(Var.isSameside(p1, lr.get(ii2).p1, b)){
                            line_set.add(new Var.Line(lr.get(ii2).p1,c1));
                            lr_use[ii2]=true;
                        }else{
                            line_set.add(new Var.Line(lr.get(ii2).p2,c1));
                            lr_use[ii]=true;
                        }
                        //===================
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pl.size();i++){
                            for(int j=0;j<pr.size();j++){
                                if(!(Var.ComparePoint(p2,pl.get(i))==0 && Var.ComparePoint(p1,pr.get(j))==0) ){
                                    bt = Bisection(pl.get(i),pr.get(j));
                                    c = Var.Line.Cross(bt, b);
                                    d_tmp2 = Var.TwoPointDistance(c, ed);
                                    if(d_tmp1>d_tmp2 && Var.ComparePoint(c, c1)!=0 && pp_use[i][j] == false){
                                        d_tmp1 = d_tmp2;
                                        p2 = pl.get(i);
                                        p1 = pr.get(j);
                                        pp_use[i][j] = true;
                                        t_l = i;
                                        t_r = j;
                                    }
                                }
                            }
                        }
    
                    }
                   
                }
                if(l_r == -1 ){
                             
                    if(p1_lr == -1){
                        if(Var.isSameside(p1, ll.get(ii).p1, b)){
                            line_set.add(new Var.Line(ll.get(ii).p1,ed));
                            ll_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(ll.get(ii).p2,ed));
                            ll_use[ii]=true;
                        }
                        
                        // change p1 or p2
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pl.size();i++){
                            if(Var.ComparePoint(p1,pl.get(i))!=0){
                                bt = Bisection(p2,pl.get(i));
                                c1 = Var.Line.Cross(bt, b);
                                d_tmp2 = Var.TwoPointDistance(c1, ed); 
                                if(d_tmp1>d_tmp2 && pp_use[i][t_r] == false){
                                    d_tmp1 = d_tmp2;
                                    p1 = pl.get(i);
                                    pp_use[i][t_r] = true;
                                    t_l = i;
                                }
                            }
                        }
                    }else{
                        
                        if(Var.isSameside(p2, ll.get(ii).p1, b)){
                            line_set.add(new Var.Line(ll.get(ii).p1,ed));
                            ll_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(ll.get(ii).p2,ed));
                            ll_use[ii]=true;
                        }
                        
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pl.size();i++){
                            if(Var.ComparePoint(p2,pl.get(i))!=0){
                                bt = Bisection(p2,pl.get(i));
                                c1 = Var.Line.Cross(bt, b);
                                d_tmp2 = Var.TwoPointDistance(c1, ed);
                               
                                if(d_tmp1>d_tmp2  && pp_use[i][t_r] == false){
                                    d_tmp1 = d_tmp2;
                                    p2 = pl.get(i);
                                    pp_use[i][t_r] = true;
                                    t_l = i;
                                }
                            }
                        }
                    }
                }
                if(l_r == 1){
                               
                    if(p1_lr == -1){
                        
                        if(Var.isSameside(p2, lr.get(ii).p1, b)){
                            line_set.add(new Var.Line(lr.get(ii).p1,ed));
                            lr_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(lr.get(ii).p2,ed));
                            lr_use[ii]=true;
                        }
                        
                        
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pr.size();i++){
                            if(Var.ComparePoint(p2,pr.get(i))!=0){
                                bt = Bisection(p1,pr.get(i));
                                c1 = Var.Line.Cross(bt, b);
                                d_tmp2 = Var.TwoPointDistance(c1, ed);
                              
                                if(d_tmp1>d_tmp2  && pp_use[t_l][i] == false){
                                    d_tmp1 = d_tmp2;
                                    p2 = pr.get(i);
                                    pp_use[t_l][i] = true;
                                    t_r = i;
                                }
                            }
                        }
                    }else{
                        
                        if(Var.isSameside(p1, lr.get(ii).p1, b)){
                            line_set.add(new Var.Line(lr.get(ii).p1,ed));
                            lr_use[ii]=true;
                        }else{
                            line_set.add(new Var.Line(lr.get(ii).p2,ed));
                            lr_use[ii]=true;
                        }
                        
                        d_tmp1 = Double.MAX_VALUE;
                        for(int i=0;i<pr.size();i++){
                            if(Var.ComparePoint(p1,pr.get(i))!=0){
                                bt = Bisection(p2,pr.get(i));
                                c1 = Var.Line.Cross(bt, b);
                                d_tmp2 = Var.TwoPointDistance(c1, ed);
                               
                                if(d_tmp1>d_tmp2 && pp_use[t_l][i] == false){
                                    d_tmp1 = d_tmp2;
                                    p1 = pr.get(i);
                                    pp_use[t_l][i] = true;
                                    t_r = i;
                                }
                            }
                        }

                    }
                }
                if(l_r!=0){
                    HP.add(new Line(st,ed));
                    line_set.add(new Line(st,ed));
                }
            
            }
            
            b = Bisection(p1,p2);            
            st = ed;
            if(b.p1.y<b.p2.y) ed = b.p1;
            else ed = b.p2;
            line_set.add(new Line(st,ed));
            HP.add(new Line(st,ed));
            
            Line tmp_l;
            boolean out=false;
            
            
            for(int i=0;i<ll.size();i++){
                
                out=false;
                tmp_l = new Line(ll.get(i).p1,ll.get(i).p2);
                for(int j=0;j<HP.size();j++){
                    if(Var.Line.isCross(tmp_l, HP.get(j)) && HP.get(j).PointOnLineSegment(Var.Line.Cross(tmp_l, HP.get(j)))==true){

                        if(HP.get(j).Point_Line(ll.get(i).p1)==1 || HP.get(j).Point_Line(ll.get(i).p2)==1){

                            out = true;
                            break;
                        }
                    }
                }
                if(out==false&& ll_use[i]==false)line_set.add(tmp_l);
                    
            }
            for(int i=0;i<lr.size();i++){
                out=false;
                tmp_l = new Line(lr.get(i).p1,lr.get(i).p2);
                for(int j=0;j<HP.size();j++){
                    if(Var.Line.isCross(tmp_l, HP.get(j)) && HP.get(j).PointOnLineSegment(Var.Line.Cross(tmp_l, HP.get(j)))==true){
                        if(HP.get(j).Point_Line(lr.get(i).p1)==-1 || HP.get(j).Point_Line(lr.get(i).p2)==-1){
                            out = true;
                            break;
                        }
                    }
                }
                if(out==false&& lr_use[i]==false){
                    
                    line_set.add(tmp_l);
                }
            }
            
            return line_set;
        }
    
                

    }
    
    // Merge Convex Hull
    static void Merge_CH(ArrayList <Point> lp,ArrayList <Point> rp){
        // find the nearest to the origin
        
                
    }

    static ArrayList<ArrayList<Point>> Half_Dive(ArrayList<Point> p){        
        ArrayList<ArrayList<Point>> result = new ArrayList<ArrayList<Point>>();
        ArrayList<Point> L = new ArrayList<Point>();
        ArrayList<Point> R = new ArrayList<Point>();
        int tmpx,tmpy,tmp,i;
        int count=0;
        Point pp=p.get(0);
        i=1;
        while(i<p.size()){
            if(pp.x!=p.get(i).x){
                count++;
                pp = p.get(i);
            }
            i++;
        }
        
        pp=p.get(0);
        i=1;
        
        if(count==1)
        {   L.add(p.get(0));
            while(i<p.size()){
               if(pp.x==p.get(i).x){
                    L.add(p.get(i));
                }else{
                   R.add(p.get(i));
               }
               i++;
            }
            result.add(L);
            result.add(R);
            return result;
        }
        if(p.size()%2==0){
            for(i=0;i<(p.size()/2);i++){
                L.add(p.get(i));
            }
            tmp = i;
            tmpx = p.get(i-1).x;
            while(i<p.size()){
                if(tmpx == p.get(i).x ){
                    L.add(p.get(i));
                    i++;
                }else{
                    tmp = i;
                    break;
                }
            }
            for(i=tmp;i<p.size();i++){
                R.add(p.get(i));
            }
        }else{
            for(i=0;i<=(p.size()/2);i++){
                L.add(p.get(i));
            }
            tmp = i;
            tmpx = p.get(i-1).x;
            while(i<p.size()){
                if(tmpx == p.get(i).x){
                    L.add(p.get(i));
                    i++;
                }else{
                    tmp = i;
                    break;
                }
            }
            for(i=(p.size()/2)+1;i<p.size();i++){
                R.add(p.get(i));
            }
        }
        result.add(L);
        result.add(R);
        return result;
    }
    
    static Line Bisection(Point p1,Point p2)
    {
        // y = mx + k
        double bx,by;
        bx = (double)(p1.x+p2.x)/2;
        by = (double)(p1.y+p2.y)/2;
                        
        double m = -((double)(p1.x-p2.x) /(double)(p1.y-p2.y));
        double k = by-m*bx;
        if(p1.x == p2.x){
            return new Line(new Point(0,(int)Math.round(by)),new Point(Var.Panel_Size_X,(int)Math.round(by)));
        }
        else if(p1.y == p2.y){
            return new Line(new Point((int)Math.round(bx),0),new Point((int)Math.round(bx),Var.Panel_Size_Y));
        }else{ 
            // y = mx + k
            double x,y;
            ArrayList<Point> list = new ArrayList<Point>();
            
            x=0;
            y = k;
            if(y>=0 && y<=Var.Panel_Size_Y)
            {
                list.add(new Point((int)Math.round(x),(int)Math.round(y)));
            }
            
            y=0;
            x= (-k)/m; 
            if(x>=0 && x<=Var.Panel_Size_X)
            {
                list.add(new Point((int)Math.round(x),(int)Math.round(y)));
            }
            
            x=Var.Panel_Size_X;
            y = k+m*x;
            if(y>=0 && y<=Var.Panel_Size_Y)
            {
                list.add(new Point((int)Math.round(x),(int)Math.round(y)));
            }
            
            y=Var.Panel_Size_Y;
            x= (y-k)/m;
            if(x>=0 && x<=Var.Panel_Size_X)
            {
                list.add(new Point((int)Math.round(x),(int)Math.round(y)));
            }

            for (int i =0;i<list.size()-1;i++)
            {
                for (int j=i+1;j<list.size();j++)
                {
                    if(list.get(i).x == list.get(j).x && list.get(i).y == list.get(j).y)
                    {
                        list.remove(j);
                    }
                }
            }

            return new Line(new Point(list.get(0).x,list.get(0).y),new Point(list.get(1).x,list.get(1).y));
        }
    }
    
    
    
}
