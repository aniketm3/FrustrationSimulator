import java.util.Arrays;
import java.lang.Math;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Random;

public class room {
    public static int xlen;
    public static int ylen;
    public static int temerature;
    public static int numChar;
    public static int prefSetting;
    //0: null; 1: equal; 2: inverse; 3: random
    public static long seed;

    public static int[][] roomPos;
    public static particle[] particles;

    public static double[][] spring;
    //format: [perspective of char #][relative to char #]
    public static HashMap<Integer, Double> frusIndex = new HashMap<Integer, Double>();

    public static void initialize() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Starting trial");
        System.out.println("Enter room x dim: ");
        int userX = myObj.nextInt();
        xlen = userX;
        System.out.println("Enter room y dim: ");
        int userY = myObj.nextInt();
        ylen = userY;
        System.out.println("Enter number of characters");
        int userNum = myObj.nextInt();
        if (userNum>(xlen*ylen)) {
            System.out.println("Too many characters for room dim");
            System.exit(5);
        }
        numChar = userNum;
        System.out.println("Enter prefSetting (1: equal, 2: inverse, 3: random");
        int userPref = myObj.nextInt();
        if (userPref != 1 && userPref != 2 && userPref != 3) {
            System.out.println("Invalid prefSetting value");
            System.exit(6);
        }
        System.out.println("Enter simulation seed #: ");
        long userSeed = myObj.nextLong();
        seed = userSeed;
        Random r = new Random(seed);

        prefSetting = userPref;

        roomPos = new int[xlen][ylen];
        particles = new particle[numChar];
        spring = new double[numChar][numChar];

        //assigning particle initial position
        for (int i=0; i<xlen; i++) {
            for (int j=0; j<ylen; j++) {
                roomPos[i][j] = -1;
            }
        }
        //Arrays.fill(roomPos, Boolean.FALSE);
        // ^ wont need to be used since boolean initialized to false
        //false in 'room' means open space; no char present
        for(int i=0; i<numChar; i++) {
            boolean temp = false;
            int x=0;
            int y=0;
            while (!temp) {
                x = (int) (r.nextDouble() * xlen);
                y = (int) (r.nextDouble() * ylen);
                if (roomPos[x][y] == -1) {
                    temp = true;
                }
            }
            particles[i] = new particle(x,y);
            roomPos[x][y] = i;
        }

        //setting spring coefficients
        double value = Math.sqrt(xlen*ylen);
        if (prefSetting==1) {
            for (int i=0; i<spring.length;i++) {
                for (int j=0; j<spring[i].length;j++) {
                    spring[i][j] = value;
                }
            }
        }
        else if(prefSetting==2) {
            //inverse
            for (int i=0;i<spring.length;i++) {
                for (int j=i; j<spring[i].length;j++) {
                    if (i==j) {
                        spring[i][j]=0;
                    }
                    else {
                        spring[i][j]=value;
                        spring[j][i]=1/value;
                    }
                }
            }
        }
        else if (prefSetting==3) {
            //random
            for (int i=0;i<spring.length;i++) {
                for (int j=0; j<spring[i].length;j++) {
                    spring[i][j] = value * (r.nextDouble() * 100) *0.01;
                }
            }
        }
        else {
            System.out.println("Please enter a valid prefSetting");
        }

