public class q3 {
    public static void main(String[] args) 
    {
        int n=10;
        int arr[]= new int[n];
        arr[0]=0;
        arr[1]=1;
        for(int i=2; i<n; i++)
        {
            arr[i]=arr[i-1]+arr[i-2];
        }
        for(int i=0; i<n; i++)
            System.out.println(arr[i]);
        
        
    }
    //x3 to store 2^16 -1 and further diminishing values
    //x4 to store 1
    //x5 as the loop variable 
    //x6 to store n
    //x7 to store arr[i-1]
    //x8 to store arr[i-2]
    //x9 to store arr[i]
    
}
