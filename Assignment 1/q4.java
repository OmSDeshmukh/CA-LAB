public class q4 {
     public static void main(String[] args) 
    {
        int n=1;
        int m=n;
        int y=0;
        while(m>0)
        {
            int d=m%10;
            y=10*y + d;
            m=m/10;
        }
        if(y==n)
            System.out.println("Yes");
        else    
            System.out.println("No");
        
    }
    //x3 to store the number to check--n
    //x4 to keep a copy of the number--m
    //x5 to store the final output--y
    
}
