public class q5
{
    public static void main(String[] args) 
    {
        int n=23;
        int i=1,c=0;
        while(i<=n){
            if(n%i==0){
                c=c+1;
            }
            i++;
        }

        if(c==2)
            System.out.println("Prime");
        else 
            System.out.println("Not Prime");
    }
    // Store the variable in x3
    // store the counter in x4
    //Store the loop variable in x5
    //to store the quotient in x6
    //to store 2 in x8
}
