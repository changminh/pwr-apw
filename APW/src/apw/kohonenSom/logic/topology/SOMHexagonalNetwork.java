package apw.kohonenSom.logic.topology;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMHexagonalNetwork implements SOMTopology{
    
    int ox, oy, dx, dy;

	@Override
	public double calcDistance(Point origin, Point destination) {
		double dist = 0;

        ox = origin.x;
        dx = destination.x;
        oy = origin.y;
        dy = destination.y;

        while(!(ox == dx && oy == dy)){
            move();
            dist++;
        }

		return dist;
	}

    private void move(){
        if(oy%2 == 1){
            if(ox == dx && oy > dy){
                oy--;
            }else if(ox < dx && oy > dy){
                oy--; ox++;
            }else if(ox < dx && oy == dy){
                ox++;
            }else if(ox < dx && oy < dy){
                ox++; oy++;
            }else if(ox == dx && oy < dy){
                oy++;
            }else if(ox > dx && oy == dy){
                ox--;
            }else if(ox > dx && oy < dy){
                oy++;
            }else if(ox > dx && oy > dy){
                oy--;
            }
        }else{
            if(ox == dx && oy > dy){
                oy--;
            }else if(ox < dx && oy == dy){
                ox++;
            }else if(ox == dx && oy < dy){
                oy++;
            }else if(ox > dx && oy < dy){
                oy++; ox--;
            }else if(ox > dx && oy == dy){
                ox--;
            }else if(ox > dx && oy > dy){
                oy--;ox--;
            }else if(ox < dx && oy > dy){
                oy--;
            }else if(ox < dx && oy < dy){
                oy++;
            }
        }
    }

    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax) {
        ArrayList<Point> neighbours = new ArrayList<Point>();

        ox = origin.x;
        oy = origin.y;

        if(oy%2 == 1){
            if((oy-1)>=0){
                neighbours.add(new Point(ox, oy-1));
            }
            if((oy-1)>=0 && (ox+1)<xMax){
                neighbours.add(new Point(ox+1, oy-1));
            }
            if((ox+1)<xMax){
                neighbours.add(new Point(ox+1, oy));
            }
            if((ox+1)<xMax && (oy+1)<yMax){
                neighbours.add(new Point(ox+1, oy+1));
            }
            if((oy+1)<yMax){
                neighbours.add(new Point(ox, oy+1));
            }
            if((ox-1)>=0){
                neighbours.add(new Point(ox-1, oy));
            }
        }else{
            if((ox-1)>=0 && (oy-1)>=0){
                neighbours.add(new Point(ox-1, oy-1));
            }
            if((oy-1)>=0){
                neighbours.add(new Point(ox, oy-1));
            }
            if((ox+1)<xMax){
                neighbours.add(new Point(ox+1, oy));
            }
            if((oy+1)<yMax){
                neighbours.add(new Point(ox, oy+1));
            }
            if((ox-1)>=0 && (oy+1)<yMax){
                neighbours.add(new Point(ox-1, oy+1));
            }
            if((ox-1)>=0){
                neighbours.add(new Point(ox-1, oy));
            }
        }

        return neighbours;
    }

    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax, int N) {
        ArrayList<Point> neighbours = new ArrayList<Point>();
        ArrayList<Point> neighboursOld = new ArrayList<Point>();
        ArrayList<Point> temp = new ArrayList<Point>();

        ox = origin.x;
        oy = origin.y;

        neighbours.add(origin);

        for(int r=1; r<=N; r++){        
            temp.clear();

            for(int n=0; n<neighbours.size(); n++){
                temp = addList(
                        temp,
                        getNeighbours(neighbours.get(n), xMax, yMax));
            }

            neighboursOld = copyList(addList(neighbours, neighboursOld));
            neighbours = copyList(temp);
            neighbours.removeAll(neighboursOld);
        }

        return neighbours;
    }

    private ArrayList<Point> addList(
            ArrayList<Point> list1, ArrayList<Point> list2){

        for(int l=0; l< list2.size(); l++)
            if(!(list1.contains(list2.get(l))))
                list1.add(list2.get(l));

        return list1;
    }

    private ArrayList<Point> copyList(ArrayList<Point> list){
        ArrayList<Point> temp = new ArrayList<Point>();

        for(int l=0; l< list.size(); l++)
            temp.add(list.get(l));
        
        return temp;
    }
}
