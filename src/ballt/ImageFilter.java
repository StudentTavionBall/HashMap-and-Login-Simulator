/*
 * Course: CSC-1120
 * Lab Assignment 3 - Image Manipulator 3000
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/04/2026
 */
package ballt;

import javafx.scene.image.Image;

/**
 * Class to apply filters on images
 */
public final class ImageFilter {

    /**
     * method to apply filter on image using a kernel
     * and the image.util method convolve
     * @param image to apply filter to
     * @param kernel filter that is going to be applied
     * @return image with filter
     * @throws IllegalArgumentException if kernel is not right size
     * or if image is null
     */
    public static Image applyKernel(Image image, int[][] kernel)
            throws IllegalArgumentException{
        final int kernelSize = 9;
        if(kernel.length!=3 || kernel[0].length!=3 || image==null){
            throw new IllegalArgumentException("Kernel is not right size or image" +
                    " is null!!!");
        }
        int sum = 0;
        double[] doubleKernel = new double[kernelSize];
        for (int[] ints : kernel) {
            for (int j = 0; j < kernel[0].length; j++) {
                sum += ints[j];
            }
        }
        if(sum == 0){
            throw new IllegalArgumentException("Sum of filter cannot be zero!!!");
        }
        int iteration = 0;
        for (int[] ints : kernel) {
            for (int i = 0; i < kernel[0].length; i++) {
                doubleKernel[iteration] = ints[i]/(double)sum;
                iteration++;
            }
        }
        return ImageUtil.convolve(image, doubleKernel);
    }

}
