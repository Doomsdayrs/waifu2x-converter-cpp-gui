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
    public String input;
    public String output;


    public String block_Size;

    public boolean disable_Gpu;
    public boolean force_Opencl;

    public String processor;
    public String jobs;
    public String scale_Ratio;
    public String noise_level;
    public String mode;
    public boolean quiet;
    /**
     * Must be either 0 or 1
     */
    public boolean recursive_directory;
    private String model_Dir;

    public String getSettings() {
        String output = " -i " + input + " -o " + " --block_size " + block_Size;
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
