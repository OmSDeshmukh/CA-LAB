public class myproject{
    public static void main(String args[]){
        int L = 1000;
        int w = 1;
        double p = 0.99;
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

        while(x == w-1 || time < 10000){
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

            //infiltrator simulation
            if(border[x][y] == 1){
                break; 
            }
            else{
                border[x][y] = 0 ; 
                boolean flag = false ;
                for(int k = 0 ; k < 8 ; k++){
                    if(x+dx[k] >= 0 && x+dx[k] < w && y+dy[k] >=0 && y+dy[k] < L){
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
        System.out.println(time);
        if(y != L){
            System.out.println("Caught");
        }
        else{
            System.out.println("Infiltrated!");
        }

    }
}


