package node;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OpenCVTransformer {
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private static final Size BLUR_SIZE = new Size(3,3);

    /**
     * Perform the transformation of an image
     * @param imagePath path leading to the image
     * @param outputPath path leading to where the transformed image should go
     * @return Object containing the transformation status and a msg
     */
    public TransformationData perform(String imagePath, String outputPath) {
        
        int lowThresh = 5;
        Mat srcBlur = new Mat();
        Mat detectedEdges = new Mat();
        Mat dst = new Mat();

        Mat src = Imgcodecs.imread(imagePath);

        if (src.empty()) { // Image path doesn't exist
            String localErrorMsg = "ERROR: " + imagePath + " is an empty image.";
            System.out.println(localErrorMsg);
            return new TransformationData(
                TransformationStatus.FAILURE,
                localErrorMsg
            );
        }
        
        // do transformation
        Imgproc.blur(src, srcBlur, BLUR_SIZE);
        Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
        src.copyTo(dst, detectedEdges);

        // save file
        Imgcodecs.imwrite(outputPath, dst);

        return new TransformationData( // Return a successful image transformation
            TransformationStatus.SUCCESS,
            imagePath + " successfully transformed."
        );
    }
}

