package pintodbsimulation;

import java.util.Random;

/**
 * This class is use to generate randon numbers and random variables values, 
 * that respect an specific distribution.
 *
 * @author B65477 B65728 B55830
 */
public class RandomNumberGenerator {

    private final Random r;

    /**
     * Class constructor.
     */
    public RandomNumberGenerator() {
        long seed = System.nanoTime();
        r = new Random(seed);
    }

    /**
     * Returns a randon number.
     *
     * @return Double random number
     */
    public double getRandNumb() {
        return r.nextDouble();
    }

    /**
     * Returns a random variable value that respect the normal distribution, 
     * with parameter mean and variance.
     * This random variable value is generate with the convolution method.
     *
     * @param mean
     * @param variance
     * @return Double random variable value
     */
    public double getTimeUsingNormalDist(double mean, double variance) {
        // Use convolution method to generate X value
        double sumRandNumber = 0.0;
        for (int counter = 0; counter < 12; ++counter) {
            sumRandNumber += this.getRandNumb();
        }
        return mean + Math.sqrt(variance) * (sumRandNumber - 6.0);
    }

    /**
     * Returns a random variable value that respect the uniform distribution, 
     * with parameter a and b.
     * This random variable value is generate with the inverse transform method 
     * obteined formula for this distribution.
     *
     * @param a
     * @param b
     * @return Double random variable value
     */
    public double getTimeUsingUniformDist(double a, double b) {
        return (b - a) * (this.getRandNumb()) + a;
    }

    /**
     * Returns a random variable value that respect the exponencial distribution, 
     * with parameter lambda.
     * This random variable value is generate with the inverse transform method 
     * obteined formula for this distribution.
     * 
     * @param lambda
     * @return Double random variable value
     */
    public double getTimeUsingExponencialDist(double lambda) {
        return (Math.log(this.getRandNumb()) * (-1.0 / lambda));
    }

    /**
     * Return a StatementType calculate using a generated randon number and the Monte Carlo method.
     *
     * @return An random StatementType that respect the discrete distribution given on this project 
     */
    public StatementType getConnectionStatementType() {
        // Use Monte Carlo Method
        double randomNumber = this.getRandNumb();

        if (randomNumber >= 0 && randomNumber < 0.30) {
            return StatementType.SELECT;

        } else if (randomNumber >= 0.30 && randomNumber < 0.55) {
            return StatementType.UPDATE;

        } else if (randomNumber >= 0.55 && randomNumber < 0.90) {

            return StatementType.JOIN;

        } else {
            return StatementType.DDL;
        }
    }
}
