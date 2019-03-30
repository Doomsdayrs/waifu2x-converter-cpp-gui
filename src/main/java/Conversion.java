
/**
 * This file is part of Waifu2xConverterCppGui.
 * Waifu2xConverterCppGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Waifu2xConverterCppGui.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * Waifu2xConverterCppGui
 * 08 / December / 2018
 *
 * @author github.com/doomsdayrs
 */


public class Conversion {

    Conversion()
    {

        this.mode = "noise_scale";
        this.block_Size = "256";
        this.jobs = "1";
        this.scale_Ratio = "1.0";
        this.noise_level = "1";
        this.processor = "0";
    }

    public Conversion(String scaling, String blockSize,String Jobs, String scaleRatio)
    {
        this.mode = scaling;
        this.block_Size = blockSize;
        this.jobs = Jobs;
        this.scale_Ratio = scaleRatio;
    }

    String input;
    String output;

    String block_Size;

    boolean disable_Gpu;
    boolean force_Opencl;

    String processor;
    String jobs;
    String scale_Ratio;
    String noise_level;
    String mode;
    public boolean quiet;
    /**
     * Must be either 0 or 1
     */
    public boolean recursive_directory;

    private String model_Dir;

    String getOutputHeaders()
    {
        String output = "";
        if (recursive_directory)
            output+=" (Folder)";
        else output += " (Photo)";
        output += " (" + mode + ")";
        output += " (Level"+noise_level+")";
        output += " (x" + scale_Ratio + ")";
        return output;
    }

    String getSettings(String outputFile) {
        String output = " -i '" + input + "' -o " + outputFile +" --block_size " + block_Size;
        if (disable_Gpu)
            output += " --disable-gpu";
        if (force_Opencl)
            output += " --force-OpenCL";
        output += " -p " + processor;
        output += " -j " + jobs;
        output += " --scale_ratio " + scale_Ratio;
        output += " --noise_level " + noise_level;
        if (recursive_directory)
            output += " -r 1";
        else output += " -r 0";

        output += " -m " + mode;
        return output;
    }
}
