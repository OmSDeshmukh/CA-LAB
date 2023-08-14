public class retry{
    public static int simulate(int w , double p){
        int L = 1000;
        // int w = 2;
        // double p = 1;
        int border[][] = new int[w][L];

        int dx[] = {1,1,1,0,0,-1,-1,-1};
        int dy[] = {0,-1,1,-1,1,-1,1,0};

        boolean visited[][] = new boolean[w][L];

        int start = (int)(Math.random()*L);
        int x = 0 ;
        int y = start ;
        int time = 0 ;
        System.out.print(start);

        //blank cell = 0 
        //sensor = 1 
        //infiltrator = 2   

        while(x == w-1 || time < 100000){
            for(int i = 0 ; i < w ; i++){
                for(int j = 0 ; j < L ; j++){
                    int on = -1 ; 
                    if(Math.random() > p){
                        on = 0; // tails 
                    }
                    else{
                        on = 1 ; //heads 
                    }
                    border[i][j] = on ;
                }
            }

            // infiltrator simulation
            if(border[x][y] == 1){
                break; 
            }
            else{
                border[x][y] = 0 ; 
                boolean flag = false ;
                for(int k = 0 ; k < 8 ; k++){
                    if(x+dx[k] >= 0 && x+dx[k] < w && y+dy[k] >=0 && y+dy[k] < L){
                        if(x+dx[k] == w-1){
                            break; 
                        }
                        if(border[x+dx[k]][y+dy[k]] == 0 && flag == false && visited[x+dx[k]][y+dy[k]] == false){
                            x = x + dx[k];
                            y = y + dy[k];
                            border[x][y] = 2 ;
                            flag = true ;
                            visited[x][y] = true ;
                            
                        }
                        if(flag == false && border[x+dx[k]][y+dy[k]]==1){
                            break ; 
                            //return time ;
                        }
                    }
                }
            }
            time = time + 10 ;
        }

        //sensor simulation 
        // if(x == w-1){
        //     System.out.println("Infiltrated");
        // }
        // else{
        //     System.out.println("Caught");
        // }
        return time ;
    }

    public static void main(String args[]){
        int width[] = {4, 6, 10, 12, 16, 20, 24, 30};
        double probabilities[] = {0.2, 0.3, 0.4, 0.5, 0.6, 0.8};
        ;
        int timings[][] = new int[8][6];

        for(int i = 0 ; i < 8 ; i++){
            for(int j = 0 ; j < 6 ; j++){
                timings[i][j] = simulate(width[i], probabilities[j]);
                System.out.print(timings[i][j]/10 + " ");
            }
            System.out.println("");
        }

    }

}


