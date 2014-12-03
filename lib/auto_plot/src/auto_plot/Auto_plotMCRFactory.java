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

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class Auto_plotMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "auto_plot_C5142D412B17A2429ACD0B1A4539B562";
    
    /** Component name */
    private static final String sComponentName = "auto_plot";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(Auto_plotMCRFactory.class)
        );
    
    
    private Auto_plotMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            Auto_plotMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{7,15,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
