/*
 * MATLAB Compiler: 4.15 (R2011a)
 * Date: Fri Oct 25 21:11:52 2013
 * Arguments: "-B" "macro_default" "-W" "java:auto_plot,Plot" "-T" "link:lib" "-d" 
 * "G:\\Program Files\\MATLAB\\R2011a\\bin\\auto_plot\\src" "-w" 
 * "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{Plot:G:\\Program Files\\MATLAB\\R2011a\\bin\\draw_curve_auto.m}" 
 */

package auto_plot;

import com.mathworks.toolbox.javabuilder.pooling.Poolable;
import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The <code>PlotRemote</code> class provides a Java RMI-compliant interface to the 
 * M-functions from the files:
 * <pre>
 *  G:\\Program Files\\MATLAB\\R2011a\\bin\\draw_curve_auto.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>PlotRemote</code> 
 * instance when it is no longer needed to ensure that native resources allocated by this 
 * class are properly freed, and the server-side proxy is unexported.  (Failure to call 
 * dispose may result in server-side threads not being properly shut down, which often 
 * appears as a hang.)  
 *
 * This interface is designed to be used together with 
 * <code>com.mathworks.toolbox.javabuilder.remoting.RemoteProxy</code> to automatically 
 * generate RMI server proxy objects for instances of auto_plot.Plot.
 */
public interface PlotRemote extends Poolable
{
    /**
     * Provides the standard interface for calling the <code>draw_curve_auto</code> 
     * M-function with 9 input arguments.  
     *
     * Input arguments to standard interface methods may be passed as sub-classes of 
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of any 
     * supported Java type (i.e. scalars and multidimensional arrays of any numeric, 
     * boolean, or character type, or String). Arguments passed as Java types are 
     * converted to MATLAB arrays according to default conversion rules.
     *
     * All inputs to this method must implement either Serializable (pass-by-value) or 
     * Remote (pass-by-reference) as per the RMI specification.
     *
     * M-documentation as provided by the author of the M function:
     * <pre>
     * %clear;
     * 
     * %X=[376.764,380.313,384.284,384.8895,385.3935,389.3345,391.0655,392.761,395.5595,397.483,400.8515,401.9085,402.713,403.199,404.9065,405.852,407.9065,409.033,410.0295,411.615,412.1975,411.805,413.8975,415.171,416.73,419.5025,419.1365,420.9335,421.458,423.941,425.7395,424.778,426.7575,426.897,429.243,431.036,431.1985,432.188,433.852,435.5815,437.1245,437.0555,438.331,439.223,440.867,443.0445,443.8735,443.6055,445.6505,446.942];
     * 
     * %Y=[376.7765,380.065,381.4805,380.879,382.532,381.4815,382.5705,382.661,384.1745,384.336,385.541,384.7995,386.1355,385.489,387.309,387.241,387.6745,387.332,387.0015,388.3395,388.6035,389.167,390.487,391.0235,392.632,393.6695,392.4575,394.104,392.9555,394.4985,394.0435,396.643,397.737,398.273,397.5285,398.562,399.985,400.327,400.269,400.4535,401.085,401.494,402.638,403.6935,404.739,405.1055,405.527,406.8595,406.3925,407.492];
     * 
     * %Z=[377.4185,379.023,377.2555,382.0115,383.6175,384.5495,385.07,388.948,389.333,390.583,393.562,395.68,395.554,397.636,399.1845,401.8375,400.7935,403.498,404.5965,406.3565,406.3485,411.992,411.9635,412.893,416.1215,416.412,417.3735,419.272,419.5945,421.4665,421.7635,424.2115,424.5345,425.5225,426.701,428.261,429.0965,430.081,429.4745,431.422,433.253,434.2675,435.227,435.7935,437.093,440.091,441.137,441.8365,444.9925,443.2075];
     * 
     * %M=[377.224,379.702,380.8235,381.186,383.4325,384.385,387.064,388.358,390.162,391.225,393.895,394.5285,396.2845,398.969,401.2005,401.9755,402.469,405.6215,406.931,407.574,408.2115,411.5105,412.2195,415.6325,415.334,416.403,417.0575,419.1645,420.9105,421.9135,422.2495,424.1955,425.585,427.0025,427.059,427.6925,428.918,430.6175,431.878,433.1225,433.692,435.131,435.534,436.481,438.4365,439.145,440.0165,441.1455,443.9525,444.882];
     * %title('ICM模型上四种算法的传播结果');
     * </pre>
     *
     * @param rhs The inputs to the M function.
     *
     * @return Array of length nargout containing the function outputs. Outputs are 
     * returned as sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>. 
     * Each output array should be freed by calling its <code>dispose()</code> method.
     *
     * @throws java.jmi.RemoteException An error has occurred during the function call or 
     * in communication with the server.
     */
    public Object[] draw_curve_auto(Object... rhs) throws RemoteException;
  
    /** Frees native resources associated with the remote server object */
    void dispose() throws RemoteException;
}
