package pintodbsimulation;

import java.util.Random;


public class RandomNumberGenerator {
    
    private final Random r;
    
     /**
     *
     */    
    public RandomNumberGenerator()
    {
        r = new Random(46811);
    }
    
     /**
     *
     * @return 
     */    
    public double getRandNumb( )
    {
        return r.nextDouble();
    }
    
     /**
     *
     * @param mean
     * @param variance
     * @return 
     */    
    public double getTimeUsingNormalDist(double mean, double variance)
    {                
        // Use convolution method to generate X value
        double sumRandNumber = 0.0;
        for (int counter = 0; counter < 12; ++counter)
        {
            sumRandNumber += this.getRandNumb();
        }
        return mean + Math.sqrt( variance )*( sumRandNumber -6.0);
    }
    
     /**
     *
     * @param a
     * @param b
     * @return 
     */    
    public double getTimeUsingUniformDist( double a , double b)
    {
        return (b -a )*(this.getRandNumb()) + a;
    }
    
     /**
     *
     * @param lambda
     * @return 
     */    
    public double getTimeUsingExponencialDist( double lambda)
    {
        return -1.0*( Math.log( this.getRandNumb() )/ (lambda) );
    }
    
     /**
     *
     * @return 
     */    
    public StatementType  getConnectionStatementType( )
    {
        // Use Monte Carlo Method
        double randomNumber = this.getRandNumb();
        
        if (randomNumber >= 0 && randomNumber < 0.30 )
        {
            return StatementType.SELECT;
            
        }else if ( randomNumber >= 0.30 && randomNumber < 0.55 )
        {
            return StatementType.UPDATE;
            
        }else if ( randomNumber >= 0.55 && randomNumber < 0.90 )
        {   
            
            return StatementType.JOIN;
            
        }else
        {
            return StatementType.DDL;
        }
    }
}