        System.out.println("initialized");
    }

    public static double calcDis (int x1, int x2, int y1, int y2) {
        double dist = Math.abs((x1-x2)^2+(y1-y2)^2);
        dist = Math.sqrt(dist);
        return dist;
    }

    public static double calcFrus(int mainChar,  int subChar) {
        //mainChar is observee for spring array, mainX and mainY are for location in array, necessary for surroinding
        //frus between two chars
     //   System.out.println("DEBUG:"+particles[mainChar].getxPos());
      //  System.out.println("DEBUG:mainChar="+mainChar+"SubChar="+subChar);
     //   System.out.println("DEBUG:"+particles[0].getxPos());
        double dist = calcDis(particles[subChar].getxPos(),particles[mainChar].getxPos() , particles[mainChar].getyPos(), particles[subChar].getyPos());
        double frus = Math.pow((dist - spring[mainChar][subChar]), 2);
        return frus;
    }

    public static double calcFrusTot(int mainChar) {
        //frus between one char and all others
        double total =0.0;
        for (int i=0; i<numChar; i++) {
            total += calcFrus(mainChar, i);
        }
        return total;
    }

    public static double calcRoomFrus() {
        double total = 0.0;
        for (int i=0; i<numChar; i++) {
            total+= calcFrusTot(i);
        }
        return total;
    }

    public static HashMap<Integer, Double> orderSortByValue(HashMap<Integer, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double> > list =
                new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<particle, Double> surroundSortByValue(HashMap<particle, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<particle, Double> > list =
                new LinkedList<Map.Entry<particle, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<particle, Double>>() {
            public int compare(Map.Entry<particle, Double> o1,
                               Map.Entry<particle, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<particle, Double> temp = new LinkedHashMap<particle, Double>();
        for (Map.Entry<particle, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static Integer[] charSort() {
        for (int i=0; i<numChar; i++) {
            frusIndex.put(i, calcFrusTot(i));
        }
        Map<Integer, Double> ordered = orderSortByValue(frusIndex);
 //       System.out.println(ordered);
        Integer[] ints = ordered.keySet().toArray(new Integer[ordered.size()]);
 //       System.out.println(Arrays.toString(ints));
        return ints;
    }

    public static void moveChar() {
        Integer[] order = charSort();
        for (Integer i: order) {
            roomPos[particles[i].getxPos()][particles[i].getyPos()] = -1;
            HashMap<particle, Double> surrounding = new HashMap<particle, Double>();
    //        int j=0;
     //       System.out.println("char " + i + "at " + particles[i].getxPos() + " " + particles[i].getyPos());
            for (int xinc = -1; xinc<2; xinc++) {
                for (int yinc = -1; yinc<2; yinc++) {
                    particle poss = new particle(particles[i].getxPos() + xinc, particles[i].getyPos() + yinc);
                    if(poss.getxPos()>xlen-1 || poss.getxPos()<0) {
                        continue;
                    }
                    if(poss.getyPos()>ylen-1 || poss.getyPos()<0) {
                        continue;
                    }
                    if(roomPos[poss.getxPos()][poss.getyPos()] != -1) {
                        continue;
                    }
                    int xStor = particles[i].getxPos();
                    int yStor = particles[i].getyPos();
                    particles[i].setxPos(poss.getxPos());
                    particles[i].setyPos(poss.getyPos());
                    surrounding.put(poss, calcFrusTot(i));
                    particles[i].setxPos(xStor);
                    particles[i].setyPos(yStor);
       //             System.out.println(poss.getxPos() + " " + poss.getyPos());
        //            j++;
                }
            }
      //      System.out.println(i + "had " + j);
            int minX = particles[i].getxPos();
            int minY = particles[i].getyPos();
            Double minValue = Double.MAX_VALUE;
            int j=0;
            for (Map.Entry<particle, Double> entry : surrounding.entrySet()) {
                Double value = entry.getValue();
    //            System.out.println("surrounding for: " + i + " #" + j++);
                if (value<minValue) {
                    minValue = value;
                    minX = entry.getKey().getxPos();
                    minY = entry.getKey().getyPos();
    //                System.out.println("min reassign");
                }
            }

           particles[i] = new particle(minX, minY);
            roomPos[minX][minY] = i;
        }
    }

    public static void printRoom() {
        for (int i=0; i<xlen; i++) {
            for (int j=0; j<ylen; j++) {
                System.out.printf("%02d", roomPos[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("Frus total: " + calcRoomFrus());
        System.out.println();
    }

}

