public class q2 
{
    public static void main(String[] args)
    {
        int n=8;
        int a[] = {70,
            80,
            40,
            20,
            10,
            30,
            50,
            60};
        int i=0;
        while(i<n)
        {
            int j=i;
            while(j<n)
            {
                if(a[j]>a[i])
                {
                    int temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            j++;
            }
        i++;
        }
        i=0;
        for(;i<n;i++){
            System.out.println(a[i]);
        }
    }
    //x3 to store the size of the array
    //x4 for loop variable i
    //x5 for loop variable j
    //x6 for temp variable
    //x7 for a[i]
    //x8 for a[j]
}
